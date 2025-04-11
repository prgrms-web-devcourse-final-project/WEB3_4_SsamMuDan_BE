package ssammudan.cotree.domain.community.service;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotNull;
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
	CommunityResponse.BoardCreate createNewBoard(
		@NonNull final CommunityRequest.CreateBoard createBoard,
		@NonNull final String memberId
	);

	PageResponse<CommunityResponse.BoardListDetail> getBoardList(
		@NonNull final Pageable pageable,
		@NonNull final SearchBoardSort sort,
		@NonNull final SearchBoardCategory category,
		@NonNull final String keyword,
		@Nullable final String memberId
	);

	CommunityResponse.BoardDetail getBoardDetail(
		@NotNull final Long boardId,
		@NonNull final String memberId
	);

	void modifyBoard(
		@NonNull final Long boardId,
		@NonNull final CommunityRequest.ModifyBoard modifyBoard,
		@NonNull final String memberId
	);

	void deleteBoard(
		@NonNull final Long boardId,
		@NonNull final String memberId
	);

	PageResponse<CommunityResponse.BoardLikeListDetail> getBoardLikeList(
		@NonNull final Pageable pageable,
		@NonNull final String memberId
	);
}
