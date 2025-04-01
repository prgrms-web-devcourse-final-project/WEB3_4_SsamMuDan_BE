package ssammudan.cotree.domain.member.service;

import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.model.member.member.entity.Member;

public interface MemberService {
	Member signUp(MemberSignupRequest member);

	CustomUser signIn(MemberSigninRequest request);
}
