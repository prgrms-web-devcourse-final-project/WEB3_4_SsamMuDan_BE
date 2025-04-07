package ssammudan.cotree.model.project.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ssammudan.cotree.model.project.project.entity.Project;

/**
 * PackageName : ssammudan.cotree.model.project.project.repository
 * FileName    : ProjectRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 * 2025-04-02    sangxxjin             get HotProject
 */
public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {

	@Transactional
	@Modifying
	@Query("UPDATE Project p SET p.viewCount = p.viewCount + :viewCount WHERE p.id = :projectId")
	void incrementViewCount(@Param("projectId") Long projectId, @Param("viewCount") int viewCount);
}
