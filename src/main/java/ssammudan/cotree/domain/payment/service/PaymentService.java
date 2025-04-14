package ssammudan.cotree.domain.payment.service;

import java.time.LocalDateTime;

import ssammudan.cotree.domain.payment.dto.PaymentRequest;
import ssammudan.cotree.domain.payment.dto.PaymentResponse;
import ssammudan.cotree.domain.payment.dto.PrePaymentValue;
import ssammudan.cotree.domain.payment.dto.TossPaymentRequest;
import ssammudan.cotree.domain.payment.dto.TossPaymentResponse;

/**
 * PackageName : ssammudan.cotree.domain.payment.service
 * FileName    : PaymentService
 * Author      : loadingKKamo21
 * Date        : 25. 4. 5.
 * Description : 결제 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 5.     loadingKKamo21       Initial creation
 */
public interface PaymentService {

	PaymentResponse.PrePaymentInfo savePrePayment(
		String orderId,
		String redisKey,
		LocalDateTime savedAt,
		PaymentRequest.PrePayment request,
		String memberId
	);

	PrePaymentValue verifyPayment(String redisKey, TossPaymentRequest request, String memberId);

	TossPaymentResponse confirmPaymentRequest(String redisKey, TossPaymentRequest request);

}
