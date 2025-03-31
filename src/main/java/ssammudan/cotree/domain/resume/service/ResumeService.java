package ssammudan.cotree.domain.resume.service;

import ssammudan.cotree.domain.resume.dto.ResumeCreateRequest;
import ssammudan.cotree.domain.resume.dto.ResumeCreateResponse;

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
}
