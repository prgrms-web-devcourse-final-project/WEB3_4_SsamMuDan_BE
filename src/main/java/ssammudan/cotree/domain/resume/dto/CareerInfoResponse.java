package ssammudan.cotree.domain.resume.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import ssammudan.cotree.domain.resume.dto.query.TechStackInfo;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.recruitment.career.career.entity.Career;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : CareerInfoResponse
 * Author      : kwak
 * Date        : 2025. 3. 30.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 30.     kwak               Initial creation
 */
@Builder(access = AccessLevel.PRIVATE)
public record CareerInfoResponse(
	LocalDate startDate,
	LocalDate endDate,
	String position,
	String companyName,
	String careerDescription,
	boolean isWorking,
	List<TechStackInfo> techStackInfos
) {
	public static CareerInfoResponse of(Career career, List<TechStack> techStacks) {
		return CareerInfoResponse.builder()
			.startDate(career.getStartDate())
			.endDate(career.getEndDate())
			.position(career.getPosition())
			.companyName(career.getCompany())
			.careerDescription(career.getDescription())
			.isWorking(career.isWorking())
			.techStackInfos(getTechStackInfos(techStacks))
			.build();
	}

	private static List<TechStackInfo> getTechStackInfos(List<TechStack> techStacks) {
		return techStacks.stream().map(techStack ->
			new TechStackInfo(techStack.getName(), techStack.getImageUrl())).toList();
	}
}
