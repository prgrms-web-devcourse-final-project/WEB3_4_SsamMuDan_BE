package ssammudan.cotree.global.config.security.oauth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.config.security.jwt.AccessTokenService;
import ssammudan.cotree.global.config.security.jwt.RefreshTokenService;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.infra.frontend.FrontendConfig;

/**
 * PackageName : ssammudan.cotree.global.config
 * FileName    : OAuth2SuccessHandler
 * Author      : hc
 * Date        : 25. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     hc               Initial creation
 */

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final AccessTokenService accessTokenService;
	private final RefreshTokenService refreshTokenService;
	private final FrontendConfig frontendConfig;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication)
		throws IOException, ServletException {

		String frontendUrl = frontendConfig.getPrimaryFrontendUrl();

		CustomUser loginUser = (CustomUser)authentication.getPrincipal();

		// 새로운 리프레시 토큰 발급 및 쿠키 저장
		accessTokenService.generateTokenToCookie(loginUser, response);
		refreshTokenService.generateTokenToCookie(loginUser, response);

		response.sendRedirect(frontendUrl + "/login/callback");
	}
}
