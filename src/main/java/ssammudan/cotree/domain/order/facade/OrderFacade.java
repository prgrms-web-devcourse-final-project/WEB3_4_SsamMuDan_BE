package ssammudan.cotree.domain.order.facade;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.member.service.MemberService;
import ssammudan.cotree.domain.order.service.OrderCategoryService;
import ssammudan.cotree.domain.order.service.OrderHistoryService;
import ssammudan.cotree.domain.payment.dto.PaymentRequest;
import ssammudan.cotree.domain.payment.dto.PaymentResponse;
import ssammudan.cotree.domain.payment.dto.PrePaymentValue;
import ssammudan.cotree.domain.payment.dto.TossPaymentRequest;
import ssammudan.cotree.domain.payment.dto.TossPaymentResponse;
import ssammudan.cotree.domain.payment.service.PaymentService;
import ssammudan.cotree.infra.payment.dto.ApiPaymentRequest;
import ssammudan.cotree.model.education.type.EducationType;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.payment.order.category.entity.OrderCategory;
import ssammudan.cotree.model.payment.order.history.entity.OrderHistory;
import ssammudan.cotree.model.payment.order.type.PaymentStatus;

/**
 * PackageName : ssammudan.cotree.domain.order.facade
 * FileName    : OrderFacade
 * Author      : loadingKKamo21
 * Date        : 25. 4. 8.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 8.     loadingKKamo21       Initial creation
 */
@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final MemberService memberService;
	private final OrderCategoryService orderCategoryService;
	private final OrderHistoryService orderHistoryService;
	private final PaymentService paymentService;

	/**
	 * 사전 결제 정보 Redis에 저장
	 *
	 * @param requestDto - 결제 사전 정보 요청 DTO
	 * @param memberId   - 회원 ID
	 * @return PaymentResponse PrePaymentInfo DTO
	 */
	public PaymentResponse.PrePaymentInfo savePrePayment(
		final PaymentRequest.PrePayment requestDto, final String memberId
	) {
		memberService.findById(memberId);    //회원 검증

		LocalDateTime now = LocalDateTime.now();

		String orderId = generateOrderId(now);
		String redisKey = getRedisKey(orderId);

		return paymentService.savePrePayment(orderId, redisKey, now, requestDto, memberId);
	}

	/**
	 * 결제 승인 API 호출
	 *
	 * @param request  - 결제 정보 요청 DTO
	 * @param memberId - 회원 ID
	 * @return PaymentResponse Detail DTO
	 */
	@Transactional
	public PaymentResponse.PaymentDetail confirmPayment(final ApiPaymentRequest request, final String memberId) {
		TossPaymentRequest tossPaymentRequest = (TossPaymentRequest)request;

		Member member = memberService.findById(memberId);

		String redisKey = getRedisKey(tossPaymentRequest.getOrderId());

		//사전 저장된 결제 정보 검증
		PrePaymentValue verifiedValue = paymentService.verifyPayment(redisKey, tossPaymentRequest, memberId);
		long educationTypeId = verifiedValue.info().educationType().getId();
		long itemId = verifiedValue.info().itemId();

		OrderCategory orderCategory = orderCategoryService.findOrderCategoryById(educationTypeId);

		OrderHistory orderHistory = orderHistoryService.createOrderHistory(
			member, orderCategory, tossPaymentRequest.getPaymentKey(), verifiedValue
		);

		TossPaymentResponse response;
		try {
			response = paymentService.confirmPaymentRequest(redisKey, tossPaymentRequest);
			orderHistoryService.updateStatus(orderHistory, PaymentStatus.SUCCESS);
		} catch (Exception e) {
			orderHistoryService.updateStatus(orderHistory, PaymentStatus.FAILED);
			throw e;
		}

		return PaymentResponse.PaymentDetail.of(
			response.getOrderId(),
			response.getOrderName(),
			response.getTotalAmount().intValue(),
			EducationType.getTechEducationType(educationTypeId),
			itemId,
			response.getApprovedAt(),
			PaymentStatus.SUCCESS
		);
	}

	/**
	 * 주문번호(orderId) 생성
	 *
	 * @param localDateTime - 주문 생성 시각
	 * @return 주문번호
	 */
	private String generateOrderId(final LocalDateTime localDateTime) {
		return "Order_%s_%s".formatted(
			localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
			UUID.randomUUID().toString().replace("-", "")
		);
	}

	/**
	 * 결제 정보 저장용 Redis 키 생성
	 *
	 * @param orderId - 주문번호
	 * @return Redis 키
	 */
	private String getRedisKey(final String orderId) {
		return "payment:prepay:%s".formatted(orderId);
	}

}
