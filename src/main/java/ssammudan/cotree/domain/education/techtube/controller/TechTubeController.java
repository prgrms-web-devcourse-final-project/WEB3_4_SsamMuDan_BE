package ssammudan.cotree.domain.education.techtube.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeResponse;
import ssammudan.cotree.domain.education.techtube.service.TechTubeService;
import ssammudan.cotree.domain.education.type.SearchEducationSort;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.education.techtube.controller
 * FileName    : TechTubeController
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : TechTube 컨트롤러
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 * 25. 4. 7.     Baekgwa       		  techTube 목록 조회 refactor
 * 25. 4. 7.     Baekgwa       		  techTube 상세 조회 refactor
 */
@RestController
@RequestMapping("/api/v1/education/techtube")
@RequiredArgsConstructor
@Tag(name = "TechTube Controller", description = "TechTube API")
public class TechTubeController {

	private final TechTubeService techTubeService;

	@GetMapping("/{techTubeId}/info")
	@Operation(summary = "TechTube 상세 조회", description = "ID를 통해 특정 TechTube를 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	public BaseResponse<TechTubeResponse.TechTubeDetail> getTechTubeById(
		@PathVariable(name = "techTubeId") Long techTubeId,
		@AuthenticationPrincipal final CustomUser customUser
	) {
		String memberId = (customUser != null) ? customUser.getId() : null;
		TechTubeResponse.TechTubeDetail content = techTubeService.findTechTubeDetail(techTubeId, memberId);
		return BaseResponse.success(SuccessCode.TECH_TUBE_READ_SUCCESS, content);
	}

	@GetMapping
	@Operation(summary = "TechTube 목록 조회", description = "검색어를 사용해 TechTube 목록을 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	public BaseResponse<PageResponse<TechTubeResponse.ListInfo>> getTechTubes(
		@RequestParam(required = false) String keyword,
		@RequestParam(value = "page", required = false, defaultValue = "0") final int page,
		@RequestParam(value = "size", required = false, defaultValue = "16") final int size,
		@RequestParam(value = "sort", required = false, defaultValue = "LATEST") final SearchEducationSort sort,
		@RequestParam(value = "categoryId", required = false) final Long educationId,
		@AuthenticationPrincipal CustomUser customUser
	) {
		String memberId = (customUser != null) ? customUser.getId() : null;
		Pageable pageable = PageRequest.of(page, size);

		PageResponse<TechTubeResponse.ListInfo> responseDto =
			techTubeService.findAllTechTubes(keyword, sort, pageable, memberId, educationId);

		return BaseResponse.success(SuccessCode.TECH_TUBE_LIST_FIND_SUCCESS, responseDto);
	}
}
