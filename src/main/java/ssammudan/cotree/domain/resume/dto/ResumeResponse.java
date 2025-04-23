package ssammudan.cotree.domain.resume.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import ssammudan.cotree.model.recruitment.resume.resume.repository.dto.ResumeBasicInfoDto;

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
	Long resumeId,
	String profileImage,
	boolean isOpen,
	List<String> positions,
	List<Long> tackStacksId,
	Integer year,
	String introduction,
	Integer viewCount,
	LocalDateTime createAt
) {
	public static ResumeResponse of(
		ResumeBasicInfoDto resumeBasicInfoDto,
		Map<Long, List<String>> positionsMap,
		Map<Long, List<Long>> techStacksMap
	) {
		Long resumeId = resumeBasicInfoDto.id();
		return ResumeResponse.builder()
			.resumeId(resumeId)
			.profileImage(resumeBasicInfoDto.profileImage())
			.isOpen(resumeBasicInfoDto.isOpen())
			.positions(positionsMap.getOrDefault(resumeId, Collections.emptyList()))
			.tackStacksId(techStacksMap.getOrDefault(resumeId, Collections.emptyList()))
			.year(resumeBasicInfoDto.years())
			.introduction(resumeBasicInfoDto.introduction())
			.viewCount(resumeBasicInfoDto.viewCount())
			.createAt(resumeBasicInfoDto.createdAt())
			.build();
	}
}
