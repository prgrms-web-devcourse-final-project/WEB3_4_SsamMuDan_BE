package ssammudan.cotree.domain.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.domain.payment.dto.PaymentRequest;
import ssammudan.cotree.domain.payment.dto.PaymentResponse;
import ssammudan.cotree.domain.payment.dto.PrePaymentValue;
import ssammudan.cotree.domain.payment.dto.TossPaymentRequest;
import ssammudan.cotree.domain.payment.dto.TossPaymentResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.infra.payment.dto.ApiPaymentRequest;
import ssammudan.cotree.infra.payment.toss.TossPaymentClient;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.payment.order.category.entity.OrderCategory;
import ssammudan.cotree.model.payment.order.category.repository.OrderCategoryRepository;
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

	private final MemberRepository memberRepository;
	private final OrderCategoryRepository orderCategoryRepository;

	private final PrePaymentService prePaymentService;
	private final PaymentVerificationService paymentVerificationService;
	private final TossPaymentClient tossPaymentClient;
	private final OrderHistoryService orderHistoryService;

	/**
	 * 사전 결제 정보 Redis에 저장
	 *
	 * @param requestDto - 결제 사전 정보 요청 DTO
	 * @param memberId   - 회원 ID
	 * @return PaymentResponse PrePaymentInfo DTO
	 */
	@Override
	public PaymentResponse.PrePaymentInfo savePrePayment(
		final PaymentRequest.PrePayment requestDto, final String memberId
	) {
		if (!memberRepository.existsById(memberId)) {
			throw new GlobalException(ErrorCode.MEMBER_NOT_FOUND);
		}
		return prePaymentService.savePrePayment(requestDto, memberId);
	}

	/**
	 * 결제 승인 API 요청
	 *
	 * @param requestDto - 결제 정보 요청 DTO
	 * @param memberId   - 회원 ID
	 * @return PaymentResponse Detail DTO
	 */
	@Transactional
	@Override
	public PaymentResponse.Detail confirmPayment(
		final ApiPaymentRequest requestDto, final String memberId
	) {
		TossPaymentRequest tossPaymentRequest = (TossPaymentRequest)requestDto;

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

		PrePaymentValue verifiedValue = paymentVerificationService.verify(tossPaymentRequest, memberId);

		OrderCategory orderCategory = orderCategoryRepository.findById(verifiedValue.info().educationType().getId())
			.orElseThrow(() -> new GlobalException(ErrorCode.ORDER_CATEGORY_NOT_FOUND));

		OrderHistory orderHistory = orderHistoryService.createOrderHistory(
			member, orderCategory, tossPaymentRequest.getPaymentKey(), verifiedValue
		);

		try {
			TossPaymentResponse tossPaymentResponse = (TossPaymentResponse)tossPaymentClient.confirmPayment(
				tossPaymentRequest
			);
			orderHistory.modifyStatus(PaymentStatus.SUCCESS);
			return PaymentResponse.Detail.from(tossPaymentResponse);
		} catch (GlobalException e) {
			if (e.getErrorCode() == ErrorCode.TOSS_API_ERROR) {
				log.error("토스 API 에러", e.getMessage());
			} else if (e.getErrorCode() == ErrorCode.TOSS_API_TIMEOUT) {
				log.error("토스 API 응답 시간 초과", e.getMessage());
			}
			orderHistory.modifyStatus(PaymentStatus.FAILED);
			throw new GlobalException(ErrorCode.TOSS_API_TIMEOUT);
		} catch (Exception e) {
			log.error("알 수 없는 에러", e);
			orderHistory.modifyStatus(PaymentStatus.FAILED);
			throw new GlobalException(ErrorCode.PAYMENT_PROCESSING_FAILED);
		}
	}

}
