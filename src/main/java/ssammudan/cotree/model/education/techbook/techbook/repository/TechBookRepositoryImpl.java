package ssammudan.cotree.model.education.techbook.techbook.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.education.techbook.dto.TechBookResponse;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.education.category.entity.QEducationCategory;
import ssammudan.cotree.model.education.level.entity.QEducationLevel;
import ssammudan.cotree.model.education.techbook.category.entity.QTechBookEducationCategory;
import ssammudan.cotree.model.education.techbook.techbook.entity.QTechBook;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;
import ssammudan.cotree.model.member.member.entity.QMember;
import ssammudan.cotree.model.payment.order.category.entity.QOrderCategory;
import ssammudan.cotree.model.payment.order.history.entity.QOrderHistory;

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

	private static final QTechBook techBook = QTechBook.techBook;
	private static final QMember member = QMember.member;
	private static final QEducationLevel educationLevel = QEducationLevel.educationLevel;
	private static final QEducationCategory educationCategory = QEducationCategory.educationCategory;
	private static final QTechBookEducationCategory techBookEducationCategory = QTechBookEducationCategory.techBookEducationCategory;
	private static final QOrderHistory orderHistory = QOrderHistory.orderHistory;
	private static final QOrderCategory orderCategory = QOrderCategory.orderCategory;
	private static final QLike like = QLike.like;

	private final JPAQueryFactory jpaQueryFactory;

	/**
	 * TechBook 상세 조회
	 *
	 * @param techBookId - TechBook ID
	 * @param memberId   - 회원 ID
	 * @return TechBookResponse Detail DTO
	 */
	@Override
	public TechBookResponse.TechBookDetail findTechBook(final Long techBookId, final String memberId) {
		TechBookResponse.TechBookDetail content = jpaQueryFactory.select(Projections.constructor(
				TechBookResponse.TechBookDetail.class,
				techBook.id,
				member.nickname,
				member.profileImageUrl,
				educationLevel.name,
				Expressions.constant(new ArrayList<String>()),
				techBook.title,
				techBook.description,
				techBook.introduction,
				Expressions.numberOperation(
					Double.class,
					Ops.DIV,
					techBook.totalRating.castToNum(Double.class),
					Expressions.numberTemplate(
						Double.class, "CASE WHEN {0} = 0 THEN 1.0 ELSE {0} END", techBook.totalReviewCount
					)
				),
				techBook.totalReviewCount,
				techBook.techBookUrl,
				techBook.techBookPreviewUrl,
				techBook.techBookThumbnailUrl,
				techBook.techBookPage,
				techBook.price,
				techBook.viewCount,
				JPAExpressions.select(like.count()).from(like).where(like.techBook.id.eq(techBookId)),
				memberId != null
					? JPAExpressions.select(like.count()).from(like)
					.where(like.techBook.id.eq(techBook.id).and(like.member.id.eq(memberId))).exists()
					: Expressions.constant(Boolean.FALSE),
				techBook.createdAt,
				memberId != null
					? JPAExpressions.select(orderHistory.count()).from(orderHistory)
					.join(orderHistory).on(orderHistory.orderCategory.id.eq(orderCategory.id))
					.where(orderHistory.customer.id.eq(memberId)
						.and(orderHistory.productId.eq(techBookId))
						.and(orderCategory.name.eq("TechBook")))
					.exists()
					: Expressions.constant(Boolean.FALSE)
			))
			.from(techBook)
			.join(member).on(techBook.writer.id.eq(member.id))
			.join(educationLevel).on(techBook.educationLevel.id.eq(educationLevel.id))
			.where(techBook.id.eq(techBookId))
			.fetchOne();

		if (content != null) {
			List<String> categoryList = jpaQueryFactory.select(educationCategory.name)
				.from(techBookEducationCategory)
				.join(educationCategory).on(techBookEducationCategory.educationCategory.id.eq(educationCategory.id))
				.where(techBookEducationCategory.techBook.id.eq(techBookId))
				.fetch();

			content.addEducationCategoryList(categoryList);
		}

		return content;
	}

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
			.where(getSearchCondition(keyword))
			.orderBy(getSortCondition(pageable))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		JPAQuery<Long> count = jpaQueryFactory.select(techBook.count())
			.from(techBook)
			.where(getSearchCondition(keyword));
		return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
	}

	/**
	 * 검색어(keyword)로 TechBook 페이징 목록 조회
	 *
	 * @param keyword     - 검색어
	 * @param memberId    - 회원 ID
	 * @param educationId - 교육 카테고리 ID
	 * @param pageable    - 페이징 객체
	 * @return Page<TechBookResponse.ListInfo>
	 */
	@Override
	public Page<TechBookResponse.ListInfo> findTechBooks(
		final String keyword, final String memberId, final Long educationId, final Pageable pageable
	) {
		JPAQuery<TechBookResponse.ListInfo> contentJpaQuery = jpaQueryFactory.select(Projections.constructor(
				TechBookResponse.ListInfo.class,
				techBook.id,
				member.nickname,
				member.profileImageUrl,
				educationId != null
					? educationCategory.name
					: Expressions.nullExpression(String.class),
				techBook.title,
				techBook.price,
				techBook.techBookThumbnailUrl,
				JPAExpressions.select(like.count()).from(like).where(like.techBook.id.eq(techBook.id)),
				techBook.createdAt,
				memberId != null
					? JPAExpressions.select(like.count()).from(like)
					.where(like.techBook.id.eq(techBook.id).and(like.member.id.eq(memberId))).exists()
					: Expressions.constant(Boolean.FALSE)
			)).from(techBook)
			.join(member).on(techBook.writer.id.eq(member.id))
			.where(getSearchCondition(keyword))
			.orderBy(getSortCondition(pageable))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		addJoinByEducationCategory(contentJpaQuery, educationId);

		List<TechBookResponse.ListInfo> content = contentJpaQuery.fetch();

		JPAQuery<Long> countJpaQuery = jpaQueryFactory.select(techBook.count())
			.from(techBook)
			.where(getSearchCondition(keyword));

		addJoinByEducationCategory(countJpaQuery, educationId);

		return PageableExecutionUtils.getPage(content, pageable, countJpaQuery::fetchOne);
	}

	/**
	 * 회원 ID를 기준으로 좋아요된 TechBook 페이징 목록 조회
	 *
	 * @param memberId - 회원 ID
	 * @param pageable - 페이징 객체
	 * @return Page<TechBookResponse.ListInfo>
	 */
	@Override
	public Page<TechBookResponse.ListInfo> findLikeTechBooks(final String memberId, final Pageable pageable) {
		List<TechBookResponse.ListInfo> content = jpaQueryFactory.select(Projections.constructor(
				TechBookResponse.ListInfo.class,
				techBook.id,
				member.id,
				member.profileImageUrl,
				Expressions.nullExpression(String.class),
				techBook.title,
				techBook.price,
				techBook.techBookThumbnailUrl,
				JPAExpressions.select(like.count()).from(like).where(like.techBook.id.eq(techBook.id)),
				techBook.createdAt,
				memberId != null
					? JPAExpressions.select(like.count()).from(like)
					.where(like.techBook.id.eq(techBook.id).and(like.member.id.eq(memberId))).exists()
					: Expressions.constant(Boolean.FALSE)
			)).from(techBook)
			.join(member).on(techBook.writer.id.eq(member.id))
			.join(like).on(like.techBook.id.eq(techBook.id))
			.where(like.member.id.eq(memberId))
			.orderBy(like.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()).fetch();

		JPAQuery<Long> countJpaQuery = jpaQueryFactory.select(techBook.count())
			.from(techBook)
			.join(like).on(like.techBook.id.eq(techBook.id))
			.where(like.member.id.eq(memberId));

		return PageableExecutionUtils.getPage(content, pageable, countJpaQuery::fetchOne);
	}

	/**
	 * 교육 카테고리 ID 유무에 따라 동적 쿼리 추가
	 *
	 * @param jpaQuery    - JPAQuery
	 * @param educationId - 교육 카테고리 ID
	 * @param <T>
	 */
	private <T> void addJoinByEducationCategory(final JPAQuery<T> jpaQuery, final Long educationId) {
		if (educationId != null) {
			jpaQuery.join(techBookEducationCategory)
				.on(techBook.id.eq(techBookEducationCategory.techBook.id)
					.and(techBookEducationCategory.educationCategory.id.eq(educationId)));
		}
	}

	/**
	 * 검색어(keyword) 기준 BooleanExpression 생성
	 *
	 * @param keyword  - 검색어
	 * @return BooleanExpression
	 */
	private BooleanExpression getSearchCondition(final String keyword) {
		if (!StringUtils.hasText(keyword))
			return null;
		return Expressions.numberTemplate(
			Double.class,
			"function('full_text_boolean_search_param_3', {0}, {1}, {2}, {3})",
			techBook.title,
			techBook.description,
			techBook.introduction,
			Expressions.constant(keyword)
		).gt(0);
	}

	/**
	 * 페이징 객체에 포함된 정렬 조건에 따라 OrderSpecifier[] 생성
	 *
	 * @param pageable - 페이징 객체
	 * @return OrderSpecifier[]
	 */
	private OrderSpecifier<?>[] getSortCondition(@NotNull final Pageable pageable) {
		List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
		boolean hasCreatedAt = pageable.getSort().stream().anyMatch(order -> "createdAt".equals(order.getProperty()));

		if (!pageable.getSort().isEmpty()) {
			pageable.getSort().forEach(order -> {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

				switch (order.getProperty()) {
					case "rating" -> orderSpecifiers.add(new OrderSpecifier(direction,
						Expressions.numberTemplate(Double.class, "CASE WHEN {1} = 0 THEN 0 ELSE ({0} * 1.0 / {1}) END",
							techBook.totalRating, techBook.totalReviewCount))
					);
					case "viewCount" -> orderSpecifiers.add(new OrderSpecifier(direction, techBook.viewCount));
					case "reviewCount" -> orderSpecifiers.add(new OrderSpecifier(direction, techBook.totalReviewCount));
					case "createdAt" -> orderSpecifiers.add(new OrderSpecifier(direction, techBook.createdAt));
					case "likeCount" -> orderSpecifiers.add(new OrderSpecifier(direction, techBook.likes.size()));
					default -> {
					}
				}
			});
		}

		if (!hasCreatedAt) {
			orderSpecifiers.add(new OrderSpecifier(Order.DESC, techBook.createdAt));
		}

		return orderSpecifiers.toArray(new OrderSpecifier[0]);
	}

}
