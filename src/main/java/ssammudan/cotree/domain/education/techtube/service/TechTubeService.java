package ssammudan.cotree.domain.education.techtube.service;

import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.education.techtube.dto.TechTubeRequest;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeResponse;
import ssammudan.cotree.global.response.PageResponse;

/**
 * PackageName : ssammudan.cotree.domain.education.techtube.service
 * FileName    : TechTubeService
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : TechTube 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 */
public interface TechTubeService {

	Long createTechTube(String memberId, TechTubeRequest.Create requestDto);

	TechTubeResponse.Detail findTechTubeById(Long id);

	PageResponse<TechTubeResponse.ListInfo> findAllTechTubes(String keyword, Pageable pageable);

}
