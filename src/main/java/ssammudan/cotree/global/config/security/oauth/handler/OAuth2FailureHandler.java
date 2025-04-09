package ssammudan.cotree.global.config.security.oauth.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.global.config.FrontendConfig;

/**
 * PackageName : ssammudan.cotree.global.config.security.oauth.handler
 * FileName    : OAuth2FailureHandler
 * Author      : hc
 * Date        : 25. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     hc               Initial creation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
	private final FrontendConfig frontendConfig;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		log.error("OAuth2 인증 실패 : {}", exception.getMessage());
		String frontendUrl = frontendConfig.getFrontendUrl();

		response.sendRedirect(frontendUrl + "/login/callback?error=true");
	}
}
