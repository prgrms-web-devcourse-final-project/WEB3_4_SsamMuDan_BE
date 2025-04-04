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
@Builder(access = AccessLevel.PRIVATE)
public record ProjectListResponse(
	Long id,
	String title,
	String description,
	String imageUrl,
	int viewCount,
	long likeCount,
	int recruitmentCount,
	LocalDate startDate,
	LocalDate endDate,
	List<String> techStacksImageUrl,
	String username,
	String userProfileImageUrl
) {
	public static ProjectListResponse from(Project project, List<String> techStacksImageUrl, long likeCount,
		Member member,
		int recruitmentCount) {
		return ProjectListResponse.builder()
			.id(project.getId())
			.title(project.getTitle())
			.description(project.getDescription().length() > 30 ? project.getDescription().substring(0, 30) + "..." :
				project.getDescription())
			.imageUrl(project.getProjectImageUrl())
			.viewCount(project.getViewCount())
			.likeCount(likeCount)
			.recruitmentCount(recruitmentCount)
			.startDate(project.getStartDate())
			.endDate(project.getEndDate())
			.techStacksImageUrl(techStacksImageUrl)
			.username(member.getUsername())
			.userProfileImageUrl(member.getProfileImageUrl())
			.build();
	}
}