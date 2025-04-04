package ssammudan.cotree.model.review.review.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.model.review.review.entity.QTechEducationReview;
import ssammudan.cotree.model.review.review.entity.TechEducationReview;

/**
 * PackageName : ssammudan.cotree.model.review.review.repository
 * FileName    : TechEducationReviewRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 1.
 * Description : TechEducationReview Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     loadingKKamo21       Initial creation
 */
@Repository
@RequiredArgsConstructor
public class TechEducationReviewRepositoryImpl implements TechEducationReviewRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	/**
	 * TechEducationReview 페이징 목록 조회
	 * @param techEducationTypeId - 교육 컨텐츠 타입 ID
	 * @param itemId              - 교육 컨텐츠 ID
	 * @param pageable            - 페이징 객체
	 * @return Page<TechEducationReview>
	 */
	@Override
	public Page<TechEducationReview> findAllTechEducationReviews(
		final Long techEducationTypeId, final Long itemId, final Pageable pageable
	) {
		QTechEducationReview techEducationReview = QTechEducationReview.techEducationReview;
		List<TechEducationReview> content = jpaQueryFactory.selectFrom(techEducationReview)
			.where(techEducationReview.techEducationType.id.eq(techEducationTypeId),
				techEducationReview.itemId.eq(itemId))
			.orderBy(getSortCondition(pageable, techEducationReview))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		JPAQuery<Long> count = jpaQueryFactory.select(techEducationReview.count())
			.from(techEducationReview)
			.where(techEducationReview.techEducationType.id.eq(techEducationTypeId),
				techEducationReview.itemId.eq(itemId));
		return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
	}

	/**
	 * 페이징 객체에 포함된 정렬 조건에 따라 OrderSpecifier[] 생성
	 * @param pageable            - 페이징 객체
	 * @param techEducationReview - Querydsl TechEducationReview
	 * @return OrderSpecifier[]
	 */
	private OrderSpecifier<?>[] getSortCondition(
		@NotNull final Pageable pageable, @NotNull final QTechEducationReview techEducationReview
	) {
		List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
		boolean hasCreatedAt = pageable.getSort().stream().anyMatch(order -> "createdAt".equals(order.getProperty()));

		if (!pageable.getSort().isEmpty()) {
			pageable.getSort().forEach(order -> {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

				switch (order.getProperty()) {
					case "rating" -> orderSpecifiers.add(new OrderSpecifier(direction, techEducationReview.rating));
					case "createdAt" ->
						orderSpecifiers.add(new OrderSpecifier(direction, techEducationReview.createdAt));
					default -> {
					}
				}
			});
		}

		if (!hasCreatedAt) {
			orderSpecifiers.add(new OrderSpecifier(Order.DESC, techEducationReview.createdAt));
		}

		return orderSpecifiers.toArray(new OrderSpecifier[0]);
	}

}
