package ssammudan.cotree.model.recruitment.career.career.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.recruitment.career.career.entity.Career;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

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

	List<Career> findByResume(Resume resume);

}
