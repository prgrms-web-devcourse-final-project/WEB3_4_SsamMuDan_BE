package ssammudan.cotree.domain.member.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public Member signUp(MemberSignupRequest signupRequest) {
		String redisEmailCode = redisTemplate.opsForValue()
			.get("signup:email:%s".formatted(signupRequest.email()));
		if (redisEmailCode == null) {
			throw new GlobalException(ErrorCode.MEMBER_SIGNUP_VERIFY_FAILED);
		}
		String redisPhoneNumberCode = redisTemplate.opsForValue()
			.get("signup:sms:%s".formatted(signupRequest.phoneNumber()));
		if (redisPhoneNumberCode == null) {
			throw new GlobalException(ErrorCode.MEMBER_SIGNUP_VERIFY_FAILED);
		}
		try {
			redisTemplate.delete("signup:email:%s".formatted(signupRequest.email()));
			redisTemplate.delete("signup:sms:%s".formatted(signupRequest.phoneNumber()));
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
	@Transactional(readOnly = true)
	public Member signIn(MemberSigninRequest request) {
		Member member = memberRepository.findByEmail(request.email())
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_UNAUTHORIZED));

		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new GlobalException(ErrorCode.MEMBER_UNAUTHORIZED);
		}
		return member;
	}

	@Override
	public Member updateMember(String memberId, MemberInfoRequest memberInfoRequest) {
		Member member = findById(memberId);
		return member.update(memberInfoRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public Member findById(String memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	@Transactional(readOnly = true)
	public MemberInfoResponse getMemberInfo(String id) {
		Member member = findById(id);
		return new MemberInfoResponse(member);
	}

	@Override
	public void updatePassword(String memberId, String password) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

		String redisEmailCode = redisTemplate.opsForValue()
			.get("signup:email:%s".formatted(member.getEmail()));
		if (redisEmailCode == null) {
			throw new GlobalException(ErrorCode.EMAIL_VERIFY_FAILED);
		}
		redisTemplate.delete("signup:email:%s".formatted(member.getEmail()));

		member.updatePassword(passwordEncoder.encode(password));
	}
}
