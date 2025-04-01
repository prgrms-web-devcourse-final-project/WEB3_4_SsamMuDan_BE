package ssammudan.cotree.model.recruitment.portfolio.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.recruitment.portfolio.portfolio.entity.Portfolio;

/**
 * PackageName : ssammudan.cotree.model.recruitment.portfolio.portfolio.repository
 * FileName    : PortfolioRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
