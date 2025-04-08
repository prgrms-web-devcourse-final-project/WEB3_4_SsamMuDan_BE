package ssammudan.cotree.domain.payment.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.payment.dto.PaymentRequest;
import ssammudan.cotree.domain.payment.dto.PaymentResponse;
import ssammudan.cotree.domain.payment.dto.PrePaymentValue;

/**
 * PackageName : ssammudan.cotree.domain.payment.service
 * FileName    : PrePaymentService
 * Author      : loadingKKamo21
 * Date        : 25. 4. 7.
 * Description : 사전 결제 정보 저장 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 7.     loadingKKamo21       Initial creation
 */
@Service
@RequiredArgsConstructor
public class PrePaymentService {

	private static final long MAX_RETENTION_TIME = 10;    //서버 내 결제 정보 저장 유지 시간

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 사전 결제 정보 Redis에 저장
	 *
	 * @param requestDto - 결제 사전 정보 요청 DTO
	 * @param memberId   - 회원 ID
	 * @return 결제 사전 정보 응답 DTO
	 */
	PaymentResponse.PrePaymentInfo savePrePayment(
		final String orderId, final String redisKey, final PaymentRequest.PrePayment requestDto, final String memberId
	) {
		LocalDateTime now = LocalDateTime.now();

		PaymentResponse.PrePaymentInfo prePaymentInfo = PaymentResponse.PrePaymentInfo.of(
			orderId,
			requestDto.productName(),
			requestDto.amount(),
			requestDto.techEducationType(),
			requestDto.itemId(),
			now.plusMinutes(10)
		);

		PrePaymentValue value = PrePaymentValue.of(memberId, prePaymentInfo);

		redisTemplate.opsForValue().set(redisKey, value, Duration.ofMinutes(MAX_RETENTION_TIME));

		return prePaymentInfo;
	}

}
