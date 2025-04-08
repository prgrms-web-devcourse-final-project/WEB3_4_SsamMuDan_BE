package ssammudan.cotree.domain.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PackageName : ssammudan.cotree.domain.member.type
 * FileName    : OrderProductCategoryType
 * Author      : kwak
 * Date        : 2025. 4. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 8.     kwak               Initial creation
 */
@Getter
@AllArgsConstructor
public enum OrderProductCategoryType {

	TECH_TUBE(1L),
	TECH_BOOK(2L);

	private final Long id;
}
