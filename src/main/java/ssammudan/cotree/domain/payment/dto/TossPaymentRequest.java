package ssammudan.cotree.domain.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.infra.payment.dto.ApiPaymentRequest;

/**
 * PackageName : ssammudan.cotree.domain.payment.dto
 * FileName    : TossPaymentRequest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 8.     loadingKKamo21       Initial creation
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "토스 결제 승인 요청 DTO")
public class TossPaymentRequest extends ApiPaymentRequest {

	@NotBlank
	@Schema(description = "토스에서 발급하는 결제 건에 대한 고유 식별자")
	private String paymentKey;

	@NotNull
	@Min(0)
	@Schema(description = "결제될 금액")
	private int amount;

	@NotBlank
	@Size(min = 6, max = 64)
	@Schema(description = "주문번호, 결제 요청에서 직접 생성한 영문 대소문자, 숫자, '-', '_'로 이루어진 6자 이상 64이하 문자열")
	private String orderId;

	public static TossPaymentRequest of(
		final String paymentKey, final int amount, final String orderId
	) {
		return new TossPaymentRequest(paymentKey, amount, orderId);
	}

}
