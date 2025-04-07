package ssammudan.cotree.model.project.project.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ssammudan.cotree.domain.project.dto.ProjectListResponse;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.project.devposition.entity.QProjectDevPosition;
import ssammudan.cotree.model.project.membership.entity.QProjectMembership;
import ssammudan.cotree.model.project.project.entity.Project;
import ssammudan.cotree.model.project.project.entity.QProject;
import ssammudan.cotree.model.project.project.helper.ProjectQueryHelper;
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
	private final ProjectQueryHelper projectQueryHelper;

	public ProjectRepositoryImpl(JPAQueryFactory queryFactory, ProjectQueryHelper projectQueryHelper) {
		this.queryFactory = queryFactory;
		this.projectQueryHelper = projectQueryHelper;
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
		List<ProjectListResponse> content = projectQueryHelper.convertToDtoOrdered(projects, projectIds);

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
		return projectQueryHelper.convertToDtoOrdered(projects, projectIds);
	}

	@Override
	public Page<ProjectListResponse> findByFilters(Pageable pageable, List<Long> techStackIds,
		List<Long> devPositionIds, String sort) {
		BooleanBuilder where = projectQueryHelper.buildFilterConditions(techStackIds, devPositionIds);

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

		List<Long> sortedIds = projectQueryHelper.sortFilteredProjects(filteredProjectIds, sort, pageable);
		if (sortedIds.isEmpty())
			return new PageImpl<>(Collections.emptyList(), pageable, 0);

		List<Project> projects = fetchProjectsWithDetails(sortedIds);
		List<ProjectListResponse> content = projectQueryHelper.convertToDtoOrdered(projects, sortedIds);

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
}
