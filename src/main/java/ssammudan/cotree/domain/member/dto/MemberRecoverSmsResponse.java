package ssammudan.cotree.domain.member.dto;

import lombok.AccessLevel;
import lombok.Builder;

/**
 * PackageName : ssammudan.cotree.domain.member.dto
 * FileName    : MemberRecoverSmsResponse
 * Author      : kwak
 * Date        : 2025. 4. 7.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 7.     kwak               Initial creation
 */
@Builder(access = AccessLevel.PRIVATE)
public record MemberRecoverSmsResponse(
	String email
) {
	public static MemberRecoverSmsResponse from(String email) {
		return MemberRecoverSmsResponse.builder()
			.email(email)
			.build();
	}
}
