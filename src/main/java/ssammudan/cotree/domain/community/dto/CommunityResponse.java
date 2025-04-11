package ssammudan.cotree.domain.community.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.With;

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
 * 2025-04-08     Baekgwa               목록, 상세 조회 시, 작성자의 프로필 이미지 return 추가
 * 2025-04-11     Baekgwa               내가 좋아요 (관심)한, Community 목록 조회 기능 추가
 */
public class CommunityResponse {

	private CommunityResponse() {
	}

	@With
	@Builder(access = AccessLevel.PRIVATE)
	public record BoardListDetail(Long id, String title, String author, LocalDateTime createdAt, String content,
								  Long commentCount, Long likeCount, Integer viewCount, String imageUrl,
								  Boolean isLike, Boolean isNew, String profileImage) {
	}

	public record BoardDetail(
		String title,
		String author,
		LocalDateTime createdAt,
		String content,
		Long likeCount,
		Integer viewCount,
		Boolean isLike,
		Boolean isOwner,
		String profileImage
	) {
	}

	public record BoardModify(Long boardId) {
	}

	public record BoardCreate(Long boardId) {
		public static BoardCreate of(Long boardId) {
			return new BoardCreate(boardId);
		}
	}

	public record BoardLikeListDetail(Long id, String title, String author, LocalDateTime createdAt, String content, String thumbnailImage) {
	}
}
