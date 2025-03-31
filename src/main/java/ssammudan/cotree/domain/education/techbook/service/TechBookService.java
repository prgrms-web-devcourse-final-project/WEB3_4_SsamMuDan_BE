package ssammudan.cotree.domain.education.techbook.service;

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
 */
public interface TechBookService {

	Long createTechBook(TechBookRequest.Create requestDto);

	TechBookResponse.Detail findTechBookById(Long id);

}
