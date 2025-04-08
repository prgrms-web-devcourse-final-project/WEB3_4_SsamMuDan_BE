package ssammudan.cotree.domain.payment.service;

import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.payment.dto.PrePaymentValue;
import ssammudan.cotree.domain.payment.dto.TossPaymentRequest;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;

/**
 * PackageName : ssammudan.cotree.domain.payment.service
 * FileName    : PaymentVerificationService
 * Author      : loadingKKamo21
 * Date        : 25. 4. 7.
 * Description : 서버에 저장된 사전 결제 정보와 클라이언트의 결제 요청 정보를 비교/검증하는 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 7.     loadingKKamo21       Initial creation
 */
@Service
@RequiredArgsConstructor
public class PaymentVerificationService {

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 사전 저장 결제 정보 검증
	 *
	 * @param requestDto - 토스페이먼츠 결제 요청 DTO
	 * @param memberId   - 회원 ID
	 * @return 검증 완료 객체
	 */
	PrePaymentValue verify(final TossPaymentRequest requestDto, final String memberId) {
		PrePaymentValue value = (PrePaymentValue)redisTemplate.opsForValue().get(getRedisKey(requestDto.getOrderId()));

		if (value == null) {
			throw new GlobalException(ErrorCode.PAYMENT_EXPIRED_PREPAYMENT);
		}

		if (!value.memberId().equals(memberId)
			|| !Objects.equals(value.info().orderId(), requestDto.getOrderId())
			|| value.info().amount() != requestDto.getAmount()) {
			throw new GlobalException(ErrorCode.PAYMENT_REQUEST_INVALID);
		}

		return value;
	}

	void deletePrePayment(final TossPaymentRequest requestDto) {
		redisTemplate.delete(getRedisKey(requestDto.getOrderId()));
	}

	private String getRedisKey(final String orderId) {
		return "prepay:%s".formatted(orderId);
	}

}
