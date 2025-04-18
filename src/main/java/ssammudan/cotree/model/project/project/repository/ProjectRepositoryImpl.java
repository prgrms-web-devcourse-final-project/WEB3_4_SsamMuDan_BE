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

import ssammudan.cotree.domain.project.project.dto.ProjectLikeListResponse;
import ssammudan.cotree.domain.project.project.dto.ProjectListResponse;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.devposition.entity.QProjectDevPosition;
import ssammudan.cotree.model.project.membership.entity.QProjectMembership;
import ssammudan.cotree.model.project.project.entity.Project;
import ssammudan.cotree.model.project.project.entity.QProject;
import ssammudan.cotree.model.project.project.mapper.ProjectMapper;
import ssammudan.cotree.model.project.project.repository.support.ProjectQuerySupport;
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

	private static final QProject PROJECT = QProject.project;
	private static final QProjectTechStack PROJECT_TECH_STACK = QProjectTechStack.projectTechStack;
	private static final QProjectDevPosition PROJECT_DEV_POSITION = QProjectDevPosition.projectDevPosition;
	private static final QLike LIKE = QLike.like;
	private static final QProjectMembership PROJECT_MEMBERSHIP = QProjectMembership.projectMembership;

	private final JPAQueryFactory queryFactory;
	private final ProjectQuerySupport projectQuerySupport;
	private final ProjectMapper projectMapper;

	public ProjectRepositoryImpl(JPAQueryFactory queryFactory, ProjectQuerySupport projectQuerySupport,
		ProjectMapper projectMapper) {
		this.queryFactory = queryFactory;
		this.projectQuerySupport = projectQuerySupport;
		this.projectMapper = projectMapper;
	}

	@Override
	public Optional<Project> fetchProjectDetailById(Long projectId, String memberId) {
		JPAQuery<Project> query = baseProjectJoinQuery()
			.leftJoin(PROJECT.projectMemberships, PROJECT_MEMBERSHIP).fetchJoin()
			.where(PROJECT.id.eq(projectId))
			.distinct();

		Project result = query.fetchOne();
		return Optional.ofNullable(result);
	}

	@Override
	public Optional<Project> fetchProjectDetailById(Long projectId) {
		JPAQuery<Project> query = baseProjectJoinQuery()
			.where(PROJECT.id.eq(projectId))
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
		List<ProjectListResponse> content = projectMapper.toDtoOrdered(projects, projectIds);

		Long total = queryFactory.select(PROJECT.countDistinct())
			.from(PROJECT)
			.where(PROJECT.isOpen.isTrue())
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}

	@Override
	public List<ProjectListResponse> findHotProjectsForProject(int limit) {
		List<Long> projectIds = getHotProjectIds(0, limit);
		if (projectIds.isEmpty())
			return Collections.emptyList();

		List<Project> projects = fetchProjectsWithDetails(projectIds);
		return projectMapper.toDtoOrdered(projects, projectIds);
	}

	@Override
	public Page<ProjectListResponse> findByFilters(Pageable pageable, List<Long> techStackIds,
		List<Long> devPositionIds, String sort) {
		BooleanBuilder where = projectQuerySupport.buildFilterConditions(techStackIds, devPositionIds);

		List<Long> filteredProjectIds = queryFactory
			.select(PROJECT.id)
			.from(PROJECT)
			.leftJoin(PROJECT.projectTechStacks, PROJECT_TECH_STACK)
			.leftJoin(PROJECT.projectDevPositions, PROJECT_DEV_POSITION)
			.where(where)
			.groupBy(PROJECT.id)
			.having(
				techStackIds != null && !techStackIds.isEmpty() ?
					PROJECT_TECH_STACK.techStack.id.countDistinct().eq((long)techStackIds.size()) :
					null,
				devPositionIds != null && !devPositionIds.isEmpty() ?
					PROJECT_DEV_POSITION.developmentPosition.id.countDistinct().eq((long)devPositionIds.size()) :
					null
			)
			.fetch();

		if (filteredProjectIds.isEmpty())
			return new PageImpl<>(Collections.emptyList(), pageable, 0);

		List<Long> sortedIds = projectQuerySupport.sortFilteredProjects(filteredProjectIds, sort, pageable);
		if (sortedIds.isEmpty())
			return new PageImpl<>(Collections.emptyList(), pageable, 0);

		List<Project> projects = fetchProjectsWithDetails(sortedIds);
		List<ProjectListResponse> content = projectMapper.toDtoOrdered(projects, sortedIds);

		return new PageImpl<>(content, pageable, filteredProjectIds.size());
	}

	@Override
	public List<ProjectDevPosition> findAllByProjectId(Long projectId) {
		QProjectDevPosition pdp = QProjectDevPosition.projectDevPosition;

		return queryFactory
			.selectFrom(pdp)
			.where(pdp.project.id.eq(projectId))
			.fetch();
	}

	@Override
	public Page<ProjectLikeListResponse> getLikeProjects(Pageable pageable, String memberId) {
		List<Long> likedProjectIds = queryFactory
			.select(LIKE.project.id)
			.from(LIKE)
			.where(
				LIKE.member.id.eq(memberId),
				LIKE.project.id.isNotNull()
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		if (likedProjectIds.isEmpty()) {
			return new PageImpl<>(Collections.emptyList(), pageable, 0);
		}

		List<Project> projects = fetchProjectsWithDetails(likedProjectIds);
		List<ProjectLikeListResponse> content = projectMapper.toLikeDtoOrdered(projects, likedProjectIds);

		Long total = queryFactory
			.select(LIKE.count())
			.from(LIKE)
			.where(
				LIKE.member.id.eq(memberId),
				LIKE.project.id.isNotNull()
			)
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}

	private JPAQuery<Project> baseProjectJoinQuery() {
		return queryFactory.selectFrom(PROJECT)
			.leftJoin(PROJECT.projectTechStacks, PROJECT_TECH_STACK).fetchJoin()
			.leftJoin(PROJECT_TECH_STACK.techStack).fetchJoin()
			.leftJoin(PROJECT.projectDevPositions, PROJECT_DEV_POSITION).fetchJoin()
			.leftJoin(PROJECT_DEV_POSITION.developmentPosition).fetchJoin()
			.leftJoin(PROJECT.likes, LIKE).fetchJoin()
			.leftJoin(PROJECT.member).fetchJoin();
	}

	private List<Long> getHotProjectIds(long offset, long limit) {
		return queryFactory.select(PROJECT.id)
			.from(PROJECT)
			.leftJoin(PROJECT.likes, LIKE)
			.where(PROJECT.isOpen.isTrue())
			.groupBy(PROJECT.id)
			.orderBy(PROJECT.viewCount.desc(), LIKE.id.count().desc())
			.offset(offset)
			.limit(limit)
			.fetch();
	}

	private List<Project> fetchProjectsWithDetails(List<Long> ids) {
		return queryFactory.selectFrom(PROJECT)
			.leftJoin(PROJECT.projectTechStacks, PROJECT_TECH_STACK).fetchJoin()
			.leftJoin(PROJECT_TECH_STACK.techStack).fetchJoin()
			.leftJoin(PROJECT.projectDevPositions, PROJECT_DEV_POSITION).fetchJoin()
			.leftJoin(PROJECT_DEV_POSITION.developmentPosition).fetchJoin()
			.leftJoin(PROJECT.likes, LIKE).fetchJoin()
			.leftJoin(PROJECT.member).fetchJoin()
			.where(PROJECT.id.in(ids))
			.distinct()
			.fetch();
	}
}
