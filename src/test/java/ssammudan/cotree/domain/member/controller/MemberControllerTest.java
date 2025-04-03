package ssammudan.cotree.domain.member.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.service.MemberService;
import ssammudan.cotree.global.config.security.exception.CustomAccessDeniedHandler;
import ssammudan.cotree.global.config.security.exception.CustomAuthenticationEntryPoint;
import ssammudan.cotree.global.config.security.jwt.AccessTokenService;
import ssammudan.cotree.global.config.security.jwt.RefreshTokenService;
import ssammudan.cotree.global.config.security.jwt.TokenBlacklistService;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.config.security.user.CustomUserDetailsService;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

/**
 * PackageName : ssammudan.cotree.domain.member.controller
 * FileName    : MemberControllerTest
 * Author      : hc
 * Date        : 25. 3. 28.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.     hc               Initial creation
 */
@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MemberService memberService;

	@MockitoBean
	private AccessTokenService accessTokenService;

	@MockitoBean
	private RefreshTokenService refreshTokenService;

	@MockitoBean
	private TokenBlacklistService tokenBlacklistService;

	@MockitoBean
	private CustomUserDetailsService userDetailsService;

	@MockitoBean
	private CustomAuthenticationEntryPoint authenticationEntryPoint;

	@MockitoBean
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("로그인 테스트, 토큰 발급 확인")
	public void signIn() throws Exception {
		// given (로그인 요청 DTO 및 반환될 Member 객체 정의)
		MemberSigninRequest signinRequest = new MemberSigninRequest("test123@naver.com", "password");
		Member mockMember = new Member(
			"randomUUID()",
			"test123@naver.com",
			"이름",
			"닉네임",
			"비밀번호",
			"01012345678",
			null,
			MemberRole.USER,
			MemberStatus.ACTIVE
		);

		given(memberService.signIn(any(MemberSigninRequest.class)))
			.willReturn(mockMember);  // 가짜 Member 반환

		given(accessTokenService.generateToken(any(CustomUser.class)))
			.willReturn("jwt_accessToken");

		given(refreshTokenService.generateToken(any(CustomUser.class)))
			.willReturn("jwt_refreshToken");

		given(accessTokenService.getExpirationSeconds())
			.willReturn(1000L);

		given(refreshTokenService.getExpirationSeconds())
			.willReturn(1000L);

		String requestJson = objectMapper.writeValueAsString(signinRequest); // JSON 변환

		// when & then (로그인 요청 테스트)
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member/signin")
				.contentType("application/json")
				.content(requestJson))
			.andExpect(status().isOk())
			.andExpect(cookie().exists("access_token"))
			.andExpect(cookie().httpOnly("access_token", true))
			.andExpect(cookie().secure("access_token", true))
			.andExpect(cookie().value("access_token", "jwt_accessToken"))
			.andExpect(cookie().exists("refresh_token"))
			.andExpect(cookie().httpOnly("refresh_token", true))
			.andExpect(cookie().secure("refresh_token", true))
			.andExpect(cookie().value("refresh_token", "jwt_refreshToken"));
	}
}
