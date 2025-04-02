package ssammudan.cotree.domain.resume.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : TackStackInfo
 * Author      : kwak
 * Date        : 2025. 3. 30.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 30.     kwak               Initial creation
 */
@Getter
@AllArgsConstructor
@Builder
public class TechStackInfo {
	String name;
	String imageUrl;
}
