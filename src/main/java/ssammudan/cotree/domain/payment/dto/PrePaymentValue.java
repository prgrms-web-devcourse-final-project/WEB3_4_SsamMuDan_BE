package ssammudan.cotree.domain.payment.dto;

import java.io.Serializable;

/**
 * PackageName : ssammudan.cotree.domain.payment.dto
 * FileName    : PrePaymentValue
 * Author      : loadingKKamo21
 * Date        : 25. 4. 7.
 * Description : 사전 결제 정보 저장 객체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 7.     loadingKKamo21       Initial creation
 */
public record PrePaymentValue(
	String memberId,
	PaymentResponse.PrePaymentInfo info
) implements Serializable {
	public static PrePaymentValue of(final String memberId, final PaymentResponse.PrePaymentInfo info) {
		return new PrePaymentValue(memberId, info);
	}
}
