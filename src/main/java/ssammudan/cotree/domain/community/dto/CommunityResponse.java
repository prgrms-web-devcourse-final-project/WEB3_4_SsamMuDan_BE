package ssammudan.cotree.domain.community.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;

/**
 * PackageName : ssammudan.cotree.domain.community.dto
 * FileName    : CommunityResponse
 * Author      : Baekgwa
 * Date        : 2025-03-31
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-31     Baekgwa               Initial creation
 * 2025-04-07     Baekgwa               Thumbnail 이미지 출력 정상화
 */
public class CommunityResponse {

	private CommunityResponse() {
	}

	@Builder(access = AccessLevel.PRIVATE)
	public record BoardListDetail(Long id, String title, String author, LocalDateTime createdAt, String content,
								  Long commentCount, Long likeCount, Integer viewCount, String imageUrl,
								  Boolean isLike, Boolean isNew) {

		public static BoardListDetail modifyContent(BoardListDetail original, String newContent) {
			return BoardListDetail.builder()
				.id(original.id())
				.title(original.title())
				.author(original.author())
				.createdAt(original.createdAt())
				.content(newContent)
				.commentCount(original.commentCount())
				.likeCount(original.likeCount())
				.viewCount(original.viewCount())
				.imageUrl(original.imageUrl())
				.isLike(original.isLike())
				.isNew(original.isNew())
				.build();
		}
	}

	public record BoardDetail(String title, String author, LocalDateTime createdAt, String content, Long likeCount,
							  Integer viewCount, Boolean isLike, Boolean isOwner) {
	}

	public record BoardModify(Long boardId) {
	}

	public record BoardCreate(Long boardId) {
		public static BoardCreate of(Long boardId) {
			return new BoardCreate(boardId);
		}
	}
}
