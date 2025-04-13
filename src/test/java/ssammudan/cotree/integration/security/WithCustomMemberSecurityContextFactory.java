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
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.entity.MemberFactory;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.member.member.type.MemberRole;

/**
 * PackageName : ssammudan.cotree.integration.security
 * FileName    : WithCustomMemberSecurityContextFactory
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
public class WithCustomMemberSecurityContextFactory implements WithSecurityContextFactory<WithCustomMember> {

	private final MemberRepository memberRepository;

	@Override
	public SecurityContext createSecurityContext(WithCustomMember annotation) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		Member newMember = MemberFactory.createSignUpMember(
			new MemberSignupRequest("mockMember", "!asdf1234", "mock member", "mock member nickname",
				MemberRole.USER.getRole(), "01099999999")
		);

		Member savedMember = memberRepository.save(newMember);

		CustomUser customUser = new CustomUser(savedMember, null);

		Authentication auth = new UsernamePasswordAuthenticationToken(
			customUser, null, List.of(new SimpleGrantedAuthority(savedMember.getRole().getRole()))
		);

		context.setAuthentication(auth);
		return context;
	}
}

