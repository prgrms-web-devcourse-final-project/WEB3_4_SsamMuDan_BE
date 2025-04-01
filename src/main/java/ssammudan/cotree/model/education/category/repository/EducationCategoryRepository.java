package ssammudan.cotree.model.education.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.education.category.entity.EducationCategory;

/**
 * PackageName : ssammudan.cotree.model.education.category.repository
 * FileName    : EducationCategoryRepository
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : EducationCategory JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
public interface EducationCategoryRepository
	extends JpaRepository<EducationCategory, Long>, EducationCategoryRepositoryCustom {
}
