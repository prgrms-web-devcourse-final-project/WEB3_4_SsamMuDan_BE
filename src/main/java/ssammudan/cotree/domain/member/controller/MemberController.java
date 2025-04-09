package ssammudan.cotree.domain.member.controller;

import java.util.Date;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.member.dto.MemberRecoverSmsRequest;
import ssammudan.cotree.domain.member.dto.MemberRecoverSmsResponse;
import ssammudan.cotree.domain.member.dto.MemberRecoverSmsVerifyRequest;
import ssammudan.cotree.domain.member.dto.info.MemberInfoRequest;
import ssammudan.cotree.domain.member.dto.info.MemberInfoResponse;
import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupSmsRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupSmsVerifyRequest;
import ssammudan.cotree.domain.member.service.MemberService;
import ssammudan.cotree.domain.phone.service.SmsService;
import ssammudan.cotree.global.config.security.jwt.AccessTokenService;
import ssammudan.cotree.global.config.security.jwt.RefreshTokenService;
import ssammudan.cotree.global.config.security.jwt.TokenBlacklistService;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;
import ssammudan.cotree.model.member.member.entity.Member;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {
	private final MemberService memberService;
	private final AccessTokenService accessTokenService;
	private final RefreshTokenService refreshTokenService;
	private final TokenBlacklistService tokenBlacklistService;
	private final SmsService smsService;

	@GetMapping
	@Operation(summary = "회원 조회", description = "로그인한 회원의 정보를 제공합니다.")
	public BaseResponse<MemberInfoResponse> getMyInfo(
		@AuthenticationPrincipal CustomUser customUser
	) {
		MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(customUser.getId());
		return BaseResponse.success(SuccessCode.MEMBER_INFO_REQUEST_SUCCESS, memberInfoResponse);
	}

	@PutMapping
	@Operation(summary = "회원정보 변경", description = "회원정보를 변경합니다. (전체변경)")
	public BaseResponse<Void> putMyInfo(
		@Valid @RequestBody MemberInfoRequest request,
		@AuthenticationPrincipal CustomUser customUser
	) {
		String memberId = customUser.getId();
		memberService.updateMember(memberId, request);

		return BaseResponse.success(SuccessCode.MEMBER_INFO_UPDATE_SUCCESS);
	}

	@PostMapping("/signup")
	@Operation(summary = "회원가입", description = "사용자 정보를 통해 회원가입을 진행합니다.")
	public BaseResponse<Void> signUp(@Valid @RequestBody MemberSignupRequest request) {
		memberService.signUp(request);
		return BaseResponse.success(SuccessCode.MEMBER_CREATE_SUCCESS);
	}

	@PostMapping("/signin")
	@Operation(summary = "로그인", description = "이메일과 비밀번호를 이용해 로그인을 진행합니다.")
	public BaseResponse<Void> signIn(@Valid @RequestBody MemberSigninRequest signinRequest,
		HttpServletResponse response) {
		Member member = memberService.signIn(signinRequest);
		CustomUser signInMember = new CustomUser(member, null);
		signInMember.setLogin();

		String accessToken = accessTokenService.generateToken(signInMember);
		long accessTokenExpirationSeconds = accessTokenService.getExpirationSeconds(); //accessTokenExpiration.getTime() - new Date().getTime();

		String refreshToken = refreshTokenService.generateToken(signInMember);
		long refreshTokenExpirationSeconds = refreshTokenService.getExpirationSeconds();//refreshTokenExpiration.getTime() - new Date().getTime();

		setValueToCookie("access_token", accessToken, accessTokenExpirationSeconds, response);
		setValueToCookie("refresh_token", refreshToken, refreshTokenExpirationSeconds, response);

		return BaseResponse.success(SuccessCode.MEMBER_SIGNIN_SUCCESS);
	}

	@GetMapping("/signout")
	@Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
	public BaseResponse<Void> signOut(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = getValueInCookie("refresh_token", request);
		long remainingTime = refreshTokenService.getClaimsFromToken(refreshToken)
			.getExpiration().getTime() - new Date().getTime();

		tokenBlacklistService.addToBlacklist(refreshToken, remainingTime);

		response.addHeader("Set-Cookie", "access_token=; Max-Age=0; Path=/; HttpOnly; SameSite=None; Secure");
		response.addHeader("Set-Cookie", "refresh_token=; Max-Age=0; Path=/; HttpOnly; SameSite=None; Secure");
		return BaseResponse.success(SuccessCode.MEMBER_SIGNOUT_SUCCESS);
	}

	@PatchMapping("/recovery/password")
	@Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
	public BaseResponse<Void> recoveryPassword(@RequestParam String password,
		@AuthenticationPrincipal CustomUser customUser) {
		memberService.updatePassword(customUser.getId(), password);
		return BaseResponse.success(SuccessCode.MEMBER_PASSWORD_UPDATE_SUCCESS);
	}

	@PostMapping("/signup/phone")
	@Operation(summary = "회원가입 전화번호 인증 코드 전송", description = "회원가입 전화번호 인증 코드를 전송합니다")
	public BaseResponse<Void> sendSignupCode(@Valid @RequestBody MemberSignupSmsRequest request) {
		smsService.sendSignupCode(request);
		return BaseResponse.success(SuccessCode.MEMBER_SIGNUP_CODE_SEND_SUCCESS);
	}

	@PostMapping("/signup/phone/verify")
	@Operation(summary = "회원가입 전화번호 인증 확인", description = "화원가입 전화번호 인증번호를 확인합니다.")
	public BaseResponse<Void> verifySignupCode(@Valid @RequestBody MemberSignupSmsVerifyRequest request) {
		smsService.verifySignupCode(request);
		return BaseResponse.success(SuccessCode.MEMBER_SIGNUP_CODE_VERIFY_SUCCESS);
	}

	@PostMapping("/recovery/loginId")
	@Operation(summary = "로그인 아이디 찾기 전화번호 인증 코드전송", description = "로그인 아이디 찾기 인증번호를 전송합니다.")
	public BaseResponse<Void> recoveryLoginId(@Valid @RequestBody MemberRecoverSmsRequest request) {
		smsService.recoverLoginId(request);
		return BaseResponse.success(SuccessCode.MEMBER_RECOVER_CODE_SEND_SUCCESS);
	}

	@PostMapping("/recovery/loginId/verify")
	@Operation(summary = "로그인 아이디 찾기 전화번호 인증 확인", description = "로그인 아이디 찾기 인증번호를 확인합니다.")
	public BaseResponse<MemberRecoverSmsResponse> verifyLoginId(
		@Valid @RequestBody MemberRecoverSmsVerifyRequest request) {
		return BaseResponse.success(SuccessCode.MEMBER_RECOVER_CODE_VERIFY_SUCCESS,
			smsService.verifyRecoverLoginId(request));
	}

	private String getValueInCookie(String value, HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(value)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	private void setValueToCookie(String name, String value, long maxAge, HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
			.path("/")
			.httpOnly(true) // XSS 방지
			.secure(true) // SameSite=None;은 Secure=true;가 필수(정책)
			.sameSite("None") // 모든 도메인의 요청에서 쿠키 전송 (Lax는 GET 요청에 대해서만 쿠키 전송, Strict는 동일 출처에서만)
			.maxAge(maxAge)
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}
}
