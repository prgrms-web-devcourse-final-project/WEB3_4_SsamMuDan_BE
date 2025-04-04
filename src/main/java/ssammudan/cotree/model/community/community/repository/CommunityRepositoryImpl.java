package ssammudan.cotree.model.community.community.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.domain.community.type.SearchBoardCategory;
import ssammudan.cotree.domain.community.type.SearchBoardSort;
import ssammudan.cotree.model.common.comment.entity.QComment;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.community.category.entity.QCommunityCategory;
import ssammudan.cotree.model.community.community.entity.QCommunity;
import ssammudan.cotree.model.member.member.entity.QMember;

/**
 * PackageName : ssammudan.cotree.model.community.community.repository
 * FileName    : CommunityRepositoryImpl
 * Author      : Baekgwa
 * Date        : 2025-04-01
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-01     Baekgwa               Initial creation
 */
@Repository
@RequiredArgsConstructor
public class CommunityRepositoryImpl implements CommunityRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private static final QCommunity community = QCommunity.community;
	private static final QCommunityCategory communityCategory = QCommunityCategory.communityCategory;
	private static final QMember member = QMember.member;
	private static final QLike like = QLike.like;
	private static final QComment comment = QComment.comment;

	@Override
	public Page<CommunityResponse.BoardListDetail> findBoardList(
		final Pageable pageable,
		final SearchBoardSort sort,
		final SearchBoardCategory category,
		final String keyword,
		final String memberId) {

		OrderSpecifier<?> orderSpecifier = createOrderSpecifier(sort);
		BooleanBuilder whereCondition = createWhereCondition(category, keyword);
		LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

		// 쿼리 실행 - 데이터 조회
		List<CommunityResponse.BoardListDetail> content = jpaQueryFactory
			.select(Projections.constructor(CommunityResponse.BoardListDetail.class,
				community.id,
				community.title,
				member.nickname,
				community.createdAt,
				community.content,
				JPAExpressions
					.select(comment.count())
					.from(comment)
					.where(comment.community.id.eq(community.id)),
				JPAExpressions
					.select(like.count())
					.from(like)
					.where(like.community.id.eq(community.id)),
				community.viewCount,
				community.title,     // todo : 대표 이미지 그냥 필드로 가지고 있는게 간단할듯
				memberId != null ? JPAExpressions
					.select(like.count())
					.from(like)
					.where(like.community.id.eq(community.id)
						.and(like.member.id.eq(memberId)))
					.exists()
					: Expressions.constant(Boolean.FALSE),
				Expressions.booleanTemplate("{0} >= {1}", community.createdAt, oneDayAgo)
			))
			.from(community)
			.join(member).on(community.member.id.eq(member.id))
			.join(communityCategory).on(community.communityCategory.id.eq(communityCategory.id))
			.where(whereCondition)
			.orderBy(orderSpecifier)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 전체 개수 조회
		Long total = jpaQueryFactory
			.select(community.count())
			.from(community)
			.join(communityCategory).on(community.communityCategory.id.eq(communityCategory.id))
			.where(whereCondition)
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}

	@Override
	public Optional<CommunityResponse.BoardDetail> findBoard(final Long boardId, final String memberId) {
		return Optional.ofNullable(jpaQueryFactory
			.select(Projections.constructor(CommunityResponse.BoardDetail.class,
				community.title,
				member.nickname,
				community.createdAt,
				community.content,
				JPAExpressions
					.select(like.count())
					.from(like)
					.where(like.community.id.eq(community.id)),
				community.viewCount,
				memberId != null ? JPAExpressions
					.select(like.count())
					.from(like)
					.where(like.community.id.eq(community.id)
						.and(like.member.id.eq(memberId)))
					.exists()
					: Expressions.constant(Boolean.FALSE),
				memberId != null ? Expressions.booleanOperation(
					Ops.EQ,
					community.member.id,
					Expressions.constant(memberId)
				) : Expressions.constant(Boolean.FALSE)
			))
			.from(community)
			.join(member).on(community.member.id.eq(member.id))
			.where(community.id.eq(boardId))
			.fetchOne());
	}

	/**
	 * 정렬 조건 생성
	 */
	private OrderSpecifier<?> createOrderSpecifier(SearchBoardSort sort) {
		return switch (sort) {
			case LATEST -> new OrderSpecifier<>(Order.DESC, community.createdAt);
			case COMMENT -> new OrderSpecifier<>(Order.DESC,
				JPAExpressions.select(comment.count())
					.from(comment)
					.where(comment.community.id.eq(community.id))
			);
			case LIKE -> new OrderSpecifier<>(Order.DESC,
				JPAExpressions.select(like.count())
					.from(like)
					.where(like.community.id.eq(community.id))
			);
		};
	}

	/**
	 * 검색 조건 생성
	 */
	private BooleanBuilder createWhereCondition(SearchBoardCategory category, String keyword) {
		BooleanBuilder builder = new BooleanBuilder();

		// 카테고리 조건
		if (category != SearchBoardCategory.TOTAL) {
			builder.and(communityCategory.name.eq(category.getData()));
		}

		// 키워드 검색 조건
		if (keyword != null && !keyword.isEmpty()) {
			builder.and(
				community.title.containsIgnoreCase(keyword)
					.or(community.content.containsIgnoreCase(keyword))
			);
		}

		return builder;
	}
}
