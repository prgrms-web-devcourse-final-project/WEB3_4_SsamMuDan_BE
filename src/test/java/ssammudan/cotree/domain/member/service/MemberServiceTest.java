package ssammudan.cotree.domain.member.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;
import ssammudan.cotree.domain.member.dto.info.MemberInfoRequest;
import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;

/**
 * PackageName : ssammudan.cotree.domain.member.service
 * FileName    : MemberServiceTest
 * Author      : hc
 * Date        : 25. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     hc               Initial creation
 */
@Transactional
class MemberServiceTest extends SpringBootTestSupporter {
	@Autowired
	MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("회원가입")
	void signUp() {
		// given
		MemberSignupRequest testMemberSignupRequest = new MemberSignupRequest(
			"testEmail123@mail.com",
			"password123",
			"testName",
			"testNickName",
			"01012345678"
		);

		// when
		Member member = memberService.signUp(testMemberSignupRequest);

		// then
		Member savedMember = memberRepository.findByUsername("testName").get();

		assertThat(member.getId()).isEqualTo(savedMember.getId());
		assertThat(member.getUsername()).isEqualTo(savedMember.getUsername());
		assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
		assertThat(member.getNickname()).isEqualTo(savedMember.getNickname());
		assertThat(member.getPhoneNumber()).isEqualTo(savedMember.getPhoneNumber());
	}

	@Test
	@DisplayName("로그인 Success")
	void signIn() {
		// given
		MemberSignupRequest testMemberSignupRequest = new MemberSignupRequest(
			"testEmail123@mail.com",
			"password123",
			"testName",
			"testNickName",
			"01012345678"
		);
		Member signUpMember = memberService.signUp(testMemberSignupRequest);

		MemberSigninRequest testMemberSigninRequest = new MemberSigninRequest(
			"testEmail123@mail.com",
			"password123"
		);

		// when
		Member signInMember = memberService.signIn(testMemberSigninRequest);

		// then
		Member savedMember = memberRepository.findByUsername("testName").get();

		assertThat(signInMember.getId()).isEqualTo(savedMember.getId());
		assertThat(signInMember.getUsername()).isEqualTo(savedMember.getUsername());
		assertThat(signInMember.getEmail()).isEqualTo(savedMember.getEmail());
		assertThat(signInMember.getNickname()).isEqualTo(savedMember.getNickname());
		assertThat(signInMember.getPhoneNumber()).isEqualTo(savedMember.getPhoneNumber());
	}

	@Test
	@DisplayName("회원 정보 수정")
	@Transactional
	void updateMember() {
		// given
		MemberSignupRequest testMemberSignupRequest = new MemberSignupRequest(
			"testEmail123@mail.com",
			"password123",
			"testName",
			"testNickName",
			"01012345678"
		);
		Member member = memberService.signUp(testMemberSignupRequest);

		MemberInfoRequest testMemberInfoRequest = new MemberInfoRequest(
			"name",
			"nickname",
			"profileImageUrl"
		);

		// when
		Member updatedMember = memberService.updateMember(member, testMemberInfoRequest);

		// then
		assertThat(updatedMember.getNickname()).isEqualTo(testMemberInfoRequest.username());
		assertThat(updatedMember.getPhoneNumber()).isEqualTo(testMemberInfoRequest.nickname());
		assertThat(updatedMember.getProfileImageUrl()).isEqualTo(testMemberInfoRequest.profileImageUrl());
	}
}
