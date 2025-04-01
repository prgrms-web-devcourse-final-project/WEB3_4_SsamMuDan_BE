package ssammudan.cotree.model.education.techbook.techbook.repository;

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
import ssammudan.cotree.model.education.techbook.techbook.entity.QTechBook;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.techbook.repository
 * FileName    : TechBookRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBook Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 * 25. 4. 1.	 loadingKKamo21		  findAllTechBooksByKeyword() 구현, getSearchCondition(), getSortCondition() 추가
 */
@Repository
@RequiredArgsConstructor
public class TechBookRepositoryImpl implements TechBookRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	/**
	 * 검색어(keyword)로 TechBook 페이징 목록 조회
	 *
	 * @param keyword  - 검색어
	 * @param pageable - 페이징 객체
	 * @return Page<TechBook>
	 */
	@Override
	public Page<TechBook> findAllTechBooksByKeyword(final String keyword, final Pageable pageable) {
		QTechBook techBook = QTechBook.techBook;
		List<TechBook> content = jpaQueryFactory.selectFrom(techBook)
			.where(getSearchCondition(keyword, techBook))
			.orderBy(getSortCondition(pageable, techBook))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		JPAQuery<Long> count = jpaQueryFactory.select(techBook.count())
			.from(techBook)
			.where(getSearchCondition(keyword, techBook));
		return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
	}

	/**
	 * 검색어(keyword) 기준 BooleanExpression 생성
	 *
	 * @param keyword  - 검색어
	 * @param techBook - Querydsl TechBook
	 * @return BooleanExpression
	 */
	private BooleanExpression getSearchCondition(final String keyword, @NotNull final QTechBook techBook) {
		BooleanExpression expression = null;

		if (StringUtils.hasText(keyword)) {
			expression = techBook.title.contains(keyword)
				.or(techBook.description.contains(keyword))
				.or(techBook.introduction.contains(keyword));
		}

		return expression;
	}

	/**
	 * 페이징 객체에 포함된 정렬 조건에 따라 OrderSpecifier[] 생성
	 *
	 * @param pageable - 페이징 객체
	 * @param techBook - Querydsl TechBook
	 * @return OrderSpecifier[]
	 */
	private OrderSpecifier<?>[] getSortCondition(@NotNull final Pageable pageable, @NotNull final QTechBook techBook) {
		List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

		if (!pageable.getSort().isEmpty()) {
			pageable.getSort().forEach(order -> {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

				switch (order.getProperty()) {
					case "title" -> orderSpecifiers.add(new OrderSpecifier(direction, techBook.title));
					case "rating" -> orderSpecifiers.add(new OrderSpecifier(direction,
						Expressions.numberTemplate(Double.class, "CASE WHEN {1} = 0 THEN 0 ELSE ({0} * 1.0 / {1}) END",
							techBook.totalRating, techBook.totalReviewCount))
					);
					case "viewCount" -> orderSpecifiers.add(new OrderSpecifier(direction, techBook.viewCount));
					case "totalReviewCount" ->
						orderSpecifiers.add(new OrderSpecifier(direction, techBook.totalReviewCount));
					case "createdAt" -> orderSpecifiers.add(new OrderSpecifier(direction, techBook.createdAt));
					default -> {
					}
				}
			});
		}

		if (orderSpecifiers.isEmpty()) {
			orderSpecifiers.add(new OrderSpecifier(Order.DESC, techBook.createdAt));
		}

		return orderSpecifiers.toArray(new OrderSpecifier[0]);
	}

}
