package ssammudan.cotree.domain.community.dto;

import java.time.LocalDateTime;

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
 */
public class CommunityResponse {

	private CommunityResponse() {
	}

	public record BoardListDetail(Long id, String title, String author, LocalDateTime createdAt, String content,
								  Long commentCount, Long likeCount, Integer viewCount, String imageUrl, Boolean isLike,
								  Boolean isNew) {
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
