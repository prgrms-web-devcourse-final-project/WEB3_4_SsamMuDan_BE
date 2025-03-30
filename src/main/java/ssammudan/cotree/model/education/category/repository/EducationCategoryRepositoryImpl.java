package ssammudan.cotree.model.education.category.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.education.category.repository
 * FileName    : EducationCategoryRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 3. 30.
 * Description : EducationCategory Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 30.    loadingKKamo21       Initial creation
 */
@Repository
@RequiredArgsConstructor
public class EducationCategoryRepositoryImpl implements EducationCategoryRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

}
