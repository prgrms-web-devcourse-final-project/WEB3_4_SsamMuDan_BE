package ssammudan.cotree.model.common.comment.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.comment.type.CommentCategory;
import ssammudan.cotree.model.common.comment.entity.QComment;
import ssammudan.cotree.model.member.member.entity.QMember;

/**
 * PackageName : ssammudan.cotree.model.common.comment.repository
 * FileName    : CommentRepositoryImpl
 * Author      : Baekgwa
 * Date        : 2025-04-03
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-03     Baekgwa               Initial creation
 */
@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private static final QComment comment = QComment.comment;
	private static final QMember member = QMember.member;

	@Override
	public List<CommentInfoProjection> findCommentListByPaging(
		final Pageable pageable,
		final String memberId,
		final CommentCategory category,
		final Long itemId) {

		// 부모 댓글 조회용 where 조건 생성
		BooleanExpression whereCondition = createWhereCondition(category, itemId);

		// 부모 댓글 ID 조회 (페이징 적용)
		List<Long> parentCommentIdList = findParentCommentIdList(pageable, whereCondition);

		// 댓글이 없는 경우 빈 결과 반환
		if (parentCommentIdList.isEmpty()) {
			return List.of();
		}

		// 부모 댓글 및 대댓글 전체 조회
		return findAllComment(parentCommentIdList, memberId);
	}

	@Override
	public Long findCommentListCounts(
		final CommentCategory category,
		final Long itemId) {
		BooleanExpression whereCondition = createWhereCondition(category, itemId);

		return jpaQueryFactory
			.select(comment.count())
			.from(comment)
			.where(whereCondition)
			.fetchOne();
	}

	private BooleanExpression createWhereCondition(CommentCategory category, Long itemId) {
		return switch (category) {
			case COMMUNITY -> comment.parentComment.id.isNull().and(comment.community.id.eq(itemId));
			case RESUME -> comment.parentComment.id.isNull().and(comment.resume.id.eq(itemId));
		};
	}

	private List<Long> findParentCommentIdList(Pageable pageable, BooleanExpression whereCondition) {
		return jpaQueryFactory
			.select(comment.id)
			.from(comment)
			.where(whereCondition)
			.orderBy(comment.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private List<CommentInfoProjection> findAllComment(List<Long> parentCommentIds, String memberId) {
		return jpaQueryFactory
			.select(new QCommentInfoProjection(
				comment.id,
				comment.parentComment.id,
				member.profileImageUrl,
				member.nickname,
				comment.createdAt,
				comment.content,
				memberId != null ? (member.id.eq(memberId))
					: (Expressions.constant(Boolean.FALSE))
			))
			.from(comment)
			.join(member).on(comment.author.id.eq(member.id))
			.where(comment.id.in(parentCommentIds)
				.or(comment.parentComment.id.in(parentCommentIds)))
			.orderBy(comment.createdAt.desc())
			.fetch();
	}
}