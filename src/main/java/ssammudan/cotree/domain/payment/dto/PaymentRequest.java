package ssammudan.cotree.domain.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ssammudan.cotree.model.education.type.EducationType;

/**
 * PackageName : ssammudan.cotree.domain.payment.dto
 * FileName    : PaymentRequest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 5.
 * Description : 결제 승인 요청 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 5.     loadingKKamo21       Initial creation
 */
@Schema(description = "결제 요청 DTO")
public class PaymentRequest {

	@Schema(description = "결제 전 사전 정보 저장 요청 DTO")
	public record PrePayment(
		@NotNull
		@Schema(description = "TechEducation 타입: TECH_BOOK, TECH_TUBE", example = "TECH_BOOK")
		EducationType techEducationType,
		@NotNull @Min(1)
		@Schema(description = "TechEducation 리뷰 대상 ID: TechBook ID, TechTube ID", example = "1")
		Long itemId,
		@NotNull @Min(0)
		@Schema(description = "결제될 금액")
		int amount,
		@NotBlank
		@Schema(description = "구매 상품 이름")
		String productName
	) {
	}

}
