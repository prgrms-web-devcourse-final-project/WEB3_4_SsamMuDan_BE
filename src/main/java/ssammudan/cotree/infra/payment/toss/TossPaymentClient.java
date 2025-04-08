package ssammudan.cotree.infra.payment.toss;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.payment.dto.TossPaymentRequest;
import ssammudan.cotree.infra.payment.PaymentClient;
import ssammudan.cotree.infra.payment.dto.ApiPaymentRequest;
import ssammudan.cotree.infra.payment.dto.ApiPaymentResponse;

/**
 * PackageName : ssammudan.cotree.infra.payment.toss
 * FileName    : TossPaymentClient
 * Author      : loadingKKamo21
 * Date        : 25. 4. 8.
 * Description : 토스페이먼츠 API 호출 전용 컴포넌트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 8.     loadingKKamo21       Initial creation
 */
@Component
@RequiredArgsConstructor
public class TossPaymentClient implements PaymentClient {

	private final TossPaymentHttpClient httpClient;

	@Override
	public ApiPaymentResponse confirmPayment(final ApiPaymentRequest apiPaymentRequest) {
		return httpClient.sendPaymentConfirmRequest((TossPaymentRequest)apiPaymentRequest);
	}

}
