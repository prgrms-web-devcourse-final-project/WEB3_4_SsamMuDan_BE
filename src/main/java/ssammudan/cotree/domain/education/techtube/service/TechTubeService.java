package ssammudan.cotree.domain.education.techtube.service;

import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.education.techtube.dto.TechTubeRequest;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeResponse;
import ssammudan.cotree.domain.education.type.SearchEducationSort;
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
 * 25. 4. 7.     Baekgwa       		  목록 조회 method params 변경
 * 25. 4. 7.     Baekgwa       		  techTube 상세 조회 refactor
 * 2025-04-11     Baekgwa               내가 좋아요 (관심)한, TechTube 목록 조회 기능 추가
 */
public interface TechTubeService {

	Long createTechTube(String memberId, TechTubeRequest.Create requestDto);

	TechTubeResponse.TechTubeDetail findTechTubeDetail(Long techTubeId, String memberId);

	PageResponse<TechTubeResponse.ListInfo> findAllTechTubes(
		String keyword,
		SearchEducationSort sort,
		Pageable pageable,
		String memberId,
		Long educationId
	);

	PageResponse<TechTubeResponse.TechTubeLikeListDetail> getTechTubeLikeList(
		final Pageable pageable,
		final String memberId
	);
}
