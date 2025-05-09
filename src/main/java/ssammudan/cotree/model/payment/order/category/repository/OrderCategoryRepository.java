package ssammudan.cotree.model.payment.order.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.payment.order.category.entity.OrderCategory;

/**
 * PackageName : ssammudan.cotree.model.payment.order.category.repository
 * FileName    : OrderCategoryRepository
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : OrderCategory JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
public interface OrderCategoryRepository extends JpaRepository<OrderCategory, Long>, OrderCategoryRepositoryCustom {
}
