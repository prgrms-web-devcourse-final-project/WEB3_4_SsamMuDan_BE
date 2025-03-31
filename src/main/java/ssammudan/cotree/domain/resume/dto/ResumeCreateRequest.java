package ssammudan.cotree.domain.resume.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

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
@Builder
public record ResumeCreateRequest(
	BasicInfo basicInfo,
	@NotNull
	@Size(min = 1, max = 9, message = "커리어 정보는 1개 이상 10개 미만이어야 합니다")
	List<CareerInfo> careerInfos,
	@NotNull
	@Size(min = 1, max = 9, message = "포트폴리오 정보는 1개 이상 10개 미만이어야 합니다")
	List<PortfolioInfo> portfolioInfos
) {
}
