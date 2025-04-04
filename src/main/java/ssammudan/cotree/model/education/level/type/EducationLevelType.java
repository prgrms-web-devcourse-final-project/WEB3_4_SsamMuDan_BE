package ssammudan.cotree.model.education.level.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.education.level.type
 * FileName    : EducationLevelType
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : 학습 난이도 타입 ENUM
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum EducationLevelType {

	BEGINNER(1), INTERMEDIATE(2), ADVANCED(3);

	private final long id;

}
