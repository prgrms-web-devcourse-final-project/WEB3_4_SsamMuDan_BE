package ssammudan.cotree.model.recruitment.portfolio.portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.recruitment.portfolio.portfolio.entity.Portfolio;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

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
	List<Portfolio> findByResume(Resume resume);
}
