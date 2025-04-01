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
		@NotBlank String educationLevel,
		@NotBlank @Size(max = 20) String title,
		@NotBlank @Size(max = 50) String description,
		@NotBlank @Size(max = 500) String introduction,
		@NotBlank String techBookUrl,
		@NotBlank String techBookPreviewUrl,
		@NotBlank String techBookThumbnailUrl,
		@NotNull @Min(1) Integer techBookPage,
		@NotNull @Min(1) Integer price
	) {
	}

}
