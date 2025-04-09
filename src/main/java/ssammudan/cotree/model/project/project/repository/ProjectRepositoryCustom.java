package ssammudan.cotree.model.project.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.project.dto.ProjectListResponse;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.project.entity.Project;

/**
 * PackageName : ssammudan.cotree.model.project.project.repository
 * FileName    : ProjectRepositoryCustom
 * Author      : sangxxjin
 * Date        : 2025. 4. 4.
 * Description : CustomRepository
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 4.     sangxxjin               Initial creation
 */
public interface ProjectRepositoryCustom {
	Optional<Project> fetchProjectDetailById(Long projectId);

	Optional<Project> fetchProjectDetailById(Long projectId, String memberId);

	Page<ProjectListResponse> findHotProjectsForMain(Pageable pageable);

	List<ProjectListResponse> findHotProjectsForProject(int limit);

	Page<ProjectListResponse> findByFilters(Pageable pageable, List<Long> techStackIds,
		List<Long> devPositionIds, String sort);

	List<ProjectDevPosition> findAllByProjectId(Long projectId);
}
