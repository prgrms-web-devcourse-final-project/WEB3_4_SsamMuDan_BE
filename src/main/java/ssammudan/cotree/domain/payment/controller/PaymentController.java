package ssammudan.cotree.domain.payment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.domain.payment.dto.PaymentRequest;
import ssammudan.cotree.domain.payment.dto.PaymentResponse;
import ssammudan.cotree.domain.payment.dto.TossPaymentRequest;
import ssammudan.cotree.domain.payment.service.PaymentService;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.payment.controller
 * FileName    : PaymentController
 * Author      : loadingKKamo21
 * Date        : 25. 4. 5.
 * Description : 결제 컨트롤러
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 5.     loadingKKamo21       Initial creation
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/payment/")
@RequiredArgsConstructor
@Tag(name = "Payment Controller", description = "Payment API")
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/prepare")
	@Operation(summary = "결제 전 결제 정보 저장 및 주문번호 발급", description = "결제 전 주문 정보를 저장하고 주문번호를 발급")
	@ApiResponse(responseCode = "200", description = "정보 저장 및 주문번호 발급 성공")
	public BaseResponse<PaymentResponse.PrePaymentInfo> preparePayment(
		@RequestBody @Valid final PaymentRequest.PrePayment requestDto,
		@AuthenticationPrincipal final UserDetails userDetails
	) {
		PaymentResponse.PrePaymentInfo resopnseDt = paymentService.savePrePayment(
			requestDto, ((CustomUser)userDetails).getId()
		);
		return BaseResponse.success(SuccessCode.PRE_PAYMENT_SAVE_SUCCESS, resopnseDt);
	}

	@GetMapping("/confirm")
	@Operation(summary = "결제 승인 API 호출", description = "결제 API로부터 받은 정보로 서버에서 결제 정보 검증 후 결제 승인 API 호출")
	@ApiResponse(responseCode = "200", description = "결제 성공")
	public BaseResponse<PaymentResponse.Detail> confirmPayment(
		@RequestParam("orderId") String orderId,
		@RequestParam("paymentKey") String paymentKey,
		@RequestParam("amount") @Min(0) int amount,
		@AuthenticationPrincipal final UserDetails userDetails
	) {
		PaymentResponse.Detail responseDto = paymentService.confirmPayment(
			TossPaymentRequest.of(paymentKey, amount, orderId), ((CustomUser)userDetails).getId()
		);
		return BaseResponse.success(SuccessCode.TOSS_PAYMENT_SUCCESS, responseDto);
	}

}
