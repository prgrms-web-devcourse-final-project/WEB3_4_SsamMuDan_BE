package ssammudan.cotree.domain.review.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.review.dto.TechEducationReviewRequest;
import ssammudan.cotree.domain.review.dto.TechEducationReviewResponse;
import ssammudan.cotree.domain.review.service.TechEducationReviewService;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.global.response.SuccessCode;
import ssammudan.cotree.model.review.reviewtype.type.TechEducationReviewType;

/**
 * PackageName : ssammudan.cotree.domain.review.review.controller
 * FileName    : TechBookReviewController
 * Author      : loadingKKamo21
 * Date        : 25. 4. 1.
 * Description : TechBookReview 컨트롤러
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     loadingKKamo21       Initial creation
 */
@RestController
@RequestMapping("/api/v1/education/review")
@RequiredArgsConstructor
@Tag(name = "TechEducation Review Controller", description = "TechEducation Review API")
public class TechEducationReviewController {

	private final TechEducationReviewService techEducationReviewService;

	@PostMapping
	@Operation(summary = "TechEducation(TechTube or TechBook) 리뷰 생성", description = "TechEducationType(TECH_TUBE, TECH_BOOK)과 ID(TechTube PK or TechBook PK), 평점과 수강평으로 리뷰를 작성")
	@ApiResponse(responseCode = "200", description = "생성 성공")
	public BaseResponse<Void> createTechEducationReview(
		@RequestBody @Valid TechEducationReviewRequest.Create requestDto
	) {
		//TODO: 시큐리티 인증 객체를 활용한 작성자 정보(Member ID) 추가 필요
		Long id = techEducationReviewService.createTechEducationReview(UUID.randomUUID().toString(), requestDto);
		return BaseResponse.success(SuccessCode.TECH_EDUCATION_REVIEW_CREATE_SUCCESS);
	}

	@GetMapping
	@Operation(summary = "TechEduction(TechTube or TechBook) 리뷰 목록 조회", description = "TechEductionType(TECH_TUBE, TECH_BOOK)과 ID(TechTube PK or TechBook PK)를 사용해서 리뷰 목록 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	public BaseResponse<PageResponse<TechEducationReviewResponse.Detail>> getTechBookReviews(
		@RequestParam final TechEducationReviewType reviewType,
		@RequestParam final Long itemId,
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		PageResponse<TechEducationReviewResponse.Detail> responseDto = techEducationReviewService.findAllTechEducationReviews(
			new TechEducationReviewRequest.Read(reviewType, itemId), pageable
		);
		return BaseResponse.success(SuccessCode.TECH_EDUCATION_REVIEW_LIST_FIND_SUCCESS, responseDto);
	}

}
