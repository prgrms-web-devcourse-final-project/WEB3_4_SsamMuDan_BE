package ssammudan.cotree.domain.member.service;

import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.model.member.member.entity.Member;

public interface MemberService {
	Member signUp(MemberSignupRequest member);

	Member signIn(MemberSigninRequest request);
}
