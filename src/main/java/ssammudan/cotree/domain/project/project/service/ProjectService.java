package ssammudan.cotree.domain.project.project.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import ssammudan.cotree.domain.project.project.dto.ProjectCreateRequest;
import ssammudan.cotree.domain.project.project.dto.ProjectCreateResponse;
import ssammudan.cotree.domain.project.project.dto.ProjectInfoResponse;
import ssammudan.cotree.domain.project.project.dto.ProjectLikeListResponse;
import ssammudan.cotree.domain.project.project.dto.ProjectListResponse;
import ssammudan.cotree.domain.project.project.dto.UpdateProjectPositionRequest;
import ssammudan.cotree.domain.project.project.type.SearchProjectSort;
import ssammudan.cotree.global.response.PageResponse;

/**
 * PackageName : ssammudan.cotree.domain.project.service
 * FileName    : ProjectService
 * Author      : sangxxjin
 * Date        : 2025. 4. 2.
 * Description : ProjectService
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-02.   sangxxjin             Initial creation
 * 2025-04-02.   sangxxjin             get HotProject
 * 2025-04-07.   sangxxjin             get Projects, updateRecruitmentStatus
 * 2024-04-11.   sangxxjin             get LikeProjects
 */

public interface ProjectService {
	ProjectCreateResponse create(ProjectCreateRequest request, MultipartFile file, String memberId);

	PageResponse<ProjectListResponse> getHotProjectsForMain(Pageable pageable);

	List<ProjectListResponse> getHotProjectsForProject();

	ProjectInfoResponse getProjectInfo(Long projectId, String memberId);

	PageResponse<ProjectListResponse> getProjects(Pageable pageable, List<Long> techStackIds,
		List<Long> devPositionIds,
		SearchProjectSort sort);

	void updateRecruitmentStatus(Long projectId, String memberId);

	void updateProjectPositionAmounts(Long projectId, String memberId, List<UpdateProjectPositionRequest> requests);

	PageResponse<ProjectLikeListResponse> getLikeProjects(Pageable pageable, String memberId);
}
