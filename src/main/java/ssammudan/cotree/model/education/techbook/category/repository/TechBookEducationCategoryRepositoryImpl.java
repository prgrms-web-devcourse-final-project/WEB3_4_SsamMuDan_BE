package ssammudan.cotree.model.education.techbook.category.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.category.repository
 * FileName    : TechBookEducationCategoryRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 3. 30.
 * Description : TechBookEducationCategory Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 30.    loadingKKamo21       Initial creation
 */
@Repository
@RequiredArgsConstructor
public class TechBookEducationCategoryRepositoryImpl implements TechBookEducationCategoryRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

}
