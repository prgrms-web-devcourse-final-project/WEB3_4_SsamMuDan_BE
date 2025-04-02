package ssammudan.cotree.domain.comment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.comment.dto.CommentRequest;
import ssammudan.cotree.domain.comment.service.CommentService;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.comment.controller
 * FileName    : CommentController
 * Author      : Baekgwa
 * Date        : 2025-04-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-02     Baekgwa               Initial creation
 */
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "댓글 관련 API")
public class CommentController {
	private final CommentService commentService;

	@PostMapping
	@Operation(summary = "댓글/대댓글 작성")
	public BaseResponse<Void> postNewComment(
			@Valid @RequestBody CommentRequest.PostComment postComment,
			@AuthenticationPrincipal CustomUser customUser
	) {
		String memberId = customUser.getId();
		commentService.postNewComment(postComment, memberId);
		return BaseResponse.success(SuccessCode.COMMENT_CREATE_SUCCESS);
	}
}
