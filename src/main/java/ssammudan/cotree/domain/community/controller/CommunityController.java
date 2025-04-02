package ssammudan.cotree.domain.community.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.community.dto.CommunityRequest;
import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.domain.community.service.CommunityService;
import ssammudan.cotree.domain.community.type.SearchBoardCategory;
import ssammudan.cotree.domain.community.type.SearchBoardSort;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.community.controller
 * FileName    : CommunityController
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : Community Controller Layer
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28     Baekgwa               Initial creation
 */
@RestController
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
@Tag(name = "Community Controller", description = "커뮤니티 API")
public class CommunityController {

	private final CommunityService communityService;

	@PostMapping("/board")
	@Operation(summary = "커뮤니티 글 작성")
	public BaseResponse<Void> createNewBoard(
			@Valid @RequestBody CommunityRequest.CreateBoard createBoard,
			@AuthenticationPrincipal CustomUser customUser
	) {
		String memberId = customUser.getId();

		communityService.createNewBoard(createBoard, memberId);
		return BaseResponse.success(SuccessCode.COMMUNITY_BOARD_CREATE_SUCCESS);
	}

	@GetMapping("/board")
	@Operation(summary = "커뮤니티 글 목록 조회")
	public BaseResponse<PageResponse<CommunityResponse.BoardListDetail>> getBoardList(
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "5", required = false) int size,
			@RequestParam(value = "sort", defaultValue = "LATEST", required = false) SearchBoardSort sort,
			@RequestParam(value = "category", defaultValue = "TOTAL", required = false) SearchBoardCategory category,
			@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
			@AuthenticationPrincipal CustomUser customUser
	) {
		String memberId = (customUser != null) ? customUser.getId() : null;
		Pageable pageable = PageRequest.of(page, size);
		PageResponse<CommunityResponse.BoardListDetail> boardList =
				communityService.getBoardList(pageable, sort, category, keyword, memberId);

		return BaseResponse.success(SuccessCode.COMMUNITY_BOARD_SEARCH_SUCCESS, boardList);
	}

	@GetMapping("/board/{boardId}")
	@Operation(summary = "커뮤니티 글 상세 조회")
	public BaseResponse<CommunityResponse.BoardDetail> getBoardDetail(
			@PathVariable(value = "boardId") Long boardId,
			@AuthenticationPrincipal CustomUser customUser
	) {
		String memberId = (customUser != null) ? customUser.getId() : null;
		CommunityResponse.BoardDetail boardDetail = communityService.getBoardDetail(boardId, memberId);
		return BaseResponse.success(SuccessCode.COMMUNITY_BOARD_DETAIL_SEARCH_SUCCESS, boardDetail);
	}
}
