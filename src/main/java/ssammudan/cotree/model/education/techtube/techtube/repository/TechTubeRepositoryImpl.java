package ssammudan.cotree.model.education.techtube.techtube.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeResponse;
import ssammudan.cotree.domain.education.type.SearchEducationSort;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.education.category.entity.QEducationCategory;
import ssammudan.cotree.model.education.level.entity.QEducationLevel;
import ssammudan.cotree.model.education.techtube.category.entity.QTechTubeEducationCategory;
import ssammudan.cotree.model.education.techtube.techtube.entity.QTechTube;
import ssammudan.cotree.model.member.member.entity.QMember;
import ssammudan.cotree.model.payment.order.category.entity.QOrderCategory;
import ssammudan.cotree.model.payment.order.history.entity.QOrderHistory;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.techtube.repository
 * FileName    : TechTubeRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 3. 30.
 * Description : TechTube Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 30.    loadingKKamo21       Initial creation
 * 25. 4. 7.     Baekgwa       		  로직 리팩토링 및, 회원의 좋아요 상태 추가.
 * 25. 4. 7.     Baekgwa       		  techTube 상세 조회 refactor
 */
@Repository
@RequiredArgsConstructor
public class TechTubeRepositoryImpl implements TechTubeRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private static final QTechTube techTube = QTechTube.techTube;
	private static final QMember member = QMember.member;
	private static final QLike like = QLike.like;
	private static final QTechTubeEducationCategory techTubeEducationCategory =
		QTechTubeEducationCategory.techTubeEducationCategory;
	private static final QEducationLevel educationLevel = QEducationLevel.educationLevel;
	private static final QEducationCategory educationCategory = QEducationCategory.educationCategory;
	private static final QOrderHistory orderHistory = QOrderHistory.orderHistory;
	private static final QOrderCategory orderCategory = QOrderCategory.orderCategory;

	/**
	 * 전체 TechTube 목록 조회
	 */
	@Override
	public Page<TechTubeResponse.ListInfo> findTechTubeList(
		final String keyword,
		final SearchEducationSort sort,
		final Pageable pageable,
		final String memberId,
		final Long educationId
	) {
		List<TechTubeResponse.ListInfo> content = getTechTubeList(keyword, sort, pageable, memberId, educationId);

		// 전체 데이터 수량 count 조회
		Long total = getTechTubeListCount(keyword, educationId);

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}

	@Override
	public TechTubeResponse.TechTubeDetail findTechTube(final Long techTubeId, final String memberId) {
		TechTubeResponse.TechTubeDetail content = jpaQueryFactory
			.select(Projections.constructor(TechTubeResponse.TechTubeDetail.class,
				member.nickname,
				educationLevel.name,
				Expressions.constant(new ArrayList<String>()),
				techTube.title,
				techTube.description,
				techTube.introduction,
				Expressions.numberOperation(Double.class, Ops.DIV,
					techTube.totalRating.castToNum(Double.class),
					Expressions.numberTemplate(Double.class, "CASE WHEN {0} = 0 THEN 1.0 ELSE {0} END",
						techTube.totalReviewCount)),
				techTube.totalReviewCount,
				techTube.techTubeUrl,
				techTube.techTubeDuration,
				techTube.techTubeThumbnailUrl,
				techTube.price,
				techTube.viewCount,
				JPAExpressions
					.select(like.count())
					.from(like)
					.where(like.techTube.id.eq(techTubeId)),
				memberId != null ? JPAExpressions
					.select(like.count())
					.from(like)
					.where(like.techTube.id.eq(techTube.id)
						.and(like.member.id.eq(memberId)))
					.exists()
					: Expressions.constant(Boolean.FALSE),
				techTube.createdAt,
				memberId != null ? JPAExpressions
					.select(orderHistory.count())
					.from(orderHistory)
					.join(orderCategory).on(orderHistory.orderCategory.id.eq(orderCategory.id))
					.where(
						orderHistory.customer.id.eq(memberId)
							.and(orderHistory.productId.eq(techTubeId))
							.and(orderCategory.name.eq("TechTube"))
					)
					.exists()
					: Expressions.constant(Boolean.FALSE)
			))
			.from(techTube)
			.join(member).on(techTube.writer.id.eq(member.id))
			.join(educationLevel).on(techTube.educationLevel.id.eq(educationLevel.id))
			.where(techTube.id.eq(techTubeId))
			.fetchOne();

		if (content != null) {
			// 카테고리 목록 별도 조회
			List<String> findCategoryList = jpaQueryFactory
				.select(educationCategory.name)
				.from(techTubeEducationCategory)
				.join(educationCategory).on(techTubeEducationCategory.educationCategory.id.eq(educationCategory.id))
				.where(techTubeEducationCategory.techTube.id.eq(techTubeId))
				.fetch();

			content.addEducationCategoryList(findCategoryList);
		}

		return content;
	}

	private List<TechTubeResponse.ListInfo> getTechTubeList(String keyword, SearchEducationSort sort, Pageable pageable,
		String memberId, Long educationId) {
		JPAQuery<TechTubeResponse.ListInfo> query = jpaQueryFactory
			.select(Projections.constructor(TechTubeResponse.ListInfo.class,
				techTube.id,
				member.nickname,
				techTube.title,
				techTube.price,
				techTube.techTubeThumbnailUrl,
				JPAExpressions
					.select(like.count())
					.from(like)
					.where(like.techTube.id.eq(techTube.id)),
				techTube.createdAt,
				memberId != null ? JPAExpressions
					.select(like.count())
					.from(like)
					.where(like.techTube.id.eq(techTube.id)
						.and(like.member.id.eq(memberId)))
					.exists()
					: Expressions.constant(Boolean.FALSE)
			))
			.from(techTube)
			.join(member).on(techTube.writer.id.eq(member.id))
			.where(getSearchCondition(keyword))
			.orderBy(getSortCondition(sort))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		//educationId 유무에 따라, 동적으로 Join 추가
		addJoinByEducationCategory(query, educationId);

		// 결과 fetch
		return query.fetch();
	}

	private Long getTechTubeListCount(String keyword, Long educationId) {
		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(techTube.count())
			.from(techTube)
			.where(getSearchCondition(keyword));

		//educationId 유무에 따라, 동적으로 Join 추가
		addJoinByEducationCategory(countQuery, educationId);

		return countQuery.fetchOne();
	}

	private <T> void addJoinByEducationCategory(JPAQuery<T> query, Long educationId) {
		if (educationId != null) {
			query.join(techTubeEducationCategory)
				.on(techTube.id.eq(techTubeEducationCategory.techTube.id)
					.and(techTubeEducationCategory.educationCategory.id.eq(educationId)));
		}
	}

	/**
	 * 검색어(keyword) 기준 BooleanExpression 생성
	 */
	private BooleanExpression getSearchCondition(final String keyword) {
		BooleanExpression expression = null;

		if (StringUtils.hasText(keyword)) {
			expression = techTube.title.containsIgnoreCase(keyword)
				.or(techTube.description.containsIgnoreCase(keyword))
				.or(techTube.introduction.containsIgnoreCase(keyword));
		}

		return expression;
	}

	/**
	 * 정렬 조건 생성
	 */
	private OrderSpecifier<?> getSortCondition(SearchEducationSort sort) {
		return switch (sort) {
			case LATEST -> new OrderSpecifier<>(Order.DESC,
				techTube.createdAt);
			case RATING -> new OrderSpecifier<>(Order.DESC,
				Expressions.numberTemplate(Double.class, "CASE WHEN {1} = 0 THEN 0 ELSE ({0} * 1.0 / {1}) END",
					techTube.totalRating, techTube.totalReviewCount));
			case LIKES -> new OrderSpecifier<>(Order.DESC, techTube.likes.size());
			case VIEWS -> new OrderSpecifier<>(Order.DESC, techTube.viewCount);
			case REVIEWS -> new OrderSpecifier<>(Order.DESC, techTube.totalReviewCount);
		};
	}
}
