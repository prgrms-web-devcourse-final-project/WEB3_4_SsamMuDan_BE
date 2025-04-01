package ssammudan.cotree.model.education.level.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.education.level.repository
 * FileName    : EducationLevelRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : EducationLevel Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
@Repository
@RequiredArgsConstructor
public class EducationLevelRepositoryImpl implements EducationLevelRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

}
