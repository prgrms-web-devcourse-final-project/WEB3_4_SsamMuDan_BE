package ssammudan.cotree.domain.resume.dto;

import lombok.AccessLevel;
import lombok.Builder;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : ResumeCreateResponse
 * Author      : kwak
 * Date        : 2025. 3. 30.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 30.     kwak               Initial creation
 */
@Builder(access = AccessLevel.PRIVATE)
public record ResumeCreateResponse(
	Long resumeId
) {
	public static ResumeCreateResponse from(Resume resume) {
		return ResumeCreateResponse.builder()
			.resumeId(resume.getId())
			.build();
	}
}
