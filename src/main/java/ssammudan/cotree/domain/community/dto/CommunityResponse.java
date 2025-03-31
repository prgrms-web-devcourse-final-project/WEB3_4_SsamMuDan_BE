package ssammudan.cotree.domain.community.dto;

import org.springframework.data.domain.Page;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

	@Getter
	public static class BoardList {
		Page<BoardListDetail> boardItems;
	}

	@Getter
	@Builder
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class BoardListDetail {
		private final String title;
		private final String author;
		private final String createdAt;
		private final String content;
		private final Integer commentCount;
		private final Integer likeCount;
		private final Integer viewCount;
		private final String imageUrl;
		private final String isLike;
	}
}
