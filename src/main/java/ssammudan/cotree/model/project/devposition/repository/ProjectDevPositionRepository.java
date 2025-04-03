package ssammudan.cotree.model.project.devposition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;

/**
 * PackageName : ssammudan.cotree.model.project.devposition.repository
 * FileName    : ProjectDevPositionRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 * 2025-04-02    sangxxjin             get HotProject
 */
public interface ProjectDevPositionRepository extends JpaRepository<ProjectDevPosition, Long> {
	List<ProjectDevPosition> findByProjectId(Long id);
}
