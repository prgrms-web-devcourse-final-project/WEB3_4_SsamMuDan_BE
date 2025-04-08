package ssammudan.cotree.domain.order.service;

import ssammudan.cotree.domain.payment.dto.PrePaymentValue;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.payment.order.category.entity.OrderCategory;
import ssammudan.cotree.model.payment.order.history.entity.OrderHistory;

/**
 * PackageName : ssammudan.cotree.domain.order.service
 * FileName    : OrderHistoryService
 * Author      : loadingKKamo21
 * Date        : 25. 4. 8.
 * Description : OrderHistory 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 8.     loadingKKamo21       Initial creation
 */
public interface OrderHistoryService {

	OrderHistory createOrderHistory(
		Member member, OrderCategory orderCategory, String paymentKey, PrePaymentValue prePaymentValue
	);

}
