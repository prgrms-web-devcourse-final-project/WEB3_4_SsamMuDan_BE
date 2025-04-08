package ssammudan.cotree.domain.category.dto;

import lombok.AccessLevel;
import lombok.Builder;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;

/**
 * PackageName : ssammudan.cotree.domain.category.dto
 * FileName    : CommunityCategoryResponse
 * Author      : Baekgwa
 * Date        : 2025-04-08
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-08     Baekgwa               Initial creation
 */
@Builder(access = AccessLevel.PRIVATE)
public record CommunityCategoryResponse(
	Long id,
	String name
) {
	public static CommunityCategoryResponse of(CommunityCategory category) {
		return CommunityCategoryResponse
			.builder()
			.id(category.getId())
			.name(category.getName())
			.build();
	}
}
