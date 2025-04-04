package ssammudan.cotree.model.review.review.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.review.review.entity.TechEducationReview;

/**
 * PackageName : ssammudan.cotree.model.review.techeducationreview.repository
 * FileName    : TechEducationReviewRepository
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : TechEducationReview JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
public interface TechEducationReviewRepository
	extends JpaRepository<TechEducationReview, Long>, TechEducationReviewRepositoryCustom {

	Optional<TechEducationReview> findByReviewer_IdAndTechEducationType_IdAndItemId(
		String reviewerId, Long techEducationTypeId, Long itemId
	);

	Page<TechEducationReview> findAllByTechEducationType_IdAndItemId(
		Long techEducationTypeId, Long itemId, Pageable pageable
	);

}
