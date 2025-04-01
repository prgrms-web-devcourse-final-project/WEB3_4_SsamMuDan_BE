package ssammudan.cotree.domain.education.techbook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.education.techbook.dto.TechBookResponse;
import ssammudan.cotree.domain.education.techbook.service.TechBookService;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.education
 * FileName    : TechBookController
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 */
@RestController
@RequestMapping("/api/v1/education/techbook")
@RequiredArgsConstructor
@Tag(name = "TechBook Controller", description = "TechBook API")
public class TechBookController {

	private final TechBookService techBookService;

	@Operation(summary = "TechBook 조회")
	@GetMapping("/{id}/info")
	public BaseResponse<TechBookResponse.Detail> getTechBookById(
		@PathVariable @Min(1) Long id
	) {
		TechBookResponse.Detail responseDto = techBookService.findTechBookById(id);
		return BaseResponse.success(SuccessCode.TECH_BOOK_READ_SUCCESS, responseDto);
	}

}
