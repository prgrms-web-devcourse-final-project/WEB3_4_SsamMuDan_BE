package ssammudan.cotree.model.common.comment.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.comment.dto.CommentResponse;
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
	public Page<CommentResponse.CommentInfo> findCommentList(
			final Pageable pageable,
			final String memberId,
			final CommentCategory category,
			final Long itemId) {

		// 부모 댓글 조회용 where 조건 생성
		BooleanExpression whereCondition = createWhereCondition(category, itemId);

		// 부모 댓글 ID 조회 (페이징 적용)
		List<Long> parentCommentIdList = findParentCommentIdList(pageable, whereCondition);

		// 만약 부모 ID 들이 비어있다면, 바로 조회 결과 없음
		if (parentCommentIdList.isEmpty()) {
			return new PageImpl<>(List.of(), pageable, 0);
		}

		// 부모 댓글 및 대댓글 전체 조회
		List<CommentInfoProjection> allComment = findAllComment(parentCommentIdList, memberId);

		// 댓글 / 대댓글 정리
		List<CommentResponse.CommentInfo> response = buildCommentInfo(allComment);

		// 부모 댓글 개수 조회
		long total = countParentComment(whereCondition);

		return new PageImpl<>(response, pageable, total);
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

	private long countParentComment(BooleanExpression whereCondition) {
		return Optional.ofNullable(
				jpaQueryFactory
						.select(comment.count())
						.from(comment)
						.where(whereCondition)
						.fetchOne()
		).orElse(0L);
	}

	private List<CommentResponse.CommentInfo> buildCommentInfo(List<CommentInfoProjection> allCommentList) {
		// 대댓글을 부모 ID 기준으로 그룹화
		Map<Long, List<CommentResponse.ChildCommentInfo>> subCommentMap = allCommentList.stream()
				.filter(c -> c.getParentId() != null)
				.collect(Collectors.groupingBy(
						CommentInfoProjection::getParentId,
						Collectors.mapping(CommentResponse.ChildCommentInfo::of, Collectors.toList())
				));

		// 부모 댓글 리스트 생성
		List<CommentInfoProjection> parentCommentList = allCommentList.stream()
				.filter(c -> c.getParentId() == null)
				.toList();

		// 대댓글 최신순 정렬
		subCommentMap.forEach((parentId, subComments) ->
				subComments.sort(Comparator.comparing(CommentResponse.ChildCommentInfo::getCreatedAt).reversed())
		);

		// 최종 결과 빌드 & 리턴
		return parentCommentList.stream()
				.map(parent -> CommentResponse.CommentInfo.from(parent,
						subCommentMap.getOrDefault(parent.getId(), List.of())))
				.toList();
	}
}
