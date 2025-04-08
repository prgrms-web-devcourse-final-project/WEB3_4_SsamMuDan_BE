package ssammudan.cotree.domain.payment.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.domain.payment.dto.PaymentRequest;
import ssammudan.cotree.domain.payment.dto.PaymentResponse;
import ssammudan.cotree.domain.payment.dto.PrePaymentValue;
import ssammudan.cotree.domain.payment.dto.TossPaymentRequest;
import ssammudan.cotree.domain.payment.dto.TossPaymentResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.infra.payment.PaymentClient;
import ssammudan.cotree.infra.payment.toss.TossPaymentClient;
import ssammudan.cotree.model.payment.order.history.entity.OrderHistory;
import ssammudan.cotree.model.payment.order.type.PaymentStatus;

/**
 * PackageName : ssammudan.cotree.domain.payment.service
 * FileName    : PaymentServiceImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 5.
 * Description : 결제 서비스 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 5.     loadingKKamo21       Initial creation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private static final long MAX_RETENTION_TIME = 10;    //서버 내 결제 정보 저장 유지 시간

	private final RedisTemplate<String, Object> redisTemplate;
	private final PaymentClient paymentClient;
	private final TossPaymentClient tossPaymentClient;

	/**
	 * 사전 결제 정보 Redis에 저장
	 *
	 * @param orderId  - 주문번호
	 * @param redisKey - Redis 키
	 * @param savedAt  - 저장 시각
	 * @param request  - PaymentResponse.PrePaymentInfo DTO
	 * @param memberId - 회원 ID
	 * @return PaymentResponse PrePaymentInfo DTO
	 */
	@Override
	public PaymentResponse.PrePaymentInfo savePrePayment(
		final String orderId,
		final String redisKey,
		final LocalDateTime savedAt,
		final PaymentRequest.PrePayment request,
		final String memberId
	) {
		PaymentResponse.PrePaymentInfo prePaymentInfo = PaymentResponse.PrePaymentInfo.of(
			orderId,
			request.productName(),
			request.amount(),
			request.techEducationType(),
			request.itemId(),
			savedAt.plusMinutes(MAX_RETENTION_TIME)
		);

		PrePaymentValue value = PrePaymentValue.of(memberId, prePaymentInfo);

		redisTemplate.opsForValue().set(redisKey, value, Duration.ofMillis(MAX_RETENTION_TIME));

		return prePaymentInfo;
	}

	/**
	 * 사전 저장 결제 정보 검증
	 *
	 * @param request  - 토스페이먼츠 결제 요청 DTO
	 * @param memberId - 회원 ID
	 * @return 검증 완료 객체
	 */
	@Override
	public PrePaymentValue verifyPayment(
		final String redisKey, final TossPaymentRequest request, final String memberId
	) {
		PrePaymentValue value = (PrePaymentValue)redisTemplate.opsForValue().get(redisKey);

		if (value == null) {
			throw new GlobalException(ErrorCode.PAYMENT_EXPIRED_PREPAYMENT);
		}

		if (!Objects.equals(value.memberId(), memberId)
			|| !Objects.equals(value.info().orderId(), request.getOrderId())
			|| value.info().amount() != request.getAmount()) {
			throw new GlobalException(ErrorCode.PAYMENT_REQUEST_INVALID);
		}

		return value;
	}

	/**
	 * 결제 승인 API 호출
	 *
	 * @param redisKey     - Redis 키
	 * @param request      - 토스 결제 정보 요청 DTO
	 * @param orderHistory - OrderHistory 엔티티
	 * @return PaymentResponse Detail DTO
	 */
	@Override
	public PaymentResponse.Detail confirmPaymentRequest(
		final String redisKey, final TossPaymentRequest request, final OrderHistory orderHistory
	) {
		try {
			TossPaymentResponse tossPaymentResponse = (TossPaymentResponse)tossPaymentClient.confirmPayment(
				request
			);
			orderHistory.modifyStatus(PaymentStatus.SUCCESS);

			return PaymentResponse.Detail.from(tossPaymentResponse, PaymentStatus.SUCCESS);
		} catch (GlobalException e) {
			if (e.getErrorCode() == ErrorCode.TOSS_API_ERROR) {
				log.error("Toss API Error", e.getMessage());
			} else if (e.getErrorCode() == ErrorCode.TOSS_API_TIMEOUT) {
				log.error("Toss API Timeout", e.getMessage());
			}
			orderHistory.modifyStatus(PaymentStatus.FAILED);
		} catch (Exception e) {
			log.error("Unexpected error calling Toss API", e);
			orderHistory.modifyStatus(PaymentStatus.FAILED);
		} finally {
			try {
				deletePrePayment(redisKey);
			} catch (Exception e) {
				log.warn("Failed to delete Redis pre-payment info. key: {}", redisKey, e);
			}
		}

		return PaymentResponse.Detail.of(
			orderHistory.getOrderId(),
			orderHistory.getProductName(),
			orderHistory.getPrice(),
			LocalDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
			PaymentStatus.FAILED
		);
	}

	private void deletePrePayment(final String redisKey) {
		redisTemplate.delete(redisKey);
	}

}
