package ssammudan.cotree.domain.member.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.entity.MemberFactory;
import ssammudan.cotree.model.member.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	@Override
	public Member signUp(MemberSignupRequest signupRequest) {
		try {
			Member newMember = MemberFactory.createSignUpMember(signupRequest);
			return memberRepository.save(newMember);
		} catch (Exception e) {
			throw new GlobalException(ErrorCode.DUPLICATED_MEMBER);
		}
	}
}
