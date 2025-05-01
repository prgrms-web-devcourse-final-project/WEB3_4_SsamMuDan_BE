package ssammudan.cotree.model.review.review.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.review.dto.TechEducationReviewResponse;
import ssammudan.cotree.model.review.review.entity.TechEducationReview;

/**
 * PackageName : ssammudan.cotree.model.review.review.repository
 * FileName    : TechEducationReviewRepositoryCustom
 * Author      : loadingKKamo21
 * Date        : 25. 4. 1.
 * Description : TechEducationReview Querydsl 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     loadingKKamo21       Initial creation
 */
public interface TechEducationReviewRepositoryCustom {

	Page<TechEducationReview> findAllTechEducationReviews(
		final Long techEducationTypeId, final Long itemId, final Pageable pageable
	);

	Page<TechEducationReviewResponse.ReviewDetail> findReviews(
		final Long techEducationTypeId, final Long itemId, final Pageable pageable
	);

	List<TechEducationReviewResponse.ReviewDetail> findReviewList(
		final Long techEducationTypeId, final Long itemId, final Pageable pageable
	);
}
