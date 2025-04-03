package ssammudan.cotree.global.security;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import ssammudan.cotree.global.annotation.CustomWithMockUser;
import ssammudan.cotree.global.config.security.oauth.dto.KakaoResponse;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.global.security
 * FileName    : CustomWithSecurityContextFactory
 * Author      : loadingKKamo21
 * Date        : 25. 4. 3.
 * Description : 테스트용 시큐리티 컨텍스트 설정
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 3.     loadingKKamo21       Initial creation
 */
public class CustomWithSecurityContextFactory implements WithSecurityContextFactory<CustomWithMockUser> {

	@Override
	public SecurityContext createSecurityContext(CustomWithMockUser annotation) {
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

		CustomUser customUser = new CustomUser(
			Member.builder()
				.email(annotation.email())
				.password("password123@@")
				.username(annotation.username())
				.nickname(annotation.nickname())
				.phoneNumber("01012345678")
				.role(annotation.role())
				.memberStatus(annotation.status())
				.build(),
			new KakaoResponse(
				Map.of("sub", UUID.randomUUID().toString(), "email", annotation.email(), "name", annotation.username())
			)
		);

		securityContext.setAuthentication(
			new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities())
		);

		return securityContext;
	}

}
