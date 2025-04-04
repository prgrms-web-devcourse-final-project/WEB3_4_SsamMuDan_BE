package ssammudan.cotree.model.common.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.comment.dto.CommentResponse;
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
	Page<CommentResponse.CommentInfo> findCommentList(
			final Pageable pageable,
			final String memberId,
			final CommentCategory category,
			final Long itemId);
}
