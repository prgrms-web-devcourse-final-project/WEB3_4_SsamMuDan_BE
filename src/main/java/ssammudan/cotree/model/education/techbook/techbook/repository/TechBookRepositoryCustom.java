package ssammudan.cotree.model.education.techbook.techbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.techbook.repository
 * FileName    : TechBookRepositoryCustom
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBook Querydsl 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 * 25. 4. 1.	 loadingKKamo21		  findAllTechBooksByKeyword() 추가
 */
public interface TechBookRepositoryCustom {

	Page<TechBook> findAllTechBooksByKeyword(String keyword, Pageable pageable);

}
