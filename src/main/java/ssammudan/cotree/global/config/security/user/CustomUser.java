package ssammudan.cotree.global.config.security.user;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.config.security.oauth.dto.OAuth2Response;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.global.config.security.user
 * FileName    : CustomUser
 * Author      : hc
 * Date        : 25. 3. 31.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.     hc               Initial creation
 */
@RequiredArgsConstructor
public class CustomUser implements OAuth2User, UserDetails {
	private final Member member;
	private final OAuth2Response oAuth2Response;

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getUsername();
	}

	@Override
	public Map<String, Object> getAttributes() { // oAuth로 제공받은 정보들. 굳이 사용할 필요 없음?
		return Map.of();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return member.getAuthorities();
	}

	@Override
	public String getName() {
		return member.getUsername();
	}

	public String getId() {
		return member.getId();
	}

	public void setLogin() {
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(this, null, getAuthorities())
		);
	}
}
