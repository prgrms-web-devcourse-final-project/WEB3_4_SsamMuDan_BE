package ssammudan.cotree.model.common.developmentposition.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

	@Query("""
			SELECT dp FROM DevelopmentPosition dp
			WHERE dp.id IN :ids
			""")
	List<DevelopmentPosition> findByIds(@Param("ids") Set<Long> ids);
}
