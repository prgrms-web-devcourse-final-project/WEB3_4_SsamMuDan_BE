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

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws
		ServletException, IOException {
		if (!request.getRequestURI().startsWith("/api/v1/")) {
			filterChain.doFilter(request, response);
			return;
		}

		if (List.of("/api/v1/member/signup", "/api/v1/member/signin").contains(request.getRequestURI())) {
			filterChain.doFilter(request, response);
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
			String memberId = refreshTokenService.getClaimsFromToken(refreshToken).get("mid", String.class);
			user = customUserDetailsService.loadUserById(memberId);

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
