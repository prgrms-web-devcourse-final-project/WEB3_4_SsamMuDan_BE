package ssammudan.cotree.domain.project.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ssammudan.cotree.domain.project.dto.HotProjectResponse;
import ssammudan.cotree.domain.project.dto.ProjectCreateRequest;
import ssammudan.cotree.domain.project.dto.ProjectCreateResponse;

/**
 * PackageName : ssammudan.cotree.domain.project.service
 * FileName    : ProjectService
 * Author      : sangxxjin
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     sangxxjin               Initial creation
 */

public interface ProjectService {
	ProjectCreateResponse create(ProjectCreateRequest request, MultipartFile file, String memberId);

	List<HotProjectResponse> getHotProjects();
}
