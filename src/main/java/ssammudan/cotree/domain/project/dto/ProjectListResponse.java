package ssammudan.cotree.domain.project.dto;

import java.time.LocalDate;
import java.util.List;


import lombok.AccessLevel;
import lombok.Builder;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.project.project.entity.Project;

/**
 * PackageName : ssammudan.cotree.domain.project.dto
 * FileName    : HotProjectResponse
 * Author      : sangxxjin
 * Date        : 2025. 4. 3.
 * Description : 핫한 프로젝트 조회 응답값
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 3.     sangxxjin               Initial creation
 */

public record ProjectListResponse(
	Long id,
	String title,
	String description,
	String imageUrl,
	int viewCount,
	long likeCount,
	int recruitmentCount,
	boolean isOpen,
	LocalDate startDate,
	LocalDate endDate,
	List<String> techStacksImageUrl,
	String username,
	String userProfileImageUrl
) {
}