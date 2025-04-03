package ssammudan.cotree.model.project.techstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.project.techstack.entity.ProjectTechStack;

/**
 * PackageName : ssammudan.cotree.model.project.techstack.repository
 * FileName    : ProjectTechStackRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 * 2025-04-02    sangxxjin             get HotProject
 */
public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {
	List<ProjectTechStack> findByProjectId(Long id);
}
