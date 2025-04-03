package ssammudan.cotree.domain.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ssammudan.cotree.model.common.comment.repository.CommentInfoProjection;

/**
 * PackageName : ssammudan.cotree.domain.comment.dto
 * FileName    : CommentResponse
 * Author      : Baekgwa
 * Date        : 2025-04-03
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-03     Baekgwa               Initial creation
 */
public class CommentResponse {

	private CommentResponse() {
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class CommentInfo {
		private final Long id;
		private final String profileImageUrl;
		private final String author;
		private final LocalDateTime createdAt;
		private final String content;
		private final Boolean isAuthor;
		private final List<ChildCommentInfo> subComment;

		public static CommentInfo from(CommentInfoProjection commentInfo, List<ChildCommentInfo> subComment) {
			return CommentInfo
					.builder()
					.id(commentInfo.getId())
					.profileImageUrl(commentInfo.getProfileImageUrl())
					.author(commentInfo.getAuthor())
					.createdAt(commentInfo.getCreatedAt())
					.content(commentInfo.getContent())
					.isAuthor(commentInfo.getIsAuthor())
					.subComment(subComment)
					.build();
		}
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ChildCommentInfo {
		private final Long id;
		private final String profileImageUrl;
		private final String author;
		private final LocalDateTime createdAt;
		private final String content;
		private final Boolean isAuthor;

		public static ChildCommentInfo of(CommentInfoProjection commentInfoProjection) {
			return ChildCommentInfo
					.builder()
					.id(commentInfoProjection.getId())
					.profileImageUrl(commentInfoProjection.getProfileImageUrl())
					.author(commentInfoProjection.getAuthor())
					.createdAt(commentInfoProjection.getCreatedAt())
					.content(commentInfoProjection.getContent())
					.isAuthor(commentInfoProjection.getIsAuthor())
					.build();
		}
	}
}
