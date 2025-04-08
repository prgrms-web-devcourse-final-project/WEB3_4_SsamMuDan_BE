package ssammudan.cotree.domain.category.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.category.dto.CommunityCategoryResponse;
import ssammudan.cotree.domain.category.dto.EducationCategoryResponse;
import ssammudan.cotree.domain.category.dto.PositionResponse;
import ssammudan.cotree.domain.category.dto.TechStackResponse;
import ssammudan.cotree.domain.category.service.CategoryService;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.category.controller
 * FileName    : CategoryController
 * Author      : kwak
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     kwak               Initial creation
 * 2025. 4. 7.     Baekgwa            교육 카테고리 조회 추가
 * 2025. 4. 8.     Baekgwa            커뮤니티 글 카테고리 조회 추가
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
@Tag(name = "CategoryController", description = "카테고리 컨트롤러")
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping("/skill")
	@Operation(summary = "기술 스택 전체 조회", description = "기술 스택 전체를 조회합니다.")
	public BaseResponse<List<TechStackResponse>> findSkills() {
		return BaseResponse.success(SuccessCode.TECH_STACK_FIND_SUCCESS, categoryService.findSkills());
	}

	@GetMapping("/position")
	@Operation(summary = "개발 직무 전체 조회", description = "개발 직무 전체를 조회합니다.")
	public BaseResponse<List<PositionResponse>> findPositions() {
		return BaseResponse.success(SuccessCode.POSITION_FIND_SUCCESS, categoryService.findPositions());
	}

	@GetMapping("/education")
	@Operation(summary = "교육 카테고리 전체 조회", description = "교육 카테고리를 전체 조회합니다.")
	public BaseResponse<List<EducationCategoryResponse>> findEducationCategoryList() {
		return BaseResponse.success(SuccessCode.EDUCATION_CATEGORY_FIND_SUCCESS,
			categoryService.findEducationCategoryList());
	}

	@GetMapping("/community")
	@Operation(summary = "커뮤니티 글 카테고리 전체 조회", description = "커뮤니티의 글의 전체 카테고리를 조회 합니다.")
	public BaseResponse<List<CommunityCategoryResponse>> findCommunityCategoryList() {
		return BaseResponse.success(SuccessCode.COMMUNITY_CATEGORY_FIND_SUCCESS,
			categoryService.findCommunityCategoryList());
	}
}
