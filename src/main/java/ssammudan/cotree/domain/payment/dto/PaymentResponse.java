package ssammudan.cotree.domain.payment.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import ssammudan.cotree.model.education.type.EducationType;
import ssammudan.cotree.model.payment.order.type.PaymentStatus;

/**
 * PackageName : ssammudan.cotree.domain.payment.dto
 * FileName    : PaymentResponse
 * Author      : loadingKKamo21
 * Date        : 25. 4. 5.
 * Description : 결제 결과 응답 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 5.     loadingKKamo21       Initial creation
 */
@Schema(description = "결제 결과 응답 DTO")
public class PaymentResponse {

	@Schema(description = "결제 전 사전 정보 저장 응답")
	public record PrePaymentInfo(
		@Schema(description = "주문번호, 결제 요청에서 직접 생성한 영문 대소문자, 숫자, '-', '_'로 이루어진 6자 이상 64이하 문자열")
		String orderId,
		@Schema(description = "구매 상품명")
		String productName,
		@Schema(description = "결제될 금액")
		int amount,
		@Schema(description = "TechEducation 타입: TECH_TUBE, TECH_BOOK", example = "TECH_TUBE")
		EducationType educationType,
		@Schema(description = "TechEducationItem ID: TechBook ID, TechTube ID", example = "1")
		long itemId,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		@Schema(description = "사전 결제 정보 만료 시간")
		LocalDateTime expiredAt
	) {
		public static PrePaymentInfo of(
			final String orderId,
			final String productName,
			final int amount,
			final EducationType educationType,
			final long itemId,
			final LocalDateTime expiredAt
		) {
			return new PrePaymentInfo(orderId, productName, amount, educationType, itemId, expiredAt);
		}
	}

	@Schema(description = "애플리케이션 결제 응답 DTO")
	public record Detail(
		@Schema(description = "주문번호, 결제 요청에서 직접 생성한 영문 대소문자, 숫자, '-', '_'로 이루어진 6자 이상 64이하 문자열")
		String orderId,
		@Schema(description = "주문 상품명")
		String productName,
		@Schema(description = "결제 금액")
		int amount,
		@Schema(description = "결제 완료 시간")
		String completedAt,
		@Schema(description = "결제 상태")
		PaymentStatus paymentStatus
	) {
		public static Detail from(final TossPaymentResponse response, final PaymentStatus paymentStatus) {
			return new Detail(
				response.getOrderId(),
				response.getOrderName(),
				response.getTotalAmount().intValue(),
				response.getApprovedAt(),
				paymentStatus
			);
		}

		public static Detail of(
			final String orderId,
			final String productName,
			final int amount,
			final String completedAt,
			final PaymentStatus paymentStatus
		) {
			return new Detail(
				orderId,
				productName,
				amount,
				completedAt,
				paymentStatus
			);
		}
	}

}
