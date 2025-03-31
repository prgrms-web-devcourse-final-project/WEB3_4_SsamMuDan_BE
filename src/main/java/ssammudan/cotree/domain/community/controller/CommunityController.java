package ssammudan.cotree.domain.community.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.community.dto.CommunityRequest;
import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.domain.community.service.CommunityService;
import ssammudan.cotree.global.response.BaseResponse;
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
	@SecurityRequirement(name = "bearerAuth")
	public BaseResponse<Void> createNewBoard(@Valid @RequestBody CommunityRequest.CreateBoard createBoard) {
		//todo : 로그인 회원 정보 입력 받아야됨, 현재 임시로 진행
		String dummyUserId = "1";

		communityService.createNewBoard(createBoard, dummyUserId);
		return BaseResponse.success(SuccessCode.COMMUNITY_BOARD_CREATE_SUCCESS);
	}

	@GetMapping("/board")
	@Operation(summary = "커뮤니티 글 목록 조회")
	public BaseResponse<CommunityResponse.BoardList> getBoardList(
	) {
	}
}
