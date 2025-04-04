package ssammudan.cotree.domain.resume.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : ResumeResponse
 * Author      : kwak
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     kwak               Initial creation
 */
@Builder
public record ResumeResponse(
	String profileImage,
	boolean isOpen,
	List<String> positions,
	Integer year,
	String introduction,
	LocalDateTime createAt
) {
}
