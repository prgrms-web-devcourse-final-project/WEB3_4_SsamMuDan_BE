package ssammudan.cotree.model.education.techtube.category.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.category.repository
 * FileName    : TechTubeEducationCategoryRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 3. 30.
 * Description : TechTubeEducationCategory Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 30.    loadingKKamo21       Initial creation
 */
@Repository
@RequiredArgsConstructor
public class TechTubeEducationCategoryRepositoryImpl implements TechTubeEducationCategoryRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

}
