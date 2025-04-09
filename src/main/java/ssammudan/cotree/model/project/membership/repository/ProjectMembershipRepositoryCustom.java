package ssammudan.cotree.model.project.membership.repository;

import java.util.List;

import ssammudan.cotree.domain.project.membership.dto.MembershipResponse;

/**
 * PackageName : ssammudan.cotree.model.project.membership.repository
 * FileName    : ProjectMembershipRepositoryCustom
 * Author      : sangxxjin
 * Date        : 2025. 4. 9.
 * Description : ProjectMembershipRepositoryCustom
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 9.     sangxxjin               Initial creation
 */
public interface ProjectMembershipRepositoryCustom {
	List<MembershipResponse> findMembershipResponsesByProjectId(Long projectId);
}
