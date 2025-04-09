package ssammudan.cotree.domain.project.membership.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import ssammudan.cotree.model.project.membership.type.ProjectMembershipStatus;

/**
 * PackageName : ssammudan.cotree.domain.membership.dto
 * FileName    : MemberShipResponse
 * Author      : sangxxjin
 * Date        : 2025. 4. 9.
 * Description : 프로젝트 신청자 목록 조회
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 9.     sangxxjin               Initial creation
 */
public record MembershipResponse(
	Long membershipId,
	ProjectMembershipStatus status,
	String username,
	String nickname,
	LocalDateTime appliedAt,
	LocalDateTime respondedAt
) {
	@QueryProjection
	public MembershipResponse {
	}
}

