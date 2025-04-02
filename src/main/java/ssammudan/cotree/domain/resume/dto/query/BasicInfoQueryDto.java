package ssammudan.cotree.domain.resume.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : ResumeDetailDto
 * Author      : kwak
 * Date        : 2025. 4. 1.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 1.     kwak               Initial creation
 */
@Getter
@AllArgsConstructor
public class BasicInfoQueryDto {
	private final String name;
	private final String imageUrl;
	private final Integer years;
	private final String email;
	private final String introduction;
	private final Integer viewCount;
}
