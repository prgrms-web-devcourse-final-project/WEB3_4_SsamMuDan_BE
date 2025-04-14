package ssammudan.cotree.infra.sms;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import net.nurigo.sdk.message.model.Message;

import ssammudan.cotree.domain.member.dto.MemberRecoverSmsResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

/**
 * PackageName : ssammudan.cotree.infra.sms
 * FileName    : SmsServiceTest
 * Author      : kwak
 * Date        : 2025. 4. 13.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 13.     kwak               Initial creation
 */

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	@Mock
	private MessageSender messageSender;

	private String receiverNumber;
	private SmsService smsService;

	@BeforeEach
	void setUp() {
		String phoneNumber = "01012345678";
		receiverNumber = "01011111111";
		smsService = new SmsService(phoneNumber, messageSender, redisTemplate, memberRepository);
	}

	@Test
	@DisplayName("인증번호 정상 발송")
	void sendSignupCode() {
		// given
		String key = "signup:sms:%s".formatted(receiverNumber);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);

		// when
		smsService.sendSignupCode(receiverNumber);

		// then
		verify(redisTemplate).delete(key);
		verify(messageSender).sendOne(any(Message.class));
		verify(valueOperations).set(eq(key), anyString(), eq(Duration.ofMinutes(3)));

	}

	@Test
	@DisplayName("count 5 이상 일 때 MEMBER_SIGNUP_COOLDOWN 발생")
	void sendSignupCodeFailedWithCooldown() {
		// given
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.increment("signup:limit:%s".formatted(receiverNumber), 1L)).thenReturn(6L);

		// when
		// smsService.sendSignupCode(receiverNumber);
		// then
		// verify(valueOperations).set("cooldown:signup:%s".formatted(receiverNumber), eq("1"), eq(Duration.ofMinutes(3)));
		assertThatThrownBy(() -> smsService.sendSignupCode(receiverNumber))
			.isInstanceOf(GlobalException.class)
			.hasMessageContaining("너무 많이 시도하였습니다. 잠시 후 재시도 해주세요");
	}

	@Test
	@DisplayName("인증번호 정상 발송 예외 시 SMS_SEND_FAILED 발생")
	void failSendSignupCode() {
		// given
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		doThrow(new GlobalException(ErrorCode.SMS_SEND_FAILED)).when(messageSender).sendOne(any(Message.class));
		// when
		// then
		assertThatThrownBy(() -> smsService.sendSignupCode(receiverNumber))
			.hasMessageContaining("SMS 가 전송 실패하였습니다.")
			.isInstanceOf(GlobalException.class);

	}

	@Test
	@DisplayName("다른 코드가 들어올 시 인증 실패")
	void wrongCodeFailedVerifySignupCode() {
		// given
		String wrongCode = "000000";
		String code = "123456";
		String key = "signup:sms:%s".formatted(receiverNumber);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn(code);
		// when
		// then
		assertThatThrownBy(() -> smsService.verifySignupCode(receiverNumber, wrongCode))
			.isInstanceOf(GlobalException.class)
			.hasMessageContaining("회원가입 인증번호가 일치하지 않습니다");
	}

	@Test
	@DisplayName("인증 코드 검증 정상 수행")
	void verifySignupCode() {
		// given
		String code = "123456";
		String key = "signup:sms:%s".formatted(receiverNumber);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(key)).thenReturn(code);
		// when
		smsService.verifySignupCode(receiverNumber, code);
		// then
		verify(redisTemplate).expire(anyString(), eq(Duration.ofMinutes(10)));
	}

	@Test
	@DisplayName("로그인 아이디 찾기 정상 작동")
	void recoverLoginId() {
		// given
		String username = "test";
		String key = username + receiverNumber;
		when(memberRepository.existsByUsernameAndPhoneNumber(username, receiverNumber)).thenReturn(true);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		// when
		smsService.recoverLoginId(username, receiverNumber);
		// then
		verify(memberRepository).existsByUsernameAndPhoneNumber(username, receiverNumber);
		verify(redisTemplate).delete("loginId:sms:%s".formatted(key));
		verify(messageSender).sendOne(any(Message.class));
		verify(valueOperations).set(eq("loginId:sms:%s".formatted(key)), anyString(), eq(Duration.ofMinutes(3)));
	}

	@Test
	@DisplayName("회원이름과 전화번호가 존재하지 않을때 MEMBER_NOT_FOUND 발생")
	void notExistMemberInRecoverId() {
		// given
		when(memberRepository.existsByUsernameAndPhoneNumber(anyString(), anyString())).thenReturn(false);
		// when
		// then
		assertThatThrownBy(() -> smsService.recoverLoginId(anyString(), anyString()))
			.isInstanceOf(GlobalException.class)
			.hasMessageContaining("회원을 찾을 수 없습니다.");
	}

	@Test
	@DisplayName("아이디 찾기 정상 작동")
	void verifyRecoverLoginId() {
		// given
		String username = "test";
		String key = username + receiverNumber;
		String code = "123456";
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get("loginId:sms:%s".formatted(key))).thenReturn(code);

		Member member = new Member(
			null,
			"test@example.com",
			"김테스트",
			"테스터",
			"password1234",
			"010-1234-5678",
			"https://example.com/profile/default.jpg",
			MemberRole.USER,
			MemberStatus.ACTIVE);

		when(memberRepository.findByUsernameAndPhoneNumber(anyString(), anyString()))
			.thenReturn(Optional.of(member));
		// when
		MemberRecoverSmsResponse response = smsService.verifyRecoverLoginId(username, receiverNumber,
			code);
		// then
		verify(redisTemplate).delete("loginId:sms:%s".formatted(key));
		assertThat(response.email()).isEqualTo("te**@example.com");
	}

	@Test
	@DisplayName("아이디 찾기 시 인증 코드가 일치하지 않을때 SMS_SEND_FAILED 발생")
	void wrongCodeFailedVerifyRecoverLoginId() {
		String username = "testuser";
		String receiverNumber = "01012345678";
		String code = "123456";
		String wrongCode = "000000";
		String key = username + receiverNumber;

		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get("loginId:sms:%s".formatted(key))).thenReturn(code); // 실제 저장된 값과 다름

		// when & then
		assertThatThrownBy(() -> smsService.verifyRecoverLoginId(username, receiverNumber, wrongCode))
			.isInstanceOf(GlobalException.class)
			.hasMessageContaining("SMS 가 전송 실패하였습니다.");
	}
}

