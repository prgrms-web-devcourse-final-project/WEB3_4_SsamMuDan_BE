package ssammudan.cotree.model.community.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ssammudan.cotree.model.community.community.entity.Community;

/**
 * PackageName : ssammudan.cotree.model.community.community.repository
 * FileName    : CommunityRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface CommunityRepository extends JpaRepository<Community, Long>, CommunityRepositoryCustom {

	// 성능 비교 필요.
	// @Query("SELECT EXISTS (SELECT 1 FROM Community c WHERE c.member.id = :memberId AND c.id = :boardId)")
	@Query("SELECT COUNT(c) > 0 FROM Community c WHERE c.member.id = :memberId AND c.id = :boardId")
	boolean isBoardAuthor(@Param("memberId") String memberId, @Param("boardId") Long boardId);
}
