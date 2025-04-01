package ssammudan.cotree.domain.member.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.domain.member.service.MemberService;
import ssammudan.cotree.global.config.security.jwt.AccessTokenService;
import ssammudan.cotree.global.config.security.jwt.RefreshTokenService;
import ssammudan.cotree.global.config.security.jwt.TokenBlacklistService;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {
	private final MemberService memberService;
	private final AccessTokenService accessTokenService;
	private final RefreshTokenService refreshTokenService;
	private final TokenBlacklistService tokenBlacklistService;

	@PostMapping("/signup")
	@Operation(summary = "회원가입", description = "이메일을 이용해 회원가입을 진행합니다.")
	public BaseResponse<Void> signUp(@Valid @RequestBody MemberSignupRequest request) {
		memberService.signUp(request);
		return BaseResponse.success(SuccessCode.MEMBER_CREATE_SUCCESS);
	}

	@PostMapping("/signin")
	@Operation(summary = "로그인", description = "이메일과 비밀번호를 이용해 로그인을 진행합니다.")
	public BaseResponse<Void> signIn(@Valid @RequestBody MemberSigninRequest signinRequest,
		HttpServletResponse response) {
		CustomUser signInMember = memberService.signIn(signinRequest);
		signInMember.setLogin();

		accessTokenService.generateTokenToCookie(signInMember, response);
		refreshTokenService.generateTokenToCookie(signInMember, response);

		return BaseResponse.success(SuccessCode.MEMBER_SIGNIN_SUCCESS);
	}

}
