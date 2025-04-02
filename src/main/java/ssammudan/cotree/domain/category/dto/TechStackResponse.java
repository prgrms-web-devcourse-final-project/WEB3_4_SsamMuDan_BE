package ssammudan.cotree.domain.category.dto;

import lombok.AccessLevel;
import lombok.Builder;

/**
 * PackageName : ssammudan.cotree.domain.category.dto
 * FileName    : TechStackResponse
 * Author      : kwak
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     kwak               Initial creation
 */
@Builder(access = AccessLevel.PRIVATE)
public record TechStackResponse(
	Long id,
	String name,
	String imageUrl
) {
	public static TechStackResponse of(Long id, String name, String imageUrl) {
		return TechStackResponse.builder()
			.id(id)
			.name(name)
			.imageUrl(imageUrl)
			.build();
	}
}
