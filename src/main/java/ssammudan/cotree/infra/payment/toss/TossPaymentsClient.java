package ssammudan.cotree.infra.payment.toss;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import ssammudan.cotree.domain.payment.dto.PaymentRequest;
import ssammudan.cotree.domain.payment.dto.PaymentResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.infra.payment.PaymentClient;
import ssammudan.cotree.infra.payment.constant.PaymentConstant;

/**
 * PackageName : ssammudan.cotree.infra.payment.toss
 * FileName    : TossPaymentsClient
 * Author      : loadingKKamo21
 * Date        : 25. 4. 7.
 * Description : 토스페이먼츠 API 호출 전용 컴포넌트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 7.     loadingKKamo21       Initial creation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentsClient implements PaymentClient {

	private final WebClient.Builder webClientBuilder;

	@Value("${TOSS_API_SECRET_KEY}")
	private String tossApiSecretKey;

	public PaymentResponse.TossPayments confirmPayment(final PaymentRequest.TossPayments requestDto) {
		return getTossPaymentsWebClient()
			.post()
			.bodyValue(requestDto)
			.retrieve()
			.onStatus(HttpStatusCode::isError, clientResponse ->
				// clientResponse.bodyToMono(PaymentResponse.TossPayments.class)
				clientResponse.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
					})
					.flatMap(response -> {
						String message = (String)response.get("message");
						String code = (String)response.get("code");
						log.error("TossPayments API Error: code={}, message={}", code, message);
						return Mono.error(new GlobalException(ErrorCode.TOSS_API_ERROR));
					}))
			.bodyToMono(PaymentResponse.TossPayments.class)
			.timeout(Duration.ofSeconds(5))
			.block();
	}

	private WebClient getTossPaymentsWebClient() {
		return webClientBuilder.baseUrl(PaymentConstant.TOSS_PAYMENTS_BASE_URL)
			.defaultHeader(HttpHeaders.AUTHORIZATION,
				"Basic %s".formatted(
					Base64.getEncoder().encodeToString((tossApiSecretKey + ":").getBytes(StandardCharsets.UTF_8))
				)
			).build();
	}

}
