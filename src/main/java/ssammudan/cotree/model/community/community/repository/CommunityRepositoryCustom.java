package ssammudan.cotree.model.community.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.domain.community.type.SearchBoardCategory;
import ssammudan.cotree.domain.community.type.SearchBoardSort;

/**
 * PackageName : ssammudan.cotree.model.community.community.repository
 * FileName    : CommunityRepositoryCustom
 * Author      : Baekgwa
 * Date        : 2025-04-01
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-01     Baekgwa               Initial creation
 */
public interface CommunityRepositoryCustom {

	Page<CommunityResponse.BoardListDetail> findBoardList(
			final Pageable pageable,
			final SearchBoardSort sort,
			final SearchBoardCategory category,
			final String keyword,
			final String memberId);
}
