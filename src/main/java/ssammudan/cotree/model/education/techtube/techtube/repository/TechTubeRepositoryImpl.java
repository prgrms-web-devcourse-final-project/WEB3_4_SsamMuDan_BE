package ssammudan.cotree.model.education.techtube.techtube.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeResponse;
import ssammudan.cotree.domain.education.type.SearchEducationSort;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.education.techtube.techtube.entity.QTechTube;
import ssammudan.cotree.model.member.member.entity.QMember;

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
 */
@Repository
@RequiredArgsConstructor
public class TechTubeRepositoryImpl implements TechTubeRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private static final QTechTube techTube = QTechTube.techTube;
	private static final QMember member = QMember.member;
	private static final QLike like = QLike.like;

	/**
	 * 전체 TechTube 목록 조회
	 * @param keyword
	 * @param sort
	 * @param pageable
	 * @param memberId
	 * @return
	 */
	@Override
	public Page<TechTubeResponse.ListInfo> findAllTechTubesByKeyword(
		final String keyword,
		final SearchEducationSort sort,
		final Pageable pageable,
		final String memberId
	) {
		List<TechTubeResponse.ListInfo> content = jpaQueryFactory
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
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(techTube.count())
			.from(techTube)
			.where(getSearchCondition(keyword))
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
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
	 * @param sort
	 * @return
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
