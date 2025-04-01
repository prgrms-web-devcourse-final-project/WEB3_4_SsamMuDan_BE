package ssammudan.cotree.model.review.reviewtype.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.review.reviewtype.repository
 * FileName    : TechEducationTypeRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 1.
 * Description : TechEducationType Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     loadingKKamo21       Initial creation
 */
@Repository
@RequiredArgsConstructor
public class TechEducationTypeRepositoryImpl implements TechEducationTypeRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

}
