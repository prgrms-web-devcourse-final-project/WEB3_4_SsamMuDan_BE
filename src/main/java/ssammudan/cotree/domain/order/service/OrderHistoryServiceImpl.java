package ssammudan.cotree.domain.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.payment.dto.PrePaymentValue;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.payment.order.category.entity.OrderCategory;
import ssammudan.cotree.model.payment.order.history.entity.OrderHistory;
import ssammudan.cotree.model.payment.order.history.repository.OrderHistoryRepository;

/**
 * PackageName : ssammudan.cotree.domain.order.service
 * FileName    : OrderHistoryServiceImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 8.
 * Description : OrderHistory 서비스 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 8.     loadingKKamo21       Initial creation
 */
@Service
@RequiredArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {

	private final OrderHistoryRepository orderHistoryRepository;

	@Transactional
	@Override
	public OrderHistory createOrderHistory(
		final Member member,
		final OrderCategory orderCategory,
		final String paymentKey,
		final PrePaymentValue prePaymentValue
	) {
		return OrderHistory.create(
			member,
			orderCategory,
			prePaymentValue.info().orderId(),
			paymentKey,
			prePaymentValue.info().itemId(),
			prePaymentValue.info().productName(),
			prePaymentValue.info().amount()
		);
	}

}
