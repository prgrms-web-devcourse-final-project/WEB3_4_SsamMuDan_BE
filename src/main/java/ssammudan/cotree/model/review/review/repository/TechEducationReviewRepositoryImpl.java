package ssammudan.cotree.model.review.review.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.review.dto.TechEducationReviewResponse;
import ssammudan.cotree.model.member.member.entity.QMember;
import ssammudan.cotree.model.review.review.entity.QTechEducationReview;
import ssammudan.cotree.model.review.review.entity.TechEducationReview;
import ssammudan.cotree.model.review.reviewtype.entity.QTechEducationType;

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

	private static final QTechEducationReview techEducationReview = QTechEducationReview.techEducationReview;
	private static final QTechEducationType techEducationType = QTechEducationType.techEducationType;
	private static final QMember member = QMember.member;

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
		List<TechEducationReview> content = jpaQueryFactory.selectFrom(techEducationReview)
			.where(techEducationReview.techEducationType.id.eq(techEducationTypeId),
				techEducationReview.itemId.eq(itemId))
			.orderBy(getSortCondition(pageable))
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
	 * TechEducationReview 페이징 목록 조회
	 * @param techEducationTypeId - 교육 컨텐츠 타입 ID
	 * @param itemId              - 교육 컨텐츠 ID
	 * @param pageable            - 페이징 객체
	 * @return Page<TechEducationReview>
	 */
	@Override
	public Page<TechEducationReviewResponse.ReviewDetail> findReviews(
		final Long techEducationTypeId, final Long itemId, final Pageable pageable
	) {
		List<TechEducationReviewResponse.ReviewDetail> content = jpaQueryFactory.select(Projections.constructor(
				TechEducationReviewResponse.ReviewDetail.class,
				techEducationReview.id,
				techEducationType.id,
				techEducationReview.itemId,
				member.nickname,
				member.profileImageUrl,
				techEducationReview.rating,
				techEducationReview.content,
				techEducationReview.createdAt
			)).from(techEducationReview)
			.join(member).on(techEducationReview.reviewer.id.eq(member.id))
			.where(techEducationReview.techEducationType.id.eq(techEducationTypeId)
				.and(techEducationReview.itemId.eq(itemId)))
			.orderBy(getSortCondition(pageable))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		JPAQuery<Long> countJpaQuery = jpaQueryFactory.select(techEducationReview.count()).from(techEducationReview)
			.where(techEducationReview.techEducationType.id.eq(techEducationTypeId)
				.and(techEducationReview.itemId.eq(itemId)));
		return PageableExecutionUtils.getPage(content, pageable, countJpaQuery::fetchOne);
	}

	@Override
	public List<TechEducationReviewResponse.ReviewDetail> findReviewList(
		final Long techEducationTypeId, final Long itemId, final Pageable pageable
	) {
		List<TechEducationReviewResponse.ReviewDetail> content = jpaQueryFactory.select(Projections.constructor(
				TechEducationReviewResponse.ReviewDetail.class,
				techEducationReview.id,
				techEducationType.id,
				techEducationReview.itemId,
				member.nickname,
				member.profileImageUrl,
				techEducationReview.rating,
				techEducationReview.content,
				techEducationReview.createdAt
			)).from(techEducationReview)
			.join(member).on(techEducationReview.reviewer.id.eq(member.id))
			.where(techEducationReview.techEducationType.id.eq(techEducationTypeId)
				.and(techEducationReview.itemId.eq(itemId)))
			.orderBy(getSortCondition(pageable))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		return content;
	}

	/**
	 * 페이징 객체에 포함된 정렬 조건에 따라 OrderSpecifier[] 생성
	 * @param pageable            - 페이징 객체
	 * @return OrderSpecifier[]
	 */
	private OrderSpecifier<?>[] getSortCondition(
		@NotNull final Pageable pageable
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
