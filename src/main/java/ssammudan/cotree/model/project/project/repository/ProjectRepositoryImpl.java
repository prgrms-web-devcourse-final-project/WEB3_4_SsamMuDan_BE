package ssammudan.cotree.model.project.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
			.where(project.isOpen.isTrue());

		applyTechStackFilter(query, techStackIds, projectTechStack);
		applyDevPositionFilter(query, devPositionIds, projectDevPosition);
		applySort(query, sort, project, like);

		return query;
	}

	private void applyTechStackFilter(JPAQuery<Project> query, List<Long> techStackIds,
		QProjectTechStack projectTechStack) {
		if (techStackIds != null && !techStackIds.isEmpty()) {
			query.where(projectTechStack.techStack.id.in(techStackIds));
		}
	}

	private void applyDevPositionFilter(JPAQuery<Project> query, List<Long> devPositionIds,
		QProjectDevPosition projectDevPosition) {
		if (devPositionIds != null && !devPositionIds.isEmpty()) {
			query.where(projectDevPosition.developmentPosition.id.in(devPositionIds));
		}
	}

	private void applySort(JPAQuery<Project> query, String sort, QProject project, QLike like) {
		if ("createdAt".equals(sort)) {
			query.orderBy(project.createdAt.desc());
		} else if ("like".equals(sort)) {
			query.leftJoin(project.likes, QLike.like)
				.groupBy(project.id)
				.orderBy(QLike.like.id.count().desc());
		}
	}

	private List<Project> getQuerydslContent(JPAQuery<Project> query, Pageable pageable) {
		return query.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}
}

