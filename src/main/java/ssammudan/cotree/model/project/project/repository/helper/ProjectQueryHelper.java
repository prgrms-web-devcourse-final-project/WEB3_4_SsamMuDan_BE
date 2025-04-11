package ssammudan.cotree.model.project.project.repository.helper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.project.project.dto.ProjectLikeListResponse;
import ssammudan.cotree.domain.project.project.dto.ProjectListResponse;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.devposition.entity.QProjectDevPosition;
import ssammudan.cotree.model.project.project.entity.Project;
import ssammudan.cotree.model.project.project.entity.QProject;
import ssammudan.cotree.model.project.techstack.entity.QProjectTechStack;

/**
 * PackageName : ssammudan.cotree.model.project.project.helper
 * FileName    : ProjectQueryHelper
 * Author      : sangxxjin
 * Date        : 2025. 4. 7.
 * Description : query를 도와줄 helper클래스 적용
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 7.     sangxxjin               Initial creation
 * 2025. 4.11.     sangxxjin               좋아요 목록 조회
 */
@Component
@RequiredArgsConstructor
public class ProjectQueryHelper {

	private final JPAQueryFactory queryFactory;
	private static final String SORT_BY_LIKE = "like";

	public BooleanBuilder buildFilterConditions(List<Long> techStackIds, List<Long> devPositionIds) {
		BooleanBuilder where = new BooleanBuilder();

		if (techStackIds != null && !techStackIds.isEmpty()) {
			where.and(QProjectTechStack.projectTechStack.techStack.id.in(techStackIds));
		}
		if (devPositionIds != null && !devPositionIds.isEmpty()) {
			where.and(QProjectDevPosition.projectDevPosition.developmentPosition.id.in(devPositionIds));
		}
		return where;
	}

	public List<Long> sortFilteredProjects(List<Long> ids, String sort, Pageable pageable) {
		return queryFactory.select(QProject.project.id)
			.from(QProject.project)
			.leftJoin(QProject.project.likes, QLike.like)
			.where(QProject.project.id.in(ids))
			.groupBy(QProject.project.id)
			.orderBy(
				SORT_BY_LIKE.equalsIgnoreCase(sort)
					? QLike.like.id.count().desc()
					: QProject.project.createdAt.desc()
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	public List<ProjectListResponse> convertToDtoOrdered(List<Project> projects, List<Long> orderedIds) {
		return convertToOrdered(projects, orderedIds, this::toDto);
	}

	public List<ProjectLikeListResponse> convertToLikeDtoOrdered(List<Project> projects, List<Long> orderedIds) {
		return convertToOrdered(projects, orderedIds, this::toLikeDto);
	}

	private <T> List<T> convertToOrdered(List<Project> projects, List<Long> orderedIds,
		java.util.function.Function<Project, T> mapper) {
		Map<Long, T> dtoMap = projects.stream()
			.collect(Collectors.toMap(Project::getId, mapper));

		return orderedIds.stream()
			.map(dtoMap::get)
			.toList();
	}

	private ProjectListResponse toDto(Project p) {
		String shortenedDescription = p.getDescription().length() > 30
			? p.getDescription().substring(0, 30) + "..."
			: p.getDescription();

		return new ProjectListResponse(
			p.getId(),
			p.getTitle(),
			shortenedDescription,
			p.getProjectImageUrl(),
			p.getViewCount(),
			p.getLikes().size(),
			getRecruitmentCount(p),
			p.getIsOpen(),
			p.getStartDate(),
			p.getEndDate(),
			getTechStackImageUrls(p),
			p.getMember().getUsername(),
			p.getMember().getProfileImageUrl()
		);
	}

	private ProjectLikeListResponse toLikeDto(Project p) {
		return new ProjectLikeListResponse(
			p.getId(),
			p.getTitle(),
			getRecruitmentCount(p),
			p.getIsOpen(),
			p.getStartDate(),
			p.getEndDate(),
			getTechStackImageUrls(p)
		);
	}

	private List<String> getTechStackImageUrls(Project p) {
		return p.getProjectTechStacks().stream()
			.map(ts -> ts.getTechStack().getImageUrl())
			.toList();
	}

	private int getRecruitmentCount(Project p) {
		return p.getProjectDevPositions().stream()
			.mapToInt(ProjectDevPosition::getAmount)
			.sum();
	}

}
