package ssammudan.cotree.model.common.developmentposition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;

/**
 * PackageName : ssammudan.cotree.model.common.developmentposition.repository
 * FileName    : DevelopmentPositionRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface DevelopmentPositionRepository extends JpaRepository<DevelopmentPosition, Long> {
}
