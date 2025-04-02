package ssammudan.cotree.domain.common.like;

import ssammudan.cotree.domain.common.dto.LikeRequest;

/**
 * PackageName : ssammudan.cotree.domain.common.like
 * FileName    : LikeService
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : Like 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 */
public interface LikeService {

	Long createLike(String memberId, LikeRequest.Create requestDto);

}
