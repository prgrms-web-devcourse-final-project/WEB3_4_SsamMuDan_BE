package ssammudan.cotree.domain.resume.dto;

import java.util.List;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : ResumeCreateRequest
 * Author      : kwak
 * Date        : 2025. 3. 28.
 * Description : 커리어와 프로젝트 정보는 여러 개가 담길 수 있습니다.
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 28.     kwak               Initial creation
 */
public record ResumeCreateRequest(
	BasicInfo basicInfo,
	List<CareerInfo> careerInfos,
	List<PortfolioInfo> portfolioInfos
) {
}
