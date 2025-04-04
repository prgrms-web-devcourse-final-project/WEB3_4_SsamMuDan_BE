package ssammudan.cotree.domain.community.dto;

import jakarta.validation.constraints.NotBlank;
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
 */
public class CommunityRequest {

	private CommunityRequest() {
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class CreateBoard {
		@NotBlank(message = "글 카테고리는 필수값 입니다.")
		private String category;

		@CommunityTitle
		private String title;

		@CommunityContent
		private String content;

		public CreateBoard(String category, String title, String content) {
			this.category = category;
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
