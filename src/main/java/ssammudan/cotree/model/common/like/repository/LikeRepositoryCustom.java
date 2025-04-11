package ssammudan.cotree.model.common.like.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeResponse;

/**
 * PackageName : ssammudan.cotree.model.common.like.repository
 * FileName    : LikeRepositoryCustom
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : Like Querydsl 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 * 2025-04-11     Baekgwa               내가 좋아요 (관심)한, Community 목록 조회 기능 추가
 * 2025-04-11     Baekgwa               내가 좋아요 (관심)한, TechTube 목록 조회 기능 추가
 */
public interface LikeRepositoryCustom {

	Page<CommunityResponse.BoardLikeListDetail> findBoardLikeList(
		final Pageable pageable,
		final String memberId
	);

	Page<TechTubeResponse.TechTubeLikeListDetail> findTechBookLikeList(
		Pageable pageable,
		String memberId
	);
}
