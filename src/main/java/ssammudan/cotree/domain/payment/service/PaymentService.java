package ssammudan.cotree.domain.payment.service;

import ssammudan.cotree.domain.payment.dto.PaymentRequest;
import ssammudan.cotree.domain.payment.dto.PaymentResponse;

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

	PaymentResponse.PrePaymentInfo savePrePayment(PaymentRequest.PrePayment requestDto, String memberId);

	PaymentResponse.Detail confirmPayment(PaymentRequest.TossPayments requestDto, String memberId);

}
