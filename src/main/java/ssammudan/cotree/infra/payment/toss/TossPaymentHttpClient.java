package ssammudan.cotree.infra.payment.toss;

import java.net.SocketTimeoutException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.domain.payment.dto.TossPaymentRequest;
import ssammudan.cotree.domain.payment.dto.TossPaymentResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.infra.payment.constant.PaymentConstant;

/**
 * PackageName : ssammudan.cotree.infra.payment.toss
 * FileName    : TossPaymentHttpClient
 * Author      : loadingKKamo21
 * Date        : 25. 4. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 8.     loadingKKamo21       Initial creation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentHttpClient {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${TOSS_API_SECRET_KEY}")
	private String tossApiSecretKey;

	public TossPaymentResponse sendPaymentConfirmRequest(final TossPaymentRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(tossApiSecretKey, "");
		HttpEntity<TossPaymentRequest> entity = new HttpEntity<>(request, headers);
		try {
			ResponseEntity<TossPaymentResponse> responseEntity = restTemplate.exchange(
				PaymentConstant.TOSS_PAYMENTS_CONFIRM_BASE_URL,
				HttpMethod.POST,
				entity,
				TossPaymentResponse.class
			);

			return responseEntity.getBody();
		} catch (HttpStatusCodeException e) {
			handleApiError(e);
			throw new GlobalException(ErrorCode.TOSS_API_ERROR);
		} catch (ResourceAccessException e) {    //연결 실패 시 발생
			if (e.getCause() instanceof SocketTimeoutException) {    //응답 대기 시간 초과 시
				log.error("TossPayments API Timeout: {}", e.getMessage());
				throw new GlobalException(ErrorCode.TOSS_API_TIMEOUT);
			} else {
				log.error("TossPayments API ResourceAccessException: {}", e.getMessage());
				throw new GlobalException(ErrorCode.TOSS_API_ERROR);
			}
		} catch (Exception e) {
			log.error("Unexpected error calling TossPayments API", e);
			throw new GlobalException(ErrorCode.TOSS_API_ERROR);
		}
	}

	private void handleApiError(HttpStatusCodeException e) {
		try {
			Map<String, Object> errorResponse = objectMapper.readValue(
				e.getResponseBodyAsString(), new TypeReference<>() {
				}
			);
			String code = (String)errorResponse.get("code");
			String message = (String)errorResponse.get("message");

			log.error("TossPayments API Error: code={}, message={}", code, message);
		} catch (JsonProcessingException ex) {
			log.error("Failed to parse TossPayments API error response", ex);
		}
	}

}
