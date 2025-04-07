package ssammudan.cotree.domain.payment.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.payment.dto.PrePaymentValue;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.payment.order.category.entity.OrderCategory;
import ssammudan.cotree.model.payment.order.history.entity.OrderHistory;
import ssammudan.cotree.model.payment.order.history.repository.OrderHistoryRepository;

/**
 * PackageName : ssammudan.cotree.domain.order
 * FileName    : OrderHistoryService
 * Author      : loadingKKamo21
 * Date        : 25. 4. 7.
 * Description : 주문 기록 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 7.     loadingKKamo21       Initial creation
 */
@Service
@RequiredArgsConstructor
public class OrderHistoryService {

	private final OrderHistoryRepository orderHistoryRepository;

	OrderHistory createOrderHistory(
		final Member member,
		final OrderCategory orderCategory,
		final String paymentKey,
		final PrePaymentValue prePaymentValue
	) {
		return orderHistoryRepository.save(OrderHistory.create(
			member,
			orderCategory,
			prePaymentValue.info().orderId(),
			paymentKey,
			prePaymentValue.info().itemId(),
			prePaymentValue.info().productName(),
			prePaymentValue.info().amount()
		));
	}

}
