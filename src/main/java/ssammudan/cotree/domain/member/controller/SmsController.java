package ssammudan.cotree.domain.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupSmsRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupSmsVerifyRequest;
import ssammudan.cotree.domain.phone.service.SmsService;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.member.controller
 * FileName    : SmsController
 * Author      : kwak
 * Date        : 2025. 4. 4.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 4.     kwak               Initial creation
 */
@RestController
@RequestMapping("/api/v1/member/signup/phone")
@RequiredArgsConstructor
@Tag(name = "MemberSms", description = "회원 Sms 관련 API")
public class SmsController {

	private final SmsService smsService;

	@PostMapping
	@Operation(summary = "회원가입 전화번호 인증 코드 전송", description = "입력하신 전화번호로 인증 코드를 전송합니다")
	public BaseResponse<Void> sendSignupCode(@Valid @RequestBody MemberSignupSmsRequest request) {
		smsService.sendSignupMsg(request);
		return BaseResponse.success(SuccessCode.MEMBER_SIGNUP_CODE_SEND_SUCCESS);
	}

	@PostMapping("/verify")
	@Operation(summary = "회원가입 전화번호 인증 확인", description = "인증번호를 확인합니다.")
	public BaseResponse<Void> verifySignupCode(@Valid @RequestBody MemberSignupSmsVerifyRequest request) {
		smsService.verifySignupCode(request);
		return BaseResponse.success(SuccessCode.MEMBER_SIGNUP_CODE_VERIFY_SUCCESS);
	}

}
