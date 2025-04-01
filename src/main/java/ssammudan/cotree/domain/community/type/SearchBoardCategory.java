package ssammudan.cotree.domain.community.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.domain.community.type
 * FileName    : SearchBoardCategory
 * Author      : Baekgwa
 * Date        : 2025-04-01
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-01     Baekgwa               Initial creation
 */
@Getter
@RequiredArgsConstructor
public enum SearchBoardCategory {
	TOTAL("전체"),
	CODE_REVIEW("코드 리뷰"),
	BOARD("게시판");

	private final String data;
}
