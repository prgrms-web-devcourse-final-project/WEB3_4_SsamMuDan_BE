package ssammudan.cotree.model.education.techbook.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.education.techbook.category.entity.TechBookEducationCategory;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.category.repository
 * FileName    : TechBookEducationCategoryRepository
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : TechBookEducationCategory JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
public interface TechBookEducationCategoryRepository
	extends JpaRepository<TechBookEducationCategory, Long>, TechBookEducationCategoryRepositoryCustom {
}
