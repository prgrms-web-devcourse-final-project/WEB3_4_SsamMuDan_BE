package ssammudan.cotree.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
		@Size(min = 1, max = 1000, message = "내용은 1자 이상 1000자 이하로 입력해주세요.")
		private String content;

		@NotNull(message = "댓글 카테고리는 필수 입니다.")
		@Schema(example = "RESUME")
		private CommentCategory category;

		@NotNull(message = "댓글을 달 Item 의 id 값은 필수입니다.")
		private Long whereId;

		private Long commentId;
	}
}
