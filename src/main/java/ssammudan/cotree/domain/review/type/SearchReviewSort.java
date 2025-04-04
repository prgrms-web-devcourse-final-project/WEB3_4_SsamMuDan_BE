package ssammudan.cotree.domain.review.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.domain.review.type
 * FileName    : SearchReviewSort
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : 교육 컨텐츠 리뷰 정렬 조건 ENUM
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SearchReviewSort {

	LATEST("createdAt"),
	RATING("rating");

	private final String value;

}
