package ssammudan.cotree.domain.category.dto;

import lombok.AccessLevel;
import lombok.Builder;
import ssammudan.cotree.model.education.category.entity.EducationCategory;

/**
 * PackageName : ssammudan.cotree.domain.category.dto
 * FileName    : EducationCategoryResponse
 * Author      : Baekgwa
 * Date        : 2025-04-07
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-07     Baekgwa               Initial creation
 */
@Builder(access = AccessLevel.PRIVATE)
public record EducationCategoryResponse(
	Long id,
	String name
) {
	public static EducationCategoryResponse of(EducationCategory educationCategory) {
		return EducationCategoryResponse
			.builder()
			.id(educationCategory.getId())
			.name(educationCategory.getName())
			.build();
	}
}

