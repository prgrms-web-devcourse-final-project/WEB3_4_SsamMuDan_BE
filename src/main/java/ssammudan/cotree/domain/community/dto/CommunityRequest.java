package ssammudan.cotree.domain.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class CreateBoard {
		@NotBlank(message = "글 카테고리는 필수값 입니다.")
		private String category;

		@Size(min = 1, max = 50, message = "제목은 1자 이상 50자 이하로 입력해주세요.")
		private String title;

		@Size(min = 1, max = 1000, message = "내용은 1자 이상 1000자 이하로 입력해주세요.")
		private String content;

		//todo : 관련 테스트 목적 생성된 생성자. 방법이 있다면 삭제 필요.
		public CreateBoard(String category, String title, String content) {
			this.category = category;
			this.title = title;
			this.content = content;
		}
	}
}
