package ssammudan.cotree.domain.resume.service;

import java.util.List;
import java.util.Map;

import ssammudan.cotree.domain.resume.dto.ResumeCreateRequest;
import ssammudan.cotree.domain.resume.dto.ResumeCreateResponse;
import ssammudan.cotree.domain.resume.dto.ResumeDetailResponse;
import ssammudan.cotree.domain.resume.dto.ResumeResponse;
import ssammudan.cotree.domain.resume.dto.SearchResumeSort;
import ssammudan.cotree.global.response.PageResponse;

/**
 * PackageName : ssammudan.cotree.domain.resume.service
 * FileName    : ResumeService
 * Author      : kwak
 * Date        : 2025. 3. 28.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 28.     kwak               Initial creation
 */
public interface ResumeService {
	ResumeCreateResponse register(ResumeCreateRequest request, String memberId);

	ResumeDetailResponse detail(Long id);

	void bulkViewCount(Map<Long, Integer> viewCountData);

	PageResponse<ResumeResponse> getResumeList(int page, int size, List<Long> positionIds, List<Long> skillIds, Integer startYear, Integer endYear, SearchResumeSort sort);
}
