package ssammudan.cotree.domain.payment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/prepare")
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
