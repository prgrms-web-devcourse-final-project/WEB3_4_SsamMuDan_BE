package ssammudan.cotree.domain.email.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.domain.email.dto.EmailRequest;
import ssammudan.cotree.domain.email.dto.EmailVerifyRequest;
import ssammudan.cotree.domain.email.service.EmailService;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.email
 * FileName    : EmailController
 * Author      : hc
 * Date        : 25. 4. 6.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 6.     hc               Initial creation
 */
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Email", description = "이메일 관련 API")
public class EmailController {
	private final EmailService emailService;

	@PostMapping("/code")
	@Operation(summary = "이메일 인증코드 전송", description = "이메일로 인증코드를 전송합니다.")
	public BaseResponse<Void> sendCode(@Valid @RequestBody EmailRequest emailRequest, HttpServletRequest request) {
		String email = emailRequest.email();
		emailService.sendCode(email);
		return BaseResponse.success(SuccessCode.EMAIL_CODE_SEND_SUCCESS);
	}

	@PostMapping("/code/verify")
	@Operation(summary = "인증코드 유효성 확인", description = "인증코드의 유효성을 확인합니다.")
	public BaseResponse<Void> verifyCode(@Valid @RequestBody EmailVerifyRequest emailVerifyRequest) {
		String email = emailVerifyRequest.email();
		String code = emailVerifyRequest.code();

		emailService.verifyCode(email, code);
		return BaseResponse.success(SuccessCode.EMAIL_CODE_VERIFY_SUCCESS);
	}
}
