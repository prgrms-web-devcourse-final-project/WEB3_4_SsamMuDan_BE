package ssammudan.cotree.model.project.membership.type;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PackageName : ssammudan.cotree.model.project.membership.type
 * FileName    : ProjectMembershipStatus
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : ProjectMembershipStatus Enum
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 * 2025-04-09
 */
public enum ProjectMembershipStatus {
	@Schema(description = "신청 대기중")
	PENDING,
	@Schema(description = "신청 승인됨")
	JOINED,
	@Schema(description = "참가자가 프로젝트 도중 탈퇴")
	LEAVED,
	@Schema(description = "신청 거절됨")
	REJECTED
}
