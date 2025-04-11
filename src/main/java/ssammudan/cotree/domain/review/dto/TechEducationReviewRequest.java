package ssammudan.cotree.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ssammudan.cotree.model.education.type.EducationType;

/**
 * PackageName : ssammudan.cotree.domain.review.review.dto
 * FileName    : TechEducationReviewRequest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 1.
 * Description : TechEducationReview 요청 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     loadingKKamo21       Initial creation
 */
@Schema(description = "TechEducation 리뷰 요청 DTO")
public class TechEducationReviewRequest {

	@Schema(description = "TechEducation(TechTube or TechBook) 리뷰 생성 요청 DTO")
	public record ReviewCreate(
		@NotNull
		@Schema(description = "TechEducation 타입: TECH_BOOK, TECH_TUBE", example = "TECH_BOOK")
		EducationType techEducationType,
		@NotNull @Min(1) @Max(5)
		@Schema(description = "TechEducation 리뷰 평점", example = "5")
		Integer rating,
		@NotBlank @Size(max = 500)
		@Schema(description = "TechEducation 리뷰 내용", example = "스프링 부트의 다양한 기능을 활용하는 방법을 배울 수 있었습니다.")
		String content,
		@NotNull @Min(1)
		@Schema(description = "TechEducation 리뷰 대상 ID: TechBook ID, TechTube ID", example = "1")
		Long itemId
	) {
	}

	@Schema(description = "TechEducation(TechTube or TechBook) 리뷰 읽기 요청 DTO")
	public record ReviewRead(
		@NotNull
		@Schema(description = "TechEducation 타입: TECH_BOOK, TECH_TUBE", example = "TECH_BOOK")
		EducationType techEducationType,
		@NotNull @Min(1)
		@Schema(description = "TechEducation 리뷰 대상 ID: TechBook ID, TechTube ID", example = "1")
		Long itemId
	) {
	}

}
