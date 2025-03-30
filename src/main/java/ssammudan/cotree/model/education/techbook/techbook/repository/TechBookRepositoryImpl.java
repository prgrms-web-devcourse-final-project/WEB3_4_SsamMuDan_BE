package ssammudan.cotree.model.education.techbook.techbook.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.techbook.repository
 * FileName    : TechBookRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBook Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 */
@Repository
@RequiredArgsConstructor
public class TechBookRepositoryImpl implements TechBookRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

}
