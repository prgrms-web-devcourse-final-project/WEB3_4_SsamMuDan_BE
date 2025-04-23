package ssammudan.cotree.model.common.comment.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.comment.type.CommentCategory;

/**
 * PackageName : ssammudan.cotree.model.common.comment.repository
 * FileName    : CommentRepositoryCustom
 * Author      : Baekgwa
 * Date        : 2025-04-03
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-03     Baekgwa               Initial creation
 */
public interface CommentRepositoryCustom {
	List<CommentInfoProjection> findCommentListByPaging(
		final Pageable pageable,
		final String memberId,
		final CommentCategory category,
		final Long itemId);

	Long findCommentListCounts(
		final CommentCategory category,
		final Long itemId);
}
