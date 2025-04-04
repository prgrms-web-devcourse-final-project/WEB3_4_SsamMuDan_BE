package ssammudan.cotree.model.education.techtube.techtube.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.model.education.techtube.techtube.entity.QTechTube;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;

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
 */
@Repository
@RequiredArgsConstructor
public class TechTubeRepositoryImpl implements TechTubeRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	/**
	 * 검색어(keyword)로 TechTube 페이징 목록 조회
	 *
	 * @param keyword  - 검색어
	 * @param pageable - 페이징 객체
	 * @return Page<TechTube>
	 */
	@Override
	public Page<TechTube> findAllTechTubesByKeyword(final String keyword, final Pageable pageable) {
		QTechTube techTube = QTechTube.techTube;
		List<TechTube> content = jpaQueryFactory.selectFrom(techTube)
			.where(getSearchCondition(keyword, techTube))
			.orderBy(getSortCondition(pageable, techTube))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		JPAQuery<Long> count = jpaQueryFactory.select(techTube.count())
			.from(techTube)
			.where(getSearchCondition(keyword, techTube));
		return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
	}

	/**
	 * 검색어(keyword) 기준 BooleanExpression 생성
	 *
	 * @param keyword  - 검색어
	 * @param techTube - Querydsl TechTube
	 * @return BooleanExpression
	 */
	private BooleanExpression getSearchCondition(final String keyword, @NotNull final QTechTube techTube) {
		BooleanExpression expression = null;

		if (StringUtils.hasText(keyword)) {
			expression = techTube.title.contains(keyword)
				.or(techTube.description.contains(keyword))
				.or(techTube.introduction.contains(keyword));
		}

		return expression;
	}

	/**
	 * 페이징 객체에 포함된 정렬 조건에 따라 OrderSpecifier[] 생성
	 *
	 * @param pageable - 페이징 객체
	 * @param techTube - Querydsl TechTube
	 * @return OrderSpecifier[]
	 */
	private OrderSpecifier<?>[] getSortCondition(@NotNull final Pageable pageable, @NotNull final QTechTube techTube) {
		List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
		boolean hasCreatedAt = pageable.getSort().stream().anyMatch(order -> "createdAt".equals(order.getProperty()));

		if (!pageable.getSort().isEmpty()) {
			pageable.getSort().forEach(order -> {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

				switch (order.getProperty()) {
					case "rating" -> orderSpecifiers.add(new OrderSpecifier(direction,
						Expressions.numberTemplate(Double.class, "CASE WHEN {1} = 0 THEN 0 ELSE ({0} * 1.0 / {1}) END",
							techTube.totalRating, techTube.totalReviewCount))
					);
					case "viewCount" -> orderSpecifiers.add(new OrderSpecifier(direction, techTube.viewCount));
					case "reviewCount" -> orderSpecifiers.add(new OrderSpecifier(direction, techTube.totalReviewCount));
					case "createdAt" -> orderSpecifiers.add(new OrderSpecifier(direction, techTube.createdAt));
					case "likeCount" -> orderSpecifiers.add(new OrderSpecifier(direction, techTube.likes.size()));
					default -> {
					}
				}
			});
		}

		if (!hasCreatedAt) {
			orderSpecifiers.add(new OrderSpecifier(Order.DESC, techTube.createdAt));
		}

		return orderSpecifiers.toArray(new OrderSpecifier[0]);
	}

}
