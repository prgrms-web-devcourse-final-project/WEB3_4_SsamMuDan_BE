package ssammudan.cotree.domain.education.techbook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.education.techbook.dto.TechBookRequest;
import ssammudan.cotree.domain.education.techbook.dto.TechBookResponse;

/**
 * PackageName : ssammudan.cotree.domain.education.service
 * FileName    : TechBookService
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBook 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 * 25. 4. 1.     loadingKKamo21       findAllTechBooks() 추가
 */
public interface TechBookService {

	Long createTechBook(TechBookRequest.Create requestDto);

	TechBookResponse.Detail findTechBookById(Long id);

	Page<TechBookResponse.ListInfo> findAllTechBooks(String keyword, Pageable pageable);

}
