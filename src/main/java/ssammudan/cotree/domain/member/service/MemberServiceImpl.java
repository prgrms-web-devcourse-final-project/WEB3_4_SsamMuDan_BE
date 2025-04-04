package ssammudan.cotree.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.member.dto.info.MemberInfoResponse;
import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member signUp(MemberSignupRequest signupRequest) {
		try {
			Member newMember = Member.builder()
				.email(signupRequest.email())
				.password(passwordEncoder.encode(signupRequest.password()))
				.username(signupRequest.name())
				.nickname(signupRequest.nickname())
				.phoneNumber(signupRequest.phoneNumber())
				.build();
			return memberRepository.save(newMember);
		} catch (Exception e) {
			throw new GlobalException(ErrorCode.MEMBER_DUPLICATED);
		}
	}

	@Override
	public Member signIn(MemberSigninRequest request) {
		Member member = memberRepository.findByEmail(request.email())
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_UNAUTHORIZED));

		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new GlobalException(ErrorCode.MEMBER_UNAUTHORIZED);
		}

		return member;
	}
	@Override
	public MemberInfoResponse getMemberInfo(String id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
		return new MemberInfoResponse(member);
	}
}
