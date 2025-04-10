package ssammudan.cotree.infra.sms;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import ssammudan.cotree.domain.member.dto.MemberRecoverSmsResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;

/**
 * PackageName : ssammudan.cotree.domain.member.service
 * FileName    : SmsService
 * Author      : kwak
 * Date        : 2025. 4. 4.
 * Description : 휴대폰 인증 코드와 관련된 작업 담당
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 4.     kwak               Initial creation
 */
@Service
public class SmsService {
	private final DefaultMessageService messageService;
	private final RedisTemplate<String, String> redisTemplate;
	private final MemberRepository memberRepository;

	private static final String EMAIL_DELIMITER = "@";
	private static final String MASKING = "*";
	private static final String coolSmsUrl = "https://api.coolsms.co.kr";
	private static final long CODE_EXPIRATION = 3;
	private static final String SIGNUP_KEY = "signup:sms:%s";
	private static final String RECOVER_LOGIN_ID_KEY = "loginId:sms:%s";
	private static final String LIMIT_COUNT_SIGNUP_KEY = "signup:limit:%s";
	private static final String LIMIT_COUNT_RECOVERY_LOGIN_ID_KEY = "loginId:limit:%s";
	private static final String COOLDOWN_SIGNUP_KEY = "cooldown:signup:%s";
	private static final String COOLDOWN_RECOVER_LOGIN_ID_KEY = "cooldown:loginId:%s";

	@Value("${SENDER_PHONE_NUMBER}")
	private String senderPhoneNumber;

