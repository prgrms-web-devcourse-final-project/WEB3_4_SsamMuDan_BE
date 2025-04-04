package ssammudan.cotree.domain.education.techbook.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.education.techbook.dto.TechBookResponse;
import ssammudan.cotree.domain.education.techbook.service.TechBookService;
import ssammudan.cotree.domain.education.type.SearchEducationSort;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.education
 * FileName    : TechBookController
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBookController 컨트롤러
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 * 25. 4. 1.     loadingKKamo21       GET: /api/v1/education/techbook 추가
 */
@RestController
@RequestMapping("/api/v1/education/techbook")
@RequiredArgsConstructor
@Tag(name = "TechBook Controller", description = "TechBook API")
public class TechBookController {

	private final TechBookService techBookService;

	@GetMapping("/{id}/info")
	@Operation(summary = "TechBook 상세 조회", description = "ID를 통해 특정 TechBook을 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	public BaseResponse<TechBookResponse.Detail> getTechBookById(@PathVariable @Min(1) Long id) {
		TechBookResponse.Detail responseDto = techBookService.findTechBookById(id);
		return BaseResponse.success(SuccessCode.TECH_BOOK_READ_SUCCESS, responseDto);
	}

	@GetMapping
	@Operation(summary = "TechBook 목록 조회", description = "검색어를 사용해 TechBook 목록을 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	public BaseResponse<PageResponse<TechBookResponse.ListInfo>> getTechBooks(
		@RequestParam(required = false) String keyword,
		@RequestParam(value = "page", required = false, defaultValue = "0") final int page,
		@RequestParam(value = "size", required = false, defaultValue = "16") final int size,
		@RequestParam(value = "sort", required = false, defaultValue = "LATEST") final SearchEducationSort sort
	) {
		PageResponse<TechBookResponse.ListInfo> responseDto = techBookService.findAllTechBooks(
			keyword, PageRequest.of(page, size, Sort.Direction.DESC, sort.getValue())
		);
		return BaseResponse.success(SuccessCode.TECH_BOOK_LIST_FIND_SUCCESS, responseDto);
	}

}
