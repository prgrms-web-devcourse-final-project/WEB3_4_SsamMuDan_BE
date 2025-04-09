package ssammudan.cotree.model.recruitment.resume.resume.repository;

import java.util.Map;

/**
 * PackageName : ssammudan.cotree.model.recruitment.resume.resume.repository
 * FileName    : ResumeRepositoryCustom
 * Author      : kwak
 * Date        : 2025. 4. 1.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 1.     kwak               Initial creation
 * 2025. 4. 9.     Baekgwa             ViewCount 증가 `ViewCountStore`, `ViewCountScheduler` 에서 통합 관리 진행
 */
public interface ResumeRepositoryJdbc {

	@Deprecated(since = "2025-04-09", forRemoval = true)
	void bulkUpdateViewCount(Map<Long, Integer> viewCountData);
}
