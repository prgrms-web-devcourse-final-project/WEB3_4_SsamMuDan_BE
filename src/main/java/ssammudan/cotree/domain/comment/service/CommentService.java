package ssammudan.cotree.domain.comment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
		@NonNull final CommentRequest.PostComment postComment,
		@NonNull final String memberId
	);

	PageResponse<CommentResponse.CommentInfo> getCommentList(
		@NonNull final Pageable pageable,
		@Nullable final String memberId,
		@NonNull final CommentCategory category,
		@NonNull final Long itemId
	);
}
