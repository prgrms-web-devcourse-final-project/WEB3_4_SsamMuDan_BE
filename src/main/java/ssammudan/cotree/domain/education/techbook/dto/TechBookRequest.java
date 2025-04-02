package ssammudan.cotree.domain.education.techbook.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ssammudan.cotree.model.education.level.type.EducationLevelType;

/**
 * PackageName : ssammudan.cotree.domain.education.dto
 * FileName    : TechBookRequest
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBook 요청 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 */
@Schema(description = "TechBook 요청 DTO")
public class TechBookRequest {

	@Schema(description = "TechBook 생성 요청 DTO")
	public record Create(
		@NotNull
		@Schema(description = "학습 난이도 ENUM: BEGINNER(입문), INTERMEDIATE(초급), ADVANCED(중급)", example = "INTERMEDIATE")
		EducationLevelType educationLevel,
		@NotBlank @Size(max = 20)
		@Schema(description = "TechBook 제목", example = "Spring Boot")
		String title,
		@NotBlank @Size(max = 50)
		@Schema(description = "TechBook 설명", example = "스프링 부트를 1000% 활용하는 방법!!")
		String description,
		@NotBlank @Size(max = 500)
		@Schema(description = "TechBook 소개", example = "스프링 부트에 대한 수 많은 활용법을 담았습니다. ...")
		String introduction,
		@NotBlank
		@Schema(description = "TechBook PDF URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot.pdf")
		String techBookUrl,
		@NotBlank
		@Schema(description = "TechBook PDF 미리보기 URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot_preview.pdf")
		String techBookPreviewUrl,
		@NotBlank
		@Schema(description = "TechBook 썸네일 URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot_thumbnail.png")
		String techBookThumbnailUrl,
		@NotNull @Min(1)
		@Schema(description = "TechBook 페이지 수", example = "100")
		Integer techBookPage,
		@NotNull @Min(1)
		@Schema(description = "TechBook 가격", example = "10000")
		Integer price
	) {
	}

}
