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
 */
public interface ResumeRepositoryJdbc {

	void bulkUpdateViewCount(Map<Long, Integer> viewCountData);
}
