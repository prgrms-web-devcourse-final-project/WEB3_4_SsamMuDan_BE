package ssammudan.cotree.domain.project.project.dto;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

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
 * 2025.04.29.	   sangxxjin               record->class, QueryProjection
 */

@Getter
public class ProjectListResponse {

	private final Long id;
	private final String title;
	private final String description;
	private final String imageUrl;
	private final int viewCount;
	private final long likeCount;
	private final int recruitmentCount;
	private final boolean isOpen;
	private final LocalDate startDate;
	private final LocalDate endDate;
	private final List<String> techStacksImageUrl;
	private final String username;
	private final String userProfileImageUrl;

	@Builder
	@QueryProjection
	public ProjectListResponse(Long id,
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
		String userProfileImageUrl) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.imageUrl = imageUrl;
		this.viewCount = viewCount;
		this.likeCount = likeCount;
		this.recruitmentCount = recruitmentCount;
		this.isOpen = isOpen;
		this.startDate = startDate;
		this.endDate = endDate;
		this.techStacksImageUrl = techStacksImageUrl;
		this.username = username;
		this.userProfileImageUrl = userProfileImageUrl;
	}

	public ProjectListResponse withTechStacks(List<String> techStacksImageUrl) {
		return ProjectListResponse.builder()
			.id(this.id)
			.title(this.title)
			.description(this.description)
			.imageUrl(this.imageUrl)
			.viewCount(this.viewCount)
			.likeCount(this.likeCount)
			.recruitmentCount(this.recruitmentCount)
			.isOpen(this.isOpen)
			.startDate(this.startDate)
			.endDate(this.endDate)
			.techStacksImageUrl(techStacksImageUrl)
			.username(this.username)
			.userProfileImageUrl(this.userProfileImageUrl)
			.build();
	}
}