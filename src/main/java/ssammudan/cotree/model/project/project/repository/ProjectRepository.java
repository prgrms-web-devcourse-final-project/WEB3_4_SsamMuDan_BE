package ssammudan.cotree.model.project.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * 2025-04-02    sangxxjin             get HotProject
 */
public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
	List<Project> findTop2ByIsOpenTrueOrderByViewCountDescCreatedAtDesc();

	Page<Project> findByIsOpenTrue(Pageable pageable);
}
