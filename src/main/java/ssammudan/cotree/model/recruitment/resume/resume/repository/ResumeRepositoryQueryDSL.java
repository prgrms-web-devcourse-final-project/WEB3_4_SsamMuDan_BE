package ssammudan.cotree.model.recruitment.resume.resume.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.resume.dto.ResumeResponse;
import ssammudan.cotree.domain.resume.dto.SearchResumeSort;

/**
 * PackageName : ssammudan.cotree.model.recruitment.resume.resume.repository
 * FileName    : ResumeRepositoryQueryDSL
 * Author      : kwak
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     kwak               Initial creation
 */
public interface ResumeRepositoryQueryDSL {

	Page<ResumeResponse> getResumeList(Pageable pageable, List<Long> positionIds, List<Long> skillIds,
		Integer startYear, Integer endYear, SearchResumeSort sort);
}
