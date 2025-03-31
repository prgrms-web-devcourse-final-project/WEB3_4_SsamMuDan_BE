package ssammudan.cotree.integration.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.entity.MemberFactory;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

/**
 * PackageName : ssammudan.cotree.integration.factory
 * FileName    : MemberDataFactory
 * Author      : Baekgwa
 * Date        : 2025-03-31
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-31     Baekgwa               Initial creation
 */
@Component
@RequiredArgsConstructor
public class MemberDataFactory {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * count : 생성할 회원 수
	 * 회원 데이터는 : [{1, email1@email.com, 이름1, 닉네임1, 패스워드1, 01012340001, MemberRole.USER, MemberStatus.ACTIVE }, ...]
	 */
	public List<Member> createAndSaveMember(final int count) {

		if (count == 0) {
			return List.of();
		}

		List<Member> newMemberList = new ArrayList<>();

		for (int index = 1; index <= count; index++) {

			newMemberList.add(MemberFactory.createSignUpMember(new MemberSignupRequest(
					String.format("email%d@email.com", index),
					passwordEncoder.encode(String.format("패스워드%d", index)),
					String.format("이름%d", index),
					String.format("닉네임%d", index),
					String.format("0101234%4d", index))));
		}

		return memberRepository.saveAll(newMemberList);
	}
}
