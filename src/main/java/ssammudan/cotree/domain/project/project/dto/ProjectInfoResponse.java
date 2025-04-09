package ssammudan.cotree.domain.project.project.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.project.project.entity.Project;

/**
 * PackageName : ssammudan.cotree.domain.project.dto
 * FileName    : ProjectInfoResponse
 * Author      : sangxxjin
 * Date        : 2025. 4. 3.
 * Description : 프로젝트 상세조회 응답값
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 3.     sangxxjin               Initial creation
 */
@Builder(access = AccessLevel.PRIVATE)
public record ProjectInfoResponse(
	String title,
	String description,
	String imageUrl,
	String creatorName,
	LocalDate startDate,
	LocalDate endDate,
	String contact,
	List<ProjectDevPositionResponse> devPositionsInfo,
	List<String> techStacks,
	String partnerType,
	int viewCount,
	long likeCount,
	boolean isOpen,
	boolean isLiked,
	boolean isParticipant,
	boolean isOwner
) {
	public static ProjectInfoResponse of(
		Project project,
		Member creator,
		long likeCount,
		List<ProjectDevPositionResponse> devPositionsInfo,
		List<String> techStacks,
		boolean isLiked,
		boolean isParticipant,
		boolean isOwner
	) {
		return ProjectInfoResponse.builder()
			.title(project.getTitle())
			.description(project.getDescription())
			.imageUrl(project.getProjectImageUrl())
			.creatorName(creator.getUsername())
			.startDate(project.getStartDate())
			.endDate(project.getEndDate())
			.contact(project.getContact())
			.devPositionsInfo(devPositionsInfo)
			.techStacks(techStacks)
			.partnerType(project.getPartnerType())
			.viewCount(project.getViewCount())
			.likeCount(likeCount)
			.isOpen(project.getIsOpen())
			.isLiked(isLiked)
			.isParticipant(isParticipant)
			.isOwner(isOwner)
			.build();
	}
}
