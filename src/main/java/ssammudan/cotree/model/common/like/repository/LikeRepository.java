package ssammudan.cotree.model.common.like.repository;

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
public interface LikeRepository extends JpaRepository<Like, Long> {
}
