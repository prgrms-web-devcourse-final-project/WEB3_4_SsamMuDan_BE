package ssammudan.cotree.domain.phone.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import ssammudan.cotree.domain.member.dto.signup.MemberSignupSmsRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupSmsVerifyRequest;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;

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

	private static final String coolSmsUrl = "https://api.coolsms.co.kr";
	private static final long CODE_EXPIRATION = 3;

	@Value("${SENDER_PHONE_NUMBER}")
	private String senderPhoneNumber;

	public SmsService(
		@Value("${COOL_SMS_API_KEY}") String apiKey,
		@Value("${COOL_SMS_SECRET_KEY}") String apiSecret,
		RedisTemplate<String, String> redisTemplate
	) {
		this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, coolSmsUrl);
		this.redisTemplate = redisTemplate;
	}

	public void sendSignupMsg(MemberSignupSmsRequest request) {

		// 기존의 인증 코드가 남아있을 경우 삭제
		if (redisTemplate.opsForValue().get(request.receiverNumber()) != null) {
			redisTemplate.delete(request.receiverNumber());
		}
		// 랜덤 6자리
		int randomCode = generateRandomCode();

		Message message = new Message();
		message.setFrom(senderPhoneNumber);
		message.setTo(request.receiverNumber());

		message.setText("[cotree]" + "\n" + "인증번호 : " + randomCode);

		try {
			messageService.sendOne(
				new SingleMessageSendingRequest(message));
		} catch (Exception e) {
			throw new GlobalException(ErrorCode.SMS_SEND_FAILED);
		}

		redisTemplate.opsForValue()
			.set(request.receiverNumber(), String.valueOf(randomCode), Duration.ofMinutes(CODE_EXPIRATION));
	}

	public void verifySignupCode(MemberSignupSmsVerifyRequest request) {
		String code = redisTemplate.opsForValue().get(request.receiverNumber());

		if (code == null || !code.equals(request.code())) {
			throw new GlobalException(ErrorCode.MEMBER_SIGNUP_VERIFY_FAILED);
		}
		redisTemplate.delete(request.receiverNumber());
	}

	private int generateRandomCode() {
		return (int)(Math.random() * 900000) + 100000;
	}

}
