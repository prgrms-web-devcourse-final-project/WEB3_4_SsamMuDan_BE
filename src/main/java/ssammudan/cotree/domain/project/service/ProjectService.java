package ssammudan.cotree.domain.project.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import ssammudan.cotree.domain.project.dto.ProjectCreateRequest;
import ssammudan.cotree.domain.project.dto.ProjectCreateResponse;
import ssammudan.cotree.domain.project.dto.ProjectInfoResponse;
import ssammudan.cotree.domain.project.dto.ProjectListResponse;
import ssammudan.cotree.global.response.PageResponse;

/**
 * PackageName : ssammudan.cotree.domain.project.service
 * FileName    : ProjectService
 * Author      : sangxxjin
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-02.   sangxxjin             Initial creation
 * 2025-04-02    sangxxjin             get HotProject
 */

public interface ProjectService {
	ProjectCreateResponse create(ProjectCreateRequest request, MultipartFile file, String memberId);

	PageResponse<ProjectListResponse> getHotProjectsForMain(Pageable pageable);

	List<ProjectListResponse> getHotProjectsForProject();

	ProjectInfoResponse getProjectInfo(Long projectId, String memberId);

	PageResponse<ProjectListResponse> getProjects(Pageable pageable, List<Long> techStackIds,
		List<Long> devPositionIds,
		String sort);

	void updateRecruitmentStatus(Long projectId, String memberId);

	void applyForProject(Long projectId, String memberId);
}
