package ssammudan.cotree.domain.project.membership.service;

import java.util.List;

import ssammudan.cotree.domain.project.membership.dto.MembershipResponse;

/**
 * PackageName : ssammudan.cotree.domain.project.membership.service
 * FileName    : MemberShipService
 * Author      : sangxxjin
 * Date        : 2025. 4. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 9.     sangxxjin               Initial creation
 */
public interface MembershipService {
	void applyForProject(Long projectId, String memberId);

	List<MembershipResponse> getMemberships(Long projectId, String memberId);
}
