package ssammudan.cotree.model.project.techstack.repository;

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
 */
public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {
}
