package ssammudan.cotree.domain.community.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.domain.community.dto.valid.CommunityContent;
import ssammudan.cotree.domain.community.dto.valid.CommunityTitle;

/**
 * PackageName : ssammudan.cotree.domain.community.dto
 * FileName    : CommunityRequest
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : Community Domain Request Data
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28     Baekgwa               Initial creation
 * 2025-04-08     Baekgwa               커뮤니티 글 작성 시, community category 입력 형식 변경. 기존 : String / 변경 : Long id
 */
public class CommunityRequest {

	private CommunityRequest() {
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class CreateBoard {
		private Long communityCategoryId;

		@CommunityTitle
		private String title;

		@CommunityContent
		private String content;

		public CreateBoard(Long communityCategoryId, String title, String content) {
			this.communityCategoryId = communityCategoryId;
			this.title = title;
			this.content = content;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ModifyBoard {
		@CommunityTitle
		private String title;

		@CommunityContent
		private String content;
	}
}
