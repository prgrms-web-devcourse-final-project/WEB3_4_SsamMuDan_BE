package ssammudan.cotree.model.community.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.community.category.entity.CommunityCategory;

/**
 * PackageName : ssammudan.cotree.model.community.category.repository
 * FileName    : CommunityCategoryRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface CommunityCategoryRepository extends JpaRepository<CommunityCategory, Long> {

	Optional<CommunityCategory> findByName(final String name);
}
