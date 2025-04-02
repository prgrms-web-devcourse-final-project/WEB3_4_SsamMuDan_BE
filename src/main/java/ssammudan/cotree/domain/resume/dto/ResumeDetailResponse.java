package ssammudan.cotree.domain.resume.dto;

import java.util.List;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : ResumeCreateResponse
 * Author      : kwak
 * Date        : 2025. 3. 28.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 28.     kwak               Initial creation
 */
public record ResumeDetailResponse(
	BasicInfoResponse basicInfoResponse,
	List<CareerInfoResponse> careerInfos,
	List<PortfolioInfoResponse> portfolioInfos
) {
	public static ResumeDetailResponse create(BasicInfoResponse basicInfoResponse,
		List<CareerInfoResponse> careerInfos, List<PortfolioInfoResponse> portfolioInfos) {
		return new ResumeDetailResponse(basicInfoResponse, careerInfos, portfolioInfos);
	}
}
