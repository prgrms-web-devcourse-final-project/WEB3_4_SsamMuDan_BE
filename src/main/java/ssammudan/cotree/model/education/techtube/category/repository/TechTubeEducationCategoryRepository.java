package ssammudan.cotree.model.education.techtube.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.education.techtube.category.entity.TechTubeEducationCategory;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.category.repository
 * FileName    : TechTubeEducationCategoryRepository
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : TechTubeEducationCategory JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
public interface TechTubeEducationCategoryRepository
	extends JpaRepository<TechTubeEducationCategory, Long>, TechTubeEducationCategoryRepositoryCustom {
}
