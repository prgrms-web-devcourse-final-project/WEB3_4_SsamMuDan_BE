package ssammudan.cotree.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.domain.comment.type.CommentCategory;

/**
 * PackageName : ssammudan.cotree.domain.comment.dto
 * FileName    : CommentRequest
 * Author      : Baekgwa
 * Date        : 2025-04-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-02     Baekgwa               Initial creation
 */
public class CommentRequest {

	private CommentRequest() {
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class PostComment {
		@NotBlank(message = "댓글 내용은 필수 입니다.")
		private String content;

		@NotNull(message = "댓글 카테고리는 필수 입니다.")
		@Schema(example = "RESUME")
		private CommentCategory category;

		@NotNull(message = "댓글을 달 Item 의 id 값은 필수입니다.")
		private Long whereId;

		private Long commentId;
	}
}
