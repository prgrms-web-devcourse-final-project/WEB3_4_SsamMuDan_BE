package ssammudan.cotree.model.payment.order.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.payment.order.history.entity.OrderHistory;

/**
 * PackageName : ssammudan.cotree.model.payment.order.history.repository
 * FileName    : OrderHistoryRepository
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : OrderHistory JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}
