package ssammudan.cotree.domain.category.dto;

import lombok.AccessLevel;
import lombok.Builder;

/**
 * PackageName : ssammudan.cotree.domain.category.dto
 * FileName    : PositionResponse
 * Author      : kwak
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     kwak               Initial creation
 */
@Builder(access = AccessLevel.PRIVATE)
public record PositionResponse(
	Long id,
	String name
) {
	public static PositionResponse of(Long id, String name) {
		return PositionResponse.builder()
			.id(id)
			.name(name)
			.build();
	}
}
