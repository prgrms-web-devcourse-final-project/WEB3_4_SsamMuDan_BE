package ssammudan.cotree.model.common.like.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.common.like.repository
 * FileName    : LikeRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : Like Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 */
@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

}
