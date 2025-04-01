package ssammudan.cotree.model.education.level.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.education.level.entity.EducationLevel;

/**
 * PackageName : ssammudan.cotree.model.education.level.repository
 * FileName    : EducationLevelRepository
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : EducationLevel JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
public interface EducationLevelRepository extends JpaRepository<EducationLevel, Long> {

	Optional<EducationLevel> findByNameIgnoreCase(String name);

	List<EducationLevel> findAllByNameContainsIgnoreCase(String name);

}
