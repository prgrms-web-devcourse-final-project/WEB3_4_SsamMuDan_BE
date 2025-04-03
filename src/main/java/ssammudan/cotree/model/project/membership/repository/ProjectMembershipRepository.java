package ssammudan.cotree.model.project.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.project.membership.entity.ProjectMembership;
import ssammudan.cotree.model.project.membership.type.ProjectMembershipStatus;

/**
 * PackageName : ssammudan.cotree.model.project.membership.repository
 * FileName    : ProjectMembershipRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface ProjectMembershipRepository extends JpaRepository<ProjectMembership, Long> {
	boolean existsByProjectIdAndMemberIdAndProjectMembershipStatus(Long projectId, String memberId,
		ProjectMembershipStatus projectMembershipStatus);
}
