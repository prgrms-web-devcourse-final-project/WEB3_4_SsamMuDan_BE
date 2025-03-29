package ssammudan.cotree.model.education.techbook.educationcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.education.techbook.educationcategory.entity.TechBookEducationCategory;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.educationcategory.repository
 * FileName    : TechBookEducationCategoryRepository
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : TechBookEducationCategory JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
public interface TechBookEducationCategoryRepository extends JpaRepository<TechBookEducationCategory, Long> {
}
