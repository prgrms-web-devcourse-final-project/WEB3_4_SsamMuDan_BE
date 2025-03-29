package ssammudan.cotree.model.education.level.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.education.level.entity.EducationLevel;

/**
 * PackageName : ssammudan.cotree.model.education.educationlevel.repository
 * FileName    : EducationLevelRepository
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : EducationLevel JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan 		      Initial creation
 */
public interface EducationLevelRepository extends JpaRepository<EducationLevel, Long> {
}
