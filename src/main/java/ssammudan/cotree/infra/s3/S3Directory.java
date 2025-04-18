package ssammudan.cotree.infra.s3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.infra.s3
 * FileName    : S3Directory
 * Author      : Baekgwa
 * Date        : 2025-03-31
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-31     Baekgwa               Initial creation
 */
@Getter
@RequiredArgsConstructor
public enum S3Directory {
	USER_PROFILE("user/profile/", false),

	USER_RESUME("resume/", false),

	COMMUNITY_BOARD("community/board/", true),

	PROJECT("project/team/", false),

	EDUCATION_TECHBOOK_MAIN("education/techbook/main/", false),
	EDUCATION_TECHBOOK_PREVIEW("education/techbook/preview/", false),
	EDUCATION_TECHBOOK_THUMBNAIL("education/techbook/thumbnail/", false),

	EDUCATION_TECHTUBE_MAIN("education/techtube/main/", false),
	EDUCATION_TECHTUBE_THUMBNAIL("education/techtube/thumbnail/", false),

	TECHSTACK("techstack/", false);
	private final String path;
	private final boolean isMultiFile;
}
