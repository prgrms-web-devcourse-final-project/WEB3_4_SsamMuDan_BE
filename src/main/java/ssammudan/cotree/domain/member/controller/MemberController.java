package ssammudan.cotree.domain.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.domain.member.service.MemberService;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {
	private final MemberService memberService;

	@PostMapping("/signup")
	@Operation(summary = "회원가입", description = "이메일을 이용해 회원가입을 진행합니다.")
	public BaseResponse<Void> signUp(@Valid @RequestBody MemberSignupRequest request) {
		memberService.signUp(request);
		return BaseResponse.success(SuccessCode.MEMBER_CREATE_SUCCESS);
	}
}
