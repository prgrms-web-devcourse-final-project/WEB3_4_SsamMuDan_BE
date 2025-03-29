package ssammudan.cotree.model.common.techstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
