package ssammudan.cotree.domain.community.service;

import ssammudan.cotree.domain.community.dto.CommunityRequest;

/**
 * PackageName : ssammudan.cotree.domain.community.service
 * FileName    : CommunityService
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : Community domain service layer interface
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28     Baekgwa               Initial creation
 */
public interface CommunityService {
	void createNewBoard(CommunityRequest.CreateBoard createBoard, String userId);
}
