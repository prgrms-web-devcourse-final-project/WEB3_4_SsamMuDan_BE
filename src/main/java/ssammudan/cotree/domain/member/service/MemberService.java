package ssammudan.cotree.domain.member.service;

import ssammudan.cotree.domain.member.dto.info.MemberInfoRequest;
import ssammudan.cotree.domain.member.dto.info.MemberInfoResponse;
import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.model.member.member.entity.Member;

public interface MemberService {
	Member signUp(MemberSignupRequest member);

	Member signIn(MemberSigninRequest request);

	Member updateMember(String memberId, MemberInfoRequest request);

	Member findById(String memberId);

	MemberInfoResponse getMemberInfo(String id);

	void updatePassword(String memberId, String password);
}
