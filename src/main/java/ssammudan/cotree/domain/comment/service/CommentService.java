package ssammudan.cotree.domain.comment.service;

import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.comment.dto.CommentRequest;
import ssammudan.cotree.domain.comment.dto.CommentResponse;
import ssammudan.cotree.domain.comment.type.CommentCategory;
import ssammudan.cotree.global.response.PageResponse;

/**
 * PackageName : ssammudan.cotree.domain.comment.service
 * FileName    : CommentService
 * Author      : Baekgwa
 * Date        : 2025-04-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-02     Baekgwa               Initial creation
 */
public interface CommentService {

	void postNewComment(
			final CommentRequest.PostComment postComment, final String memberId);

	PageResponse<CommentResponse.CommentInfo> getCommentList(
			final Pageable pageable, final String memberId, final CommentCategory category, final Long itemId);
}
