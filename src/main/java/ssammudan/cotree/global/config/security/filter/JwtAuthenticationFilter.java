package ssammudan.cotree.global.config.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.global.config.security.jwt.AccessTokenService;
import ssammudan.cotree.global.config.security.jwt.RefreshTokenService;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.config.security.user.CustomUserDetailsService;

/**
 * PackageName : ssammudan.cotree.global.config.security.filter
 * FileName    : JwtAuthenticationFilter
 * Author      : hc
 * Date        : 25. 3. 31.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.     hc               Initial creation
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final AccessTokenService accessTokenService;
	private final RefreshTokenService refreshTokenService;
	private final CustomUserDetailsService customUserDetailsService;

	private static final List<String> PERMIT_ALL_URIS = List.of(
		// H2, Swagger, 에러
		"/h2-console",
		"/error",
		"/favicon.ico",
		"/swagger-ui",
		"/v3/api-docs",
		"/swagger-resources",
		"/swagger-ui.html",
		"/actuator/health",

		// MEMBER Domain
		"/api/v1/member/signup",
		"/api/v1/member/signin",
		"/api/v1/member/signup/phone",
		"/api/v1/member/signup/phone/verify",
		"/api/v1/member/recovery/loginId",
		"/api/v1/member/recovery/loginId/verify",
		"/api/v1/member/update/password",
		"/oauth2",
		"/login",

		// Email
		"/api/v1/email",

		// Category
		"/api/v1/category",

		// Comment
		"/api/v1/comment",

		// Community
		"/api/v1/community/board",

		// Education
		"/api/v1/education/techbook",
		"/api/v1/education/techtube",
		"/api/v1/education/review",

		// Resume
		"/api/v1/recruitment/resume",

		// Project
		"/api/v1/project/team"
	);

	private boolean isPermitAllUri(HttpServletRequest request) {
		String uri = request.getRequestURI();

		// "/swagger-ui/index.html"처럼 접두사만 비교할 수 있도록 startsWith 사용
		return PERMIT_ALL_URIS.stream().anyMatch(uri::startsWith);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws
		ServletException, IOException {
		if (!request.getRequestURI().startsWith("/api/v1/")) {
			filterChain.doFilter(request, response);
			return;
		}

		if (isPermitAllUri(request)) {
			filterChain.doFilter(request, response); // JWT 인증 없이 통과
			return;
		}

		String accessToken = getAccessToken(request);
		String refreshToken = getRefreshToken(request);

		if (accessToken == null && refreshToken == null) {
			filterChain.doFilter(request, response);
			return;
		}

		CustomUser user = null;
		if (accessToken != null) {
			user = customUserDetailsService.loadUserByAccessToken(accessToken);
		}

		// accessToken이 유효하지 않다면 Refresh토큰을 이용하여 로그인 처리를 한다.
		if (user == null) {
			if (!refreshTokenService.isValidToken(refreshToken)) {
				filterChain.doFilter(request, response);
				return;
			}
			user = customUserDetailsService.loadUserByRefreshToken(refreshToken);

			if (user == null) {
				filterChain.doFilter(request, response);
				return;
			}
			// 헤더에 accessToken을 추가
			accessTokenService.generateTokenToCookie(user, response);
			log.error("accessToken 재발급");
		}

		// 로그인 처리
		user.setLogin();
		filterChain.doFilter(request, response);
	}

	private String getRefreshToken(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("refresh_token")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	private String getAccessToken(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("access_token")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
