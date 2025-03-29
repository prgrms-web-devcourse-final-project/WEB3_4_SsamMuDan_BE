package ssammudan.cotree.model.recruitment.career.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.recruitment.career.career.entity.Career;

/**
 * PackageName : ssammudan.cotree.model.recruitment.career.career.repository
 * FileName    : CareerTechStackRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface CareerRepository extends JpaRepository<Career, Long> {
}
