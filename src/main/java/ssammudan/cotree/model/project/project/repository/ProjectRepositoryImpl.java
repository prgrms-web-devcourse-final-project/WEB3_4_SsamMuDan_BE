package ssammudan.cotree.model.project.project.repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import ssammudan.cotree.domain.project.dto.ProjectListResponse;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.devposition.entity.QProjectDevPosition;
import ssammudan.cotree.model.project.membership.entity.QProjectMembership;
import ssammudan.cotree.model.project.project.entity.Project;
import ssammudan.cotree.model.project.project.entity.QProject;
import ssammudan.cotree.model.project.techstack.entity.QProjectTechStack;

/**
 * PackageName : ssammudan.cotree.model.project.project.repository
 * FileName    : ProjectRepositoryImpl
 * Author      : sangxxjin
 * Date        : 2025. 4. 4.
 * Description : QueryDSL
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 4.     sangxxjin               Initial creation
 */
@Repository
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private static final String SORT_BY_LIKE = "like";

	public ProjectRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Optional<Project> fetchProjectDetailById(Long projectId, String memberId) {
		QProject project = QProject.project;
		QProjectTechStack projectTechStack = QProjectTechStack.projectTechStack;
		QProjectDevPosition projectDevPosition = QProjectDevPosition.projectDevPosition;
		QLike like = QLike.like;
		QProjectMembership projectMembership = QProjectMembership.projectMembership;

		JPAQuery<Project> query = baseProjectJoinQuery(project, projectTechStack, projectDevPosition, like)
			.leftJoin(project.projectMemberships, projectMembership).fetchJoin()
			.where(project.id.eq(projectId))
			.distinct();

		Project result = query.fetchOne();
		return Optional.ofNullable(result);
	}

	@Override
	public Optional<Project> fetchProjectDetailById(Long projectId) {
		QProject project = QProject.project;
		QProjectTechStack projectTechStack = QProjectTechStack.projectTechStack;
		QProjectDevPosition projectDevPosition = QProjectDevPosition.projectDevPosition;
		QLike like = QLike.like;

		JPAQuery<Project> query = baseProjectJoinQuery(project, projectTechStack, projectDevPosition, like)
			.where(project.id.eq(projectId))
			.distinct();

		Project result = query.fetchOne();
		return Optional.ofNullable(result);
	}

	@Override
	public Page<ProjectListResponse> findHotProjectsForMain(Pageable pageable) {
		List<Long> projectIds = getHotProjectIds(pageable.getOffset(), pageable.getPageSize());
		if (projectIds.isEmpty())
			return new PageImpl<>(Collections.emptyList(), pageable, 0);

		List<Project> projects = fetchProjectsWithDetails(projectIds);
		List<ProjectListResponse> content = convertToDtoOrdered(projects, projectIds);

		Long total = queryFactory.select(QProject.project.countDistinct())
			.from(QProject.project)
			.where(QProject.project.isOpen.isTrue())
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}

	@Override
	public List<ProjectListResponse> findHotProjectsForProject(int limit) {
		List<Long> projectIds = getHotProjectIds(0, limit);
		if (projectIds.isEmpty())
			return Collections.emptyList();

		List<Project> projects = fetchProjectsWithDetails(projectIds);
		return convertToDtoOrdered(projects, projectIds);
	}

	@Override
	public Page<ProjectListResponse> findByFilters(Pageable pageable, List<Long> techStackIds,
		List<Long> devPositionIds, String sort) {
		BooleanBuilder where = buildFilterConditions(techStackIds, devPositionIds);

		List<Long> filteredProjectIds = queryFactory
			.select(QProject.project.id)
			.from(QProject.project)
			.leftJoin(QProject.project.projectTechStacks, QProjectTechStack.projectTechStack)
			.leftJoin(QProject.project.projectDevPositions, QProjectDevPosition.projectDevPosition)
			.where(where)
			.groupBy(QProject.project.id)
			.having(
				techStackIds != null && !techStackIds.isEmpty() ?
					QProjectTechStack.projectTechStack.techStack.id.countDistinct().eq((long)techStackIds.size()) :
					null,
				devPositionIds != null && !devPositionIds.isEmpty() ?
					QProjectDevPosition.projectDevPosition.developmentPosition.id.countDistinct()
						.eq((long)devPositionIds.size()) : null
			)
			.fetch();

		if (filteredProjectIds.isEmpty())
			return new PageImpl<>(Collections.emptyList(), pageable, 0);

		List<Long> sortedIds = sortFilteredProjects(filteredProjectIds, sort, pageable);
		if (sortedIds.isEmpty())
			return new PageImpl<>(Collections.emptyList(), pageable, 0);

		List<Project> projects = fetchProjectsWithDetails(sortedIds);
		List<ProjectListResponse> content = convertToDtoOrdered(projects, sortedIds);

		return new PageImpl<>(content, pageable, filteredProjectIds.size());
	}

	private JPAQuery<Project> baseProjectJoinQuery(
		QProject project,
		QProjectTechStack projectTechStack,
		QProjectDevPosition projectDevPosition,
		QLike like
	) {
		return queryFactory.selectFrom(project)
			.leftJoin(project.projectTechStacks, projectTechStack).fetchJoin()
			.leftJoin(projectTechStack.techStack).fetchJoin()
			.leftJoin(project.projectDevPositions, projectDevPosition).fetchJoin()
			.leftJoin(projectDevPosition.developmentPosition).fetchJoin()
			.leftJoin(project.likes, like).fetchJoin()
			.leftJoin(project.member).fetchJoin();
	}

	private List<Long> getHotProjectIds(long offset, long limit) {
		return queryFactory.select(QProject.project.id)
			.from(QProject.project)
			.leftJoin(QProject.project.likes, QLike.like)
			.where(QProject.project.isOpen.isTrue())
			.groupBy(QProject.project.id)
			.orderBy(QProject.project.viewCount.desc(), QLike.like.id.count().desc())
			.offset(offset)
			.limit(limit)
			.fetch();
	}

	private List<Project> fetchProjectsWithDetails(List<Long> ids) {
		return queryFactory.selectFrom(QProject.project)
			.leftJoin(QProject.project.projectTechStacks, QProjectTechStack.projectTechStack).fetchJoin()
			.leftJoin(QProjectTechStack.projectTechStack.techStack).fetchJoin()
			.leftJoin(QProject.project.projectDevPositions, QProjectDevPosition.projectDevPosition).fetchJoin()
			.leftJoin(QProjectDevPosition.projectDevPosition.developmentPosition).fetchJoin()
			.leftJoin(QProject.project.likes, QLike.like).fetchJoin()
			.leftJoin(QProject.project.member).fetchJoin()
			.where(QProject.project.id.in(ids))
			.distinct()
			.fetch();
	}

	private List<ProjectListResponse> convertToDtoOrdered(List<Project> projects, List<Long> orderedIds) {
		Map<Long, ProjectListResponse> projectDtoMap = projects.stream()
			.collect(Collectors.toMap(Project::getId, this::toDto));

		return orderedIds.stream()
			.map(projectDtoMap::get)
			.toList();
	}

	private ProjectListResponse toDto(Project p) {
		List<String> techStacksImageUrl = p.getProjectTechStacks().stream()
			.map(ts -> ts.getTechStack().getImageUrl())
			.toList();

		long likeCount = p.getLikes().size();
		int recruitmentCount = p.getProjectDevPositions().stream()
			.mapToInt(ProjectDevPosition::getAmount).sum();

		String description = p.getDescription();
		if (description.length() > 30) {
			description = description.substring(0, 30) + "...";
		}

		return new ProjectListResponse(
			p.getId(),
			p.getTitle(),
			description,
			p.getProjectImageUrl(),
			p.getViewCount(),
			likeCount,
			recruitmentCount,
			p.getIsOpen(),
			p.getStartDate(),
			p.getEndDate(),
			techStacksImageUrl,
			p.getMember().getUsername(),
			p.getMember().getProfileImageUrl()
		);
	}

	private BooleanBuilder buildFilterConditions(List<Long> techStackIds, List<Long> devPositionIds) {
		BooleanBuilder where = new BooleanBuilder();
		if (techStackIds != null && !techStackIds.isEmpty()) {
			where.and(QProjectTechStack.projectTechStack.techStack.id.in(techStackIds));
		}
		if (devPositionIds != null && !devPositionIds.isEmpty()) {
			where.and(QProjectDevPosition.projectDevPosition.developmentPosition.id.in(devPositionIds));
		}
		return where;
	}

	private List<Long> sortFilteredProjects(List<Long> ids, String sort, Pageable pageable) {
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
}


