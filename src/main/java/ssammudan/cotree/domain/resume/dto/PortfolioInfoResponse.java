package ssammudan.cotree.domain.resume.dto;

import java.time.LocalDate;
import java.util.List;

import ssammudan.cotree.domain.resume.dto.query.TechStackInfo;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.recruitment.portfolio.portfolio.entity.Portfolio;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : PortfolioInfoResponse
 * Author      : kwak
 * Date        : 2025. 4. 1.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 1.     kwak               Initial creation
 */
public record PortfolioInfoResponse(
	LocalDate startDate,
	LocalDate endDate,
	String projectName,
	String description,
	List<TechStackInfo> techStackInfos
) {
	public static PortfolioInfoResponse of(Portfolio portfolio, List<TechStack> techStacks) {
		return new PortfolioInfoResponse(
			portfolio.getStartDate(),
			portfolio.getEndDate(),
			portfolio.getName(),
			portfolio.getDescription(),
			getTechStackInfos(techStacks));
	}

	private static List<TechStackInfo> getTechStackInfos(List<TechStack> techStacks) {
		return techStacks.stream().map(techStack ->
			new TechStackInfo(techStack.getName(), techStack.getImageUrl())).toList();
	}
}
