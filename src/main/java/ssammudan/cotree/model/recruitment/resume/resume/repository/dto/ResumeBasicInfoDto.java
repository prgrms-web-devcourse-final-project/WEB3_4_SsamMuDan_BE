package ssammudan.cotree.model.recruitment.resume.resume.repository.dto;

import java.time.LocalDateTime;

/**
 * PackageName : ssammudan.cotree.model.recruitment.resume.resume.repository.dto
 * FileName    : ResumeBasicInfoDto
 * Author      : kwak
 * Date        : 2025. 4. 15.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 15.     kwak               Initial creation
 */
public record ResumeBasicInfoDto(
	Long id,
	String profileImage,
	Boolean isOpen,
	Integer years,
	String introduction,
	Integer viewCount,
	LocalDateTime createdAt
) {
}
