package ssammudan.cotree.model.project.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

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
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
	List<Project> findTop2ByIsOpenTrueOrderByViewCountDescCreatedAtDesc();
}
