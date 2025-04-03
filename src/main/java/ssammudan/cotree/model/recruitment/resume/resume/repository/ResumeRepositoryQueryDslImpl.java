package ssammudan.cotree.model.recruitment.resume.resume.repository;

import static ssammudan.cotree.model.common.developmentposition.entity.QDevelopmentPosition.*;
import static ssammudan.cotree.model.common.techstack.entity.QTechStack.*;
import static ssammudan.cotree.model.recruitment.resume.developmentposition.entity.QResumeDevelopmentPosition.*;
import static ssammudan.cotree.model.recruitment.resume.resume.entity.QResume.*;
import static ssammudan.cotree.model.recruitment.resume.techstack.entity.QResumeTechStack.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.resume.dto.ResumeResponse;
import ssammudan.cotree.domain.resume.dto.SearchResumeSort;

/**
 * PackageName : ssammudan.cotree.model.recruitment.resume.resume.repository
 * FileName    : ResumeRepositoryQueryDslImpl
 * Author      : kwak
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     kwak               Initial creation
 */
@RequiredArgsConstructor
public class ResumeRepositoryQueryDslImpl implements ResumeRepositoryQueryDsl {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<ResumeResponse> getResumeList(Pageable pageable, List<Long> positionIds, List<Long> skillIds,
		Integer startYear, Integer endYear, SearchResumeSort sort) {

		BooleanBuilder whereCondition = getWhereCondition(positionIds, skillIds, startYear, endYear);

		List<ResumeResponse> resumeResponses = jpaQueryFactory
			.select(Projections.constructor(
				ResumeResponse.class,
				resume.profileImage,
				resume.isOpen,
				developmentPosition.name,
				resume.years,
				getStringTemplate(),
				resume.createdAt
			)).from(resume)
			.join(resume.resumeDevelopmentPositions, resumeDevelopmentPosition)
			.join(resumeDevelopmentPosition.developmentPosition, developmentPosition)
			.join(resume.resumeTechStacks, resumeTechStack)
			.join(resumeTechStack.techStack, techStack)
			.where(whereCondition)
			.orderBy(getOrderSpecifier(sort))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(resume.countDistinct())
			.from(resume)
			.join(resume.resumeDevelopmentPositions, resumeDevelopmentPosition)
			.join(resume.resumeTechStacks, resumeTechStack)
			.where(whereCondition)
			.fetchOne();

		return new PageImpl<>(resumeResponses, pageable, total != null ? total : 0);
	}

	private StringTemplate getStringTemplate() {
		return Expressions.stringTemplate("SUBSTRING({0}, 1, {1})", resume.introduction, 30);
	}

	private BooleanBuilder getWhereCondition(List<Long> positionIds, List<Long> skillIds, Integer startYear,
		Integer endYear) {
		BooleanBuilder whereCondition = new BooleanBuilder();

		if (positionIds != null && !positionIds.isEmpty()) {
			whereCondition.and(resumeDevelopmentPosition.developmentPosition.id.in(positionIds));
		}

		if (skillIds != null && !skillIds.isEmpty()) {
			whereCondition.and(resumeTechStack.techStack.id.in(skillIds));
		}

		if (startYear != null) {
			whereCondition.and(resume.years.goe(startYear));
		}

		if (endYear != null) {
			whereCondition.and(resume.years.loe(endYear));
		}

		return whereCondition;
	}

	private OrderSpecifier<?> getOrderSpecifier(SearchResumeSort sort) {

		return switch (sort) {
			case VIEW -> new OrderSpecifier<>(Order.DESC, resume.viewCount);
			case LATEST -> new OrderSpecifier<>(Order.DESC, resume.createdAt);
		};

	}
}
