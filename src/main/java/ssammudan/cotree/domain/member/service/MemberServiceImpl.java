package ssammudan.cotree.domain.member.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.domain.member.dto.info.MemberInfoRequest;
import ssammudan.cotree.domain.member.dto.info.MemberInfoResponse;
import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Member signUp(MemberSignupRequest signupRequest) {
		String redisEmail = (String)redisTemplate.opsForValue().get(signupRequest.email());
		if (redisEmail == null) {
			throw new GlobalException(ErrorCode.MEMBER_SIGNUP_VERIFY_FAILED);
		}
		// String redisPhoneNumber = (String)redisTemplate.opsForValue().get(signupRequest.phoneNumber());
		// if (redisPhoneNumber == null) {
		// 	throw new GlobalException(ErrorCode.MEMBER_SIGNUP_VERIFY_FAILED);
		// }
		try {

			Member newMember = Member.builder()
				.email(signupRequest.email())
				.password(passwordEncoder.encode(signupRequest.password()))
				.username(signupRequest.username())
				.nickname(signupRequest.nickname())
				.phoneNumber(signupRequest.phoneNumber())
				.build();
			return memberRepository.save(newMember);
		} catch (DataIntegrityViolationException e) {
			throw new GlobalException(ErrorCode.MEMBER_DUPLICATED);
		} catch (Exception e) {
			log.error("회원가입 중 오류 발생", e);
			throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
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
	@Transactional
	public Member updateMember(Member member, MemberInfoRequest memberInfoRequest) {
		return member.update(memberInfoRequest);
	}

	@Override
	public Member findById(String memberId) {
		return memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	public MemberInfoResponse getMemberInfo(String id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
		return new MemberInfoResponse(member);
	}
}
