package ssammudan.cotree.model.project.project.repository;

import java.util.ArrayList;
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
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ssammudan.cotree.domain.project.project.dto.ProjectLikeListResponse;
import ssammudan.cotree.domain.project.project.dto.ProjectListResponse;
import ssammudan.cotree.domain.project.project.dto.QProjectListResponse;
import ssammudan.cotree.domain.project.project.type.SearchProjectSort;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.common.techstack.entity.QTechStack;
import ssammudan.cotree.model.member.member.entity.QMember;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.devposition.entity.QProjectDevPosition;
import ssammudan.cotree.model.project.membership.entity.QProjectMembership;
import ssammudan.cotree.model.project.project.entity.Project;
import ssammudan.cotree.model.project.project.entity.QProject;
import ssammudan.cotree.model.project.project.mapper.ProjectMapper;
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
	private static final QTechStack TECH_STACK = QTechStack.techStack;

	private final JPAQueryFactory queryFactory;
	private final ProjectMapper projectMapper;

	public ProjectRepositoryImpl(JPAQueryFactory queryFactory, ProjectMapper projectMapper) {
		this.queryFactory = queryFactory;
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
	public Page<ProjectListResponse> findFilteredProjects(
		Pageable pageable,
		List<Long> techStackIds,
		List<Long> devPositionIds,
		SearchProjectSort sort
	) {
		boolean hasTechStack = techStackIds != null && !techStackIds.isEmpty();
		boolean hasDevPosition = devPositionIds != null && !devPositionIds.isEmpty();

		List<Long> filteredIds;
		long total;

		if (hasTechStack || hasDevPosition) {
			BooleanBuilder whereBuilder = new BooleanBuilder();
			if (hasTechStack)
				whereBuilder.and(PROJECT_TECH_STACK.techStack.id.in(techStackIds));
			if (hasDevPosition)
				whereBuilder.and(PROJECT_DEV_POSITION.developmentPosition.id.in(devPositionIds));

			List<Long> allMatchingIds = queryFactory
				.select(PROJECT.id)
				.from(PROJECT)
				.leftJoin(PROJECT.projectTechStacks, PROJECT_TECH_STACK)
				.leftJoin(PROJECT.projectDevPositions, PROJECT_DEV_POSITION)
				.where(whereBuilder)
				.groupBy(PROJECT.id)
				.having(
					hasTechStack
						? PROJECT_TECH_STACK.techStack.id.countDistinct().eq((long)techStackIds.size())
						: null,
					hasDevPosition
						? PROJECT_DEV_POSITION.developmentPosition.id.countDistinct().eq((long)devPositionIds.size())
						: null
				)
				.fetch();

			total = allMatchingIds.size();
			filteredIds = allMatchingIds.stream()
				.skip(pageable.getOffset())
				.limit(pageable.getPageSize())
				.toList();
		} else {
			total = queryFactory
				.select(PROJECT.count())
				.from(PROJECT)
				.fetchOne();

			filteredIds = queryFactory
				.select(PROJECT.id)
				.from(PROJECT)
				.orderBy(sort.getOrderSpecifier(PROJECT))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		}

		if (filteredIds.isEmpty()) {
			return new PageImpl<>(Collections.emptyList(), pageable, total);
		}

		NumberTemplate<Long> likeCountExpr = Expressions.numberTemplate(Long.class,
			"(select count(l1.id) from Like l1 where l1.project.id = {0})", PROJECT.id);
		NumberTemplate<Integer> recruitmentCountExpr = Expressions.numberTemplate(Integer.class,
			"(select coalesce(sum(pdp.amount), 0) from ProjectDevPosition pdp where pdp.project.id = {0})",
			PROJECT.id);

		Map<Long, List<String>> techStackImageUrlMap = queryFactory
			.select(PROJECT.id, TECH_STACK.imageUrl)
			.from(PROJECT)
			.leftJoin(PROJECT.projectTechStacks, PROJECT_TECH_STACK)
			.leftJoin(PROJECT_TECH_STACK.techStack, TECH_STACK)
			.where(PROJECT.id.in(filteredIds))
			.fetch()
			.stream()
			.collect(Collectors.groupingBy(
				tuple -> tuple.get(PROJECT.id),
				Collectors.collectingAndThen(
					Collectors.mapping(tuple -> tuple.get(TECH_STACK.imageUrl), Collectors.toSet()),
					ArrayList::new
				)
			));

		List<ProjectListResponse> finalContent = queryFactory
			.select(new QProjectListResponse(
				PROJECT.id,
				PROJECT.title,
				PROJECT.description,
				PROJECT.projectImageUrl,
				PROJECT.viewCount,
				likeCountExpr,
				recruitmentCountExpr,
				PROJECT.isOpen,
				PROJECT.startDate,
				PROJECT.endDate,
				Expressions.constant(Collections.emptyList()),
				QMember.member.username,
				QMember.member.profileImageUrl
			))
			.from(PROJECT)
			.leftJoin(PROJECT.projectTechStacks, PROJECT_TECH_STACK)
			.leftJoin(PROJECT.projectDevPositions, PROJECT_DEV_POSITION)
			.leftJoin(PROJECT.member, QMember.member)
			.where(PROJECT.id.in(filteredIds))
			.groupBy(PROJECT.id)
			.orderBy(sort.getOrderSpecifier(PROJECT))
			.fetch()
			.stream()
			.map(project -> project.withTechStacks(
				techStackImageUrlMap.getOrDefault(project.getId(), Collections.emptyList())
			))
			.toList();

		return new PageImpl<>(finalContent, pageable, total);
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
