package ssammudan.cotree.model.member.member.entity;

import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;

/**
 * PackageName : ssammudan.cotree.model.member.member.entity
 * FileName    : MemberFactory
 * Author      : hc
 * Date        : 25. 3. 30.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 30.     hc               Initial creation
 */
public class MemberFactory {
	public static Member createSignUpMember(MemberSignupRequest request) {
		return Member.builder()
			.email(request.email())
			.password(request.password())
			.username(request.username())
			.nickname(request.nickname())
			.phoneNumber(request.phoneNumber()
			).build();
	}
}
