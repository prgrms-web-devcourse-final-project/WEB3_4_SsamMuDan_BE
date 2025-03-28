package ssammudan.cotree.domain.community.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
	@ToString
	public static class CreateBoard {
		private String category;
		private String title;
		private String content;
	}
}
