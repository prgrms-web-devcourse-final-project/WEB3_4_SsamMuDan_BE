package ssammudan.cotree.model.education.techtube.educationcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.education.techtube.educationcategory.entity.TechTubeEducationCategory;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.educationcategory.repository
 * FileName    : TechTubeEducationCategoryRepository
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : TechTubeEducationCategory JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
public interface TechTubeEducationCategoryRepository extends JpaRepository<TechTubeEducationCategory, Long> {
}
