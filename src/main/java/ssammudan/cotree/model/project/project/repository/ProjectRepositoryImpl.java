package ssammudan.cotree.model.project.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.project.devposition.entity.QProjectDevPosition;
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

	public ProjectRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Project> findByFilters(Pageable pageable, List<Long> techStackIds, List<Long> devPositionIds,
		String sort) {
		JPAQuery<Project> query = buildQueryWithFilters(techStackIds, devPositionIds, sort);

		List<Project> content = getQuerydslContent(query, pageable);
		long totalElements = query.fetchCount();

		return new PageImpl<>(content, pageable, totalElements);
	}

	private JPAQuery<Project> buildQueryWithFilters(List<Long> techStackIds, List<Long> devPositionIds, String sort) {
		QProject project = QProject.project;
		QProjectTechStack projectTechStack = QProjectTechStack.projectTechStack;
		QProjectDevPosition projectDevPosition = QProjectDevPosition.projectDevPosition;
		QLike like = QLike.like;

		JPAQuery<Project> query = queryFactory.selectDistinct(project)
			.from(project)
			.join(project.projectTechStacks, projectTechStack)
			.join(project.projectDevPositions, projectDevPosition)
			.leftJoin(project.likes, like)
			.where(isOpenFilter(project)
				.and(techStackFilter(techStackIds, projectTechStack))
				.and(devPositionFilter(devPositionIds, projectDevPosition)))
			.groupBy(project.id);

		if ("like".equals(sort)) {
			query.orderBy(like.id.count().desc());
		} else {
			query.orderBy(project.createdAt.desc());
		}

		return query;
	}

	private BooleanExpression techStackFilter(List<Long> techStackIds, QProjectTechStack projectTechStack) {
		return (techStackIds != null && !techStackIds.isEmpty()) ? projectTechStack.techStack.id.in(techStackIds) :
			null;
	}

	private BooleanExpression devPositionFilter(List<Long> devPositionIds, QProjectDevPosition projectDevPosition) {
		return (devPositionIds != null && !devPositionIds.isEmpty()) ?
			projectDevPosition.developmentPosition.id.in(devPositionIds) : null;
	}

	private BooleanExpression isOpenFilter(QProject project) {
		return project.isOpen.isTrue();
	}

	private List<Project> getQuerydslContent(JPAQuery<Project> query, Pageable pageable) {
		return query.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}
}

