package ssammudan.cotree.model.education.techbook.techbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.techbook.repository
 * FileName    : TechBookRepository
 * Author      : SSamMuDan
 * Date        : 25. 3. 28.
 * Description : TechBook JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    SSamMuDan       Initial creation
 */
public interface TechBookRepository extends JpaRepository<TechBook, Long> {
}
