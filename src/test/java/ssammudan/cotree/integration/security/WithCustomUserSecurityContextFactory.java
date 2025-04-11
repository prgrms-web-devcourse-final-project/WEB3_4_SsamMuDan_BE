package ssammudan.cotree.integration.security;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;

/**
 * PackageName : ssammudan.cotree.integration.security
 * FileName    : WithCustomUserSecurityContextFactory
 * Author      : Baekgwa
 * Date        : 2025-04-11
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-11     Baekgwa               Initial creation
 */
@Component
@RequiredArgsConstructor
public class WithCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

	private final MemberRepository memberRepository;

	@Override
	public SecurityContext createSecurityContext(WithCustomUser annotation) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		Member findMember = memberRepository.findAll().getFirst();

		CustomUser customUser = new CustomUser(findMember, null);

		Authentication auth = new UsernamePasswordAuthenticationToken(
			customUser, null, List.of(new SimpleGrantedAuthority(findMember.getRole().getRole()))
		);

		context.setAuthentication(auth);
		return context;
	}
}

