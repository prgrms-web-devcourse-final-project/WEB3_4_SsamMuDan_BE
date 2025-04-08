package ssammudan.cotree.infra.payment;

import ssammudan.cotree.infra.payment.dto.ApiPaymentRequest;
import ssammudan.cotree.infra.payment.dto.ApiPaymentResponse;

/**
 * PackageName : ssammudan.cotree.infra.payment
 * FileName    : PaymentClient
 * Author      : loadingKKamo21
 * Date        : 25. 4. 7.
 * Description : 결제 API 클라이언트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 7.     loadingKKamo21       Initial creation
 */
public interface PaymentClient {

	ApiPaymentResponse confirmPayment(ApiPaymentRequest apiPaymentRequest);

}
