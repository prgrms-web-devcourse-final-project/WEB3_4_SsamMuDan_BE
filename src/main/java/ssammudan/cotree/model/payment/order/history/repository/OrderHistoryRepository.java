package ssammudan.cotree.model.payment.order.history.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.payment.order.history.entity.OrderHistory;

/**
 * PackageName : ssammudan.cotree.model.payment.order.history.repository
 * FileName    : OrderHistoryRepository
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : OrderHistory JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long>, OrderHistoryRepositoryCustom {

	Optional<OrderHistory> findByOrderId(String orderId);

	List<OrderHistory> findAllByCustomer_Id(String customerId);

	boolean existsByOrderId(String orderId);

	boolean existsByCustomer_IdAndOrderCategory_IdAndProductId(String customerId, Long orderCategoryId, Long productId);

}
