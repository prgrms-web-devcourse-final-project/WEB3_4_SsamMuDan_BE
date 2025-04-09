package ssammudan.cotree.domain.member.dto.info;

import java.time.LocalDateTime;

import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.type.MemberRole;

/**
 * PackageName : ssammudan.cotree.domain.member.dto.info
 * FileName    : MemberInfo
 * Author      : hc
 * Date        : 25. 4. 4.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     hc               Initial creation
 */
public record MemberInfoResponse(
	String email,
	String username,
	String nickname,
	MemberRole role,
	LocalDateTime createdAt
) {
	public MemberInfoResponse(Member member) {
		this(member.getEmail(), member.getUsername(), member.getNickname(), member.getRole(), member.getCreatedAt());
	}
}
