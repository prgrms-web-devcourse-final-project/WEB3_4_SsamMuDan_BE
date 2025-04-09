package ssammudan.cotree.infra.viewcount.constant;

/**
 * PackageName : ssammudan.cotree.infra.viewcount
 * FileName    : ViewCountConstant
 * Author      : Baekgwa
 * Date        : 2025-04-08
 * Description : ViewCount 증가 스케줄러 용 상수
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-08     Baekgwa               Initial creation
 */
public final class ViewCountConstant {

	private ViewCountConstant() {
	}

	// Project
	public static final String VIEW_COUNT_PROJECT_KEY_PREFIX = "project:viewCount:";
	public static final String VIEW_COUNT_PROJECT_DIRTY_SET = "project:viewCount:dirty";
	public static final String PROJECT_TABLE_NAME = "project";

	// Resume
	public static final String VIEW_COUNT_RESUME_KEY_PREFIX = "resume:viewCount:";
	public static final String VIEW_COUNT_RESUME_DIRTY_SET = "resume:viewCount:dirty";
	public static final String RESUME_TABLE_NAME = "resume";

	// Education - TechTube
	public static final String VIEW_COUNT_TECH_TUBE_KEY_PREFIX = "techTube:viewCount:";
	public static final String VIEW_COUNT_TECH_TUBE_DIRTY_SET = "techTube:viewCount:dirty";
	public static final String TECH_TUBE_TABLE_NAME = "tech_tube";

	// Education - TechBook
	public static final String VIEW_COUNT_TECH_BOOK_KEY_PREFIX = "techBook:viewCount:";
	public static final String VIEW_COUNT_TECH_BOOK_DIRTY_SET = "techBook:viewCount:dirty";
	public static final String TECH_BOOK_TABLE_NAME = "tech_book";

	// Community
	public static final String VIEW_COUNT_COMMUNITY_KEY_PREFIX = "community:viewCount:";
	public static final String VIEW_COUNT_COMMUNITY_DIRTY_SET = "community:viewCount:dirty";
	public static final String COMMUNITY_TABLE_NAME = "community";
}
