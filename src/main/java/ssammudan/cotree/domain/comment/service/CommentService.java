package ssammudan.cotree.domain.comment.service;

import ssammudan.cotree.domain.comment.dto.CommentRequest;

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

	void postNewComment(final CommentRequest.PostComment postComment, final String memberId);
}
