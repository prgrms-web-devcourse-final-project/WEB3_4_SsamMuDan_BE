package ssammudan.cotree.model.common.like.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.common.like.entity.Like;

/**
 * PackageName : ssammudan.cotree.model.common.like.repository
 * FileName    : LikeRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {

	Optional<Like> findByMember_IdAndTechTube_Id(String memberId, Long techTubeId);

	Optional<Like> findByMember_IdAndTechBook_Id(String memberId, Long techBookId);

	Optional<Like> findByMember_IdAndProject_Id(String memberId, Long projectId);

	Optional<Like> findByMember_IdAndCommunity_Id(String memberId, Long communityId);

	boolean existsByMember_IdAndTechTube_Id(String memberId, Long techTubeId);

	boolean existsByMember_IdAndTechBook_Id(String memberId, Long techBookId);

	boolean existsByMember_IdAndProject_Id(String memberId, Long projectId);

	boolean existsByMember_IdAndCommunity_Id(String memberId, Long communityId);

	long countByTechTube_Id(Long techTubeId);

	long countByTechBook_Id(Long techBookId);

	long countByProject_Id(Long projectId);

	long countByCommunity_Id(Long communityId);

}
