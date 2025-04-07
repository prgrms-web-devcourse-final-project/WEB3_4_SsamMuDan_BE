package ssammudan.cotree.model.education.techtube.techtube.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.education.techtube.dto.TechTubeResponse;
import ssammudan.cotree.domain.education.type.SearchEducationSort;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.techtube.repository
 * FileName    : TechTubeRepositoryCustom
 * Author      : loadingKKamo21
 * Date        : 25. 3. 30.
 * Description : TechTube Querydsl 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 30.    loadingKKamo21       Initial creation
 * 25. 4. 7.     Baekgwa       	 	  techtube 목록 조회 service method params 변경
 * 25. 4. 7.     Baekgwa       		  techTube 상세 조회 refactor
 */
public interface TechTubeRepositoryCustom {

	Page<TechTubeResponse.ListInfo> findTechTubeList(
		String keyword,
		SearchEducationSort sort,
		Pageable pageable,
		String memberId,
		Long educationId);

	TechTubeResponse.TechTubeDetail findTechTube(
		Long techTubeId,
		String memberId);
}
