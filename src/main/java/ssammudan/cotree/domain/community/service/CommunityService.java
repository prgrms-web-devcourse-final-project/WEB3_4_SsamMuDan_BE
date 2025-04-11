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
 * 2025-04-11     Baekgwa               내가 좋아요 (관심)한, Community 목록 조회 기능 추가
 */
public interface CommunityService {
	CommunityResponse.BoardCreate createNewBoard(final CommunityRequest.CreateBoard createBoard, final String memberId);

	PageResponse<CommunityResponse.BoardListDetail> getBoardList(
			final Pageable pageable,
			final SearchBoardSort sort,
			final SearchBoardCategory category,
			final String keyword,
			final String memberId);

	CommunityResponse.BoardDetail getBoardDetail(final Long boardId, final String memberId);

	void modifyBoard(final Long boardId, final CommunityRequest.ModifyBoard modifyBoard, final String memberId);

	void deleteBoard(final Long boardId, final String memberId);

	PageResponse<CommunityResponse.BoardLikeListDetail> getBoardLikeList(
		final Pageable pageable,
		final String memberId
	);
}
