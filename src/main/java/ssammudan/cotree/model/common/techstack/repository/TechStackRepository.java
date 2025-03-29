package ssammudan.cotree.model.common.techstack.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ssammudan.cotree.model.common.techstack.entity.TechStack;

/**
 * PackageName : ssammudan.cotree.model.common.techstack.repository
 * FileName    : TechStackRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface TechStackRepository extends JpaRepository<TechStack, Long> {

	@Query("""
			SELECT ts FROM TechStack ts
			WHERE ts.id IN :ids
			""")
	List<TechStack> findByIds(@Param("ids") Set<Long> ids);
}
