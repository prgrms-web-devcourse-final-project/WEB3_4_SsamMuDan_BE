package ssammudan.cotree.model.project.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.project.membership.entity.ProjectMembership;

/**
 * PackageName : ssammudan.cotree.model.project.membership.repository
 * FileName    : ProjectMembershipRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : ProjectMemberShipRepository
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 * 2025-04-08     sangxxjin             참가신청 기록 확인
 */
public interface ProjectMembershipRepository
	extends JpaRepository<ProjectMembership, Long>, ProjectMembershipRepositoryCustom {

	boolean existsByProjectIdAndMemberId(Long projectId, String memberId);
}
