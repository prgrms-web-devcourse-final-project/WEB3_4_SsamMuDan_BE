package ssammudan.cotree.model.project.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	Page<Project> findByFilters(Pageable pageable, List<Long> techStackIds, List<Long> devPositionIds, String sort);
}
