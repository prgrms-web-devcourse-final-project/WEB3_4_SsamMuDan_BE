package ssammudan.cotree.model.review.review.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.review.review.repository
 * FileName    : TechEducationReviewRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 1.
 * Description : TechEducationReview Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     loadingKKamo21       Initial creation
 */
@Repository
@RequiredArgsConstructor
public class TechEducationReviewRepositoryImpl implements TechEducationReviewRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

}
