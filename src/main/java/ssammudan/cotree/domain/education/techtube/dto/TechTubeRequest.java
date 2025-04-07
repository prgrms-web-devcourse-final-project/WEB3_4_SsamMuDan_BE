package ssammudan.cotree.domain.education.techtube.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ssammudan.cotree.model.education.level.type.EducationLevelType;

/**
 * PackageName : ssammudan.cotree.domain.education.techtube.dto
 * FileName    : TechTubeRequest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : TechTube 요청 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 */
@Schema(description = "TechTube 요청 DTO")
public class TechTubeRequest {

	@Schema(description = "TechTube 생성 요청 DTO")
	public record Create(
		@NotNull
		@Schema(description = "학습 난이도 ENUM: BEGINNER(입문), INTERMEDIATE(초급), ADVANCED(중급)", example = "INTERMEDIATE")
		EducationLevelType educationLevel,
		@NotBlank @Size(max = 20)
		@Schema(description = "TechTube 제목", example = "Spring Boot")
		String title,
		@NotBlank @Size(max = 50)
		@Schema(description = "TechTube 설명", example = "스프링 부트를 1000% 활용하는 방법!!")
		String description,
		@NotBlank @Size(max = 500)
		@Schema(description = "TechTube 소개", example = "스프링 부트에 대한 수 많은 활용법을 담았습니다. ...")
		String introduction,
		@NotBlank
		@Schema(description = "TechTube 영상 URL", example = "https://cotree.ssammudan.com/techtube/SpringBoot.mp4")
		String techTubeUrl,
		@NotBlank
		@Schema(description = "TechTube 영상 미리보기 URL", example = "https://cotree.ssammudan.com/techtube/SpringBoot_preview.mp4")
		String techTubePreviewUrl,
		@NotBlank
		@Schema(description = "TechTube 썸네일 URL", example = "https://cotree.ssammudan.com/techtube/SpringBoot_thumbnail.png")
		String techTubeThumbnailUrl,
		@NotNull @Min(1)
		@Schema(description = "TechTube 동영상 길이(초)", example = "3600")
		Long techTubeDurationSeconds,
		@NotNull @Min(1)
		@Schema(description = "TechTube 가격", example = "10000")
		Integer price
	) {
	}

}
