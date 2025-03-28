package ssammudan.cotree.model.community.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.community.community.entity.Community;

/**
 * PackageName : ssammudan.cotree.model.community.community.repository
 * FileName    : CommunityRepository
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : Community table persistence layer
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28     Baekgwa               Initial creation
 */
public interface CommunityRepository extends JpaRepository<Community, Long> {
}
