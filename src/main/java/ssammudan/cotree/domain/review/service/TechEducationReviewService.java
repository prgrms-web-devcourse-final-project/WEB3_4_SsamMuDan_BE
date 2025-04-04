package ssammudan.cotree.domain.review.service;

import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.review.dto.TechEducationReviewRequest;
import ssammudan.cotree.domain.review.dto.TechEducationReviewResponse;
import ssammudan.cotree.global.response.PageResponse;

/**
 * PackageName : ssammudan.cotree.domain.review.review.service
 * FileName    : TechEducationReviewService
 * Author      : loadingKKamo21
 * Date        : 25. 4. 1.
 * Description : TechEducationReview 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     loadingKKamo21       Initial creation
 */
public interface TechEducationReviewService {

	Long createTechEducationReview(String memberId, TechEducationReviewRequest.Create requestDto);

	PageResponse<TechEducationReviewResponse.Detail> findAllTechEducationReviews(
		TechEducationReviewRequest.Read requestDto, Pageable pageable
	);

}