	public SmsService(
		@Value("${COOL_SMS_API_KEY}") String apiKey,
		@Value("${COOL_SMS_SECRET_KEY}") String apiSecret,
		RedisTemplate<String, String> redisTemplate,
		MemberRepository memberRepository
	) {
		this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, coolSmsUrl);
		this.redisTemplate = redisTemplate;
		this.memberRepository = memberRepository;
	}

	public void sendSignupCode(String receiverNumber) {

		//  쿨다운 키에 존재 시 에러 , 특정 전화번호로 1시간에 최대 5번 시도 가능
		validateSignupCodeSendLimit(receiverNumber);

		// 기존의 인증 코드가 남아있을 경우 삭제
		redisTemplate.delete(SIGNUP_KEY.formatted(receiverNumber));

		// 랜덤 6자리
		int randomCode = generateRandomCode();

		Message message = new Message();
		message.setFrom(senderPhoneNumber);
		message.setTo(receiverNumber);

		message.setText("[cotree]" + "\n" + "인증번호 : " + randomCode);

		try {
			messageService.sendOne(
				new SingleMessageSendingRequest(message));
		} catch (Exception e) {
			throw new GlobalException(ErrorCode.SMS_SEND_FAILED);
		}

		redisTemplate.opsForValue()
			.set(SIGNUP_KEY.formatted(receiverNumber), String.valueOf(randomCode),
				Duration.ofMinutes(CODE_EXPIRATION));
	}

	public void verifySignupCode(String receiverNumber, String requestCode) {
		String key = receiverNumber;
		String code = redisTemplate.opsForValue().get(SIGNUP_KEY.formatted(key));

		if (code == null || !code.equals(requestCode)) {
			throw new GlobalException(ErrorCode.MEMBER_SIGNUP_VERIFY_FAILED);
		}
		redisTemplate.expire(SIGNUP_KEY.formatted(requestCode),
			Duration.ofMinutes(10)); // 인증 코드 만료 시간 연장(10분)
		//redisTemplate.delete(SIGNUP_KEY.formatted(request.receiverNumber()));
	}

	public void recoverLoginId(String username, String receiverNumber) {

		if (!memberRepository.existsByUsernameAndPhoneNumber(username, receiverNumber)) {
			throw new GlobalException(ErrorCode.MEMBER_NOT_FOUND);
		}

		// 이름 + 전화번호 조합
		String key = username + receiverNumber;

		validateRecoverLoginIdSendLimit(key);

		// 기존의 인증 코드가 남아있을 경우 삭제
		redisTemplate.delete(RECOVER_LOGIN_ID_KEY.formatted(key));

		// 랜덤 6자리
		int randomCode = generateRandomCode();

		Message message = new Message();
		message.setFrom(senderPhoneNumber);
		message.setTo(receiverNumber);

		message.setText("[cotree]" + "\n" + "인증번호 : " + randomCode);

		try {
			messageService.sendOne(
				new SingleMessageSendingRequest(message));
		} catch (Exception e) {
			throw new GlobalException(ErrorCode.SMS_SEND_FAILED);
		}

		redisTemplate.opsForValue()
			.set(RECOVER_LOGIN_ID_KEY.formatted(key), String.valueOf(randomCode),
				Duration.ofMinutes(CODE_EXPIRATION));
	}

	public MemberRecoverSmsResponse verifyRecoverLoginId(String username, String receiverNumber, String requestCode) {
		String key = username + receiverNumber;
		String code = redisTemplate.opsForValue().get(RECOVER_LOGIN_ID_KEY.formatted(key));

		if (code == null || !code.equals(requestCode)) {
			throw new GlobalException(ErrorCode.SMS_SEND_FAILED);
		}
		redisTemplate.delete(RECOVER_LOGIN_ID_KEY.formatted(key));

		String maskingEmail = extractMaskingEmail(username, receiverNumber);
		return MemberRecoverSmsResponse.from(maskingEmail);
	}

	private String extractMaskingEmail(String username, String receiverNumber) {
		Member member = memberRepository.findByUsernameAndPhoneNumber(username, receiverNumber)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

		String[] part = member.getEmail().split(EMAIL_DELIMITER);
		String localPart = part[0];
		String domain = part[1];

		int visibleLength = localPart.length() / 2;

		return localPart.substring(0, visibleLength) + MASKING.repeat(localPart.length() - visibleLength) + domain;
	}

	private void validateSignupCodeSendLimit(String key) {
		if (Boolean.TRUE.equals(redisTemplate.hasKey(COOLDOWN_SIGNUP_KEY))) {
			throw new GlobalException(ErrorCode.MEMBER_SIGNUP_COOLDOWN);
		}
		Long count = redisTemplate.opsForValue()
			.increment(LIMIT_COUNT_SIGNUP_KEY.formatted(key), 1);

		if (count != null && count > 5) {
			redisTemplate.opsForValue()
				.set(COOLDOWN_SIGNUP_KEY.formatted(key), "1",
					Duration.ofMinutes(CODE_EXPIRATION));
			throw new GlobalException(ErrorCode.MEMBER_SIGNUP_COOLDOWN);
		}

		if (count != null && count == 1) {
			redisTemplate.expire(LIMIT_COUNT_SIGNUP_KEY.formatted(key), Duration.ofHours(1));
		}
	}

	private void validateRecoverLoginIdSendLimit(String key) {
		if (Boolean.TRUE.equals(redisTemplate.hasKey(COOLDOWN_RECOVER_LOGIN_ID_KEY.formatted(key)))) {
			throw new GlobalException(ErrorCode.MEMBER_RECOVER_COOLDOWN);
		}
		Long count = redisTemplate.opsForValue()
			.increment(LIMIT_COUNT_RECOVERY_LOGIN_ID_KEY.formatted(key), 1);

		if (count != null && count > 5) {
			redisTemplate.opsForValue()
				.set(COOLDOWN_RECOVER_LOGIN_ID_KEY.formatted(key), "1",
					Duration.ofMinutes(CODE_EXPIRATION));
			throw new GlobalException(ErrorCode.MEMBER_RECOVER_COOLDOWN);
		}

		if (count != null && count == 1) {
			redisTemplate.expire(LIMIT_COUNT_RECOVERY_LOGIN_ID_KEY.formatted(key), Duration.ofHours(1));
		}
	}

	private int generateRandomCode() {
		return (int)(Math.random() * 900000) + 100000;
	}

}
