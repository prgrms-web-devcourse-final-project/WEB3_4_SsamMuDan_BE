package ssammudan.cotree.domain.community.service;

import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.community.dto.CommunityRequest;
import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.domain.community.type.SearchBoardCategory;
import ssammudan.cotree.domain.community.type.SearchBoardSort;
import ssammudan.cotree.global.response.PageResponse;

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
	void createNewBoard(final CommunityRequest.CreateBoard createBoard, final String memberId);

	PageResponse<CommunityResponse.BoardListDetail> getBoardList(
			final Pageable pageable,
			final SearchBoardSort sort,
			final SearchBoardCategory category,
			final String keyword,
			final String memberId);

	CommunityResponse.BoardDetail getBoardDetail(final Long boardId, final String memberId);

	void checkModifyAuthority(final String memberId, final Long boardId);

	void modifyBoard(final Long boardId, final CommunityRequest.ModifyBoard modifyBoard);
}
