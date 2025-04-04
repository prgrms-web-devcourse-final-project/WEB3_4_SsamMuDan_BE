package ssammudan.cotree.model.common.comment.repository;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

/**
 * PackageName : ssammudan.cotree.model.common.comment.repository
 * FileName    : ChildCommentInfo
 * Author      : Baekgwa
 * Date        : 2025-04-03
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-03     Baekgwa               Initial creation
 */
@Getter
public class CommentInfoProjection {
	private final Long id;
	private final Long parentId;
	private final String profileImageUrl;
	private final String author;
	private final LocalDateTime createdAt;
	private final String content;
	private final Boolean isAuthor;

	@QueryProjection
	public CommentInfoProjection(
			Long id,
			Long parentId,
			String profileImageUrl,
			String author,
			LocalDateTime createdAt,
			String content,
			Boolean isAuthor) {
		this.id = id;
		this.parentId = parentId;
		this.profileImageUrl = profileImageUrl;
		this.author = author;
		this.createdAt = createdAt;
		this.content = content;
		this.isAuthor = isAuthor;
	}
}
