package ssammudan.cotree.domain.member.dto;

import lombok.Builder;

/**
 * PackageName : ssammudan.cotree.domain.member.dto
 * FileName    : MemberOrderResponse
 * Author      : kwak
 * Date        : 2025. 4. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 8.     kwak               Initial creation
 */
@Builder
public record MemberOrderResponse(
	Long productId,
	String productType,
	String thumbnail,
	String title,
	String author
) {
}
