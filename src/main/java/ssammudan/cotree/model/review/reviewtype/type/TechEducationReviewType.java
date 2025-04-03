package ssammudan.cotree.model.review.reviewtype.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;

/**
 * PackageName : ssammudan.cotree.model.review.reviewtype.type
 * FileName    : TechEducationReviewType
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : TechEducation 리뷰 타입 ENUM
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TechEducationReviewType {

	TECH_TUBE(1), TECH_BOOK(2);

	private final long id;

	public static Long getTechEducationTypeId(final TechEducationReviewType techEducationType) {
		if (techEducationType == null) {
			throw new GlobalException(ErrorCode.INVALID_INPUT_VALUE);
		}
		return techEducationType.getId();
	}

	public static TechEducationReviewType getTechEducationType(final long id) {
		for (TechEducationReviewType techEducationType : TechEducationReviewType.values()) {
			if (techEducationType.getId() == id) {
				return techEducationType;
			}
		}
		throw new GlobalException(ErrorCode.INVALID_INPUT_VALUE);
	}

}
