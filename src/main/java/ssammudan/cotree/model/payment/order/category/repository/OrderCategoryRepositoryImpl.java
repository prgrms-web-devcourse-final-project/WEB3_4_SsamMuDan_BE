package ssammudan.cotree.model.payment.order.category.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.payment.order.category.repository
 * FileName    : OrderCategoryRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 3. 30.
 * Description : OrderCategory Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 30.    loadingKKamo21       Initial creation
 */
@Repository
@RequiredArgsConstructor
public class OrderCategoryRepositoryImpl implements OrderCategoryRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

}
