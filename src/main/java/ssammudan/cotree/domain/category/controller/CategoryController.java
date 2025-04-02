package ssammudan.cotree.domain.category.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
		return BaseResponse.success(SuccessCode.TECH_STACK_FIND_SUCCESS, categoryService.findAll());
	}
}
