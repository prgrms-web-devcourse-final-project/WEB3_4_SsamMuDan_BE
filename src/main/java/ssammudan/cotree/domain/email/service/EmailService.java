package ssammudan.cotree.domain.email.service;

import java.security.SecureRandom;
import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;

/**
 * PackageName : ssammudan.cotree.domain.email
 * FileName    : EmailService
 * Author      : hc
 * Date        : 25. 4. 4.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     hc               Initial creation
 */
@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;

	private final RedisTemplate<String, String> redisTemplate;

	private void sendSimpleMailMessage(String email, String subject, String text) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

		try {
			// 메일을 받을 수신자 설정
			simpleMailMessage.setTo(email);
			// 메일의 제목 설정
			simpleMailMessage.setSubject(subject);
			// 메일의 내용 설정
			simpleMailMessage.setText(text);

			javaMailSender.send(simpleMailMessage);
		} catch (Exception e) {
			throw new GlobalException(ErrorCode.EMAIL_SEND_FAILED);
		}

	}

	private void sendMimeMessage(String email, String subject, String title, String body) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

			// 메일을 받을 수신자 설정
			mimeMessageHelper.setTo(email);
			// 메일의 제목 설정
			mimeMessageHelper.setSubject(subject);

			// html 문법 적용한 메일의 내용
			String content = String.format("""
				<!DOCTYPE html>
				<html xmlns:th="http://www.thymeleaf.org">
				<body>
					<div style="margin:100px;">
						<h1> %s </h1>
						<p> 3분 안에 인증해주세요. </p>
						<br>
						<div align="center" style="border:1px solid black;">
							<h3> %s </h3>
						</div>
						<br/>
					</div>
				</body>
				</html>
				""", title, body);

			// 메일의 내용 설정
			mimeMessageHelper.setText(content, true);

			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new GlobalException(ErrorCode.EMAIL_SEND_FAILED);
		}
	}

	@Async
	public void sendCode(String email) {
		// 랜덤 인증코드 생성
		SecureRandom random = new SecureRandom();
		String code = String.format("%06d", random.nextInt(1_000_000));

		// 인증 코드 email 전송
		String subject = "[Cotree] 인증번호 발송";
		String title = "이메일 인증번호";
		String body = "인증번호 : " + code;

		sendMimeMessage(email, subject, title, body);

		// Redis에 인증 코드 저장
		redisTemplate.opsForValue().set("signup:email:%s".formatted(email), code, Duration.ofMinutes(3));
	}

	public void verifyCode(String email, String code) {
		String redisCode = redisTemplate.opsForValue().get("signup:email:%s".formatted(email));

		if (redisCode == null || !redisCode.equals(code)) {
			throw new GlobalException(ErrorCode.EMAIL_VERIFY_FAILED);
		}
		redisTemplate.expire("signup:email:%s".formatted(email), Duration.ofMinutes(10)); // 인증 코드 만료 시간 연장(10분)
	}
}
