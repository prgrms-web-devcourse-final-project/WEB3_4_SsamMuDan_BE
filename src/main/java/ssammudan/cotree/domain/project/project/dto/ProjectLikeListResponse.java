package ssammudan.cotree.domain.project.project.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * PackageName : ssammudan.cotree.domain.project.project.dto
 * FileName    : ProjectLikeListResponse
 * Author      : sangxxjin
 * Date        : 2025. 4. 11.
 * Description : 프로젝트 좋아요 목록
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 11.     sangxxjin               Initial creation
 */
public record ProjectLikeListResponse(
	Long id,
	String title,
	int recruitmentCount,
	boolean isOpen,
	LocalDate startDate,
	LocalDate endDate,
	List<String> techStacksImageUrl
) {
}
