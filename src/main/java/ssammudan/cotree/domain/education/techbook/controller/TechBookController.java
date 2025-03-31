package ssammudan.cotree.domain.education.techbook.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping(value = "/api/v1/education/techbook",
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TechBookController {

	private final TechBookService techBookService;

	@GetMapping("/{id}/info")
	public BaseResponse<TechBookResponse.Detail> getTechBookById(
		@PathVariable @Min(value = 1, message = "{Min}") Long id
	) {
		TechBookResponse.Detail responseDto = techBookService.findTechBookById(id);
		return BaseResponse.success(SuccessCode.TECH_BOOK_READ_SUCCESS, responseDto);
	}

}
