package ssammudan.cotree.model.payment.orderhistory.ordercategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.payment.orderhistory.ordercategory.entity.OrderCategory;

/**
 * PackageName : ssammudan.cotree.model.payment.orderhistory.ordercategory.repository
 * FileName    : OrderCategoryRepository
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : OrderCategory JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
public interface OrderCategoryRepository extends JpaRepository<OrderCategory, Long> {
}
