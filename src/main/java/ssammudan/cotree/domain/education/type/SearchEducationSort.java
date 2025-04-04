package ssammudan.cotree.domain.education.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.domain.education.type
 * FileName    : SearchEducationSort
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : 교육 컨텐츠 정렬 조건 ENUM
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SearchEducationSort {

	LATEST("createdAt"),
	RATING("rating"),
	LIKES("likeCount"),
	VIEWS("viewCount"),
	REVIEWS("reviewCount");

	private final String value;

}
