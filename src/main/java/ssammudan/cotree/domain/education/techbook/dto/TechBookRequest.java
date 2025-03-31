package ssammudan.cotree.domain.education.techbook.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
public class TechBookRequest {

	public record Create(
		//TODO: EducationLevel 파라미터 PK(Long) vs 난이도 명칭(String)
		@NotBlank(message = "{NotBlank}") String educationLevel,
		@NotBlank(message = "{NotBlank}") @Size(max = 20, message = "{Size}") String title,
		@NotBlank(message = "{NotBlank}") @Size(max = 50, message = "{Size}") String description,
		@NotBlank(message = "{NotBlank}") @Size(max = 500, message = "{Size}") String introduction,
		@NotBlank(message = "{NotBlank}") String techBookUrl,
		@NotBlank(message = "{NotBlank}") String techBookPreviewUrl,
		@NotBlank(message = "{NotBlank}") String techBookThumbnailUrl,
		@NotNull(message = "{NotNull}") @Min(value = 1, message = "{Min}") Integer techBookPage,
		@NotNull(message = "{NotNull}") @Min(value = 1, message = "{Min}") Integer price
	) {
	}

}
