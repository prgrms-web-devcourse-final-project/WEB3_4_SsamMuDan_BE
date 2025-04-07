package ssammudan.cotree.domain.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ssammudan.cotree.model.education.type.EducationType;

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
		String completedAt
	) {
		public static Detail from(final TossPayments tossPayments) {
			return new Detail(
				tossPayments.orderId,
				tossPayments.orderName,
				tossPayments.totalAmount.intValue(),
				tossPayments.approvedAt
			);
		}
	}

	@Schema(description = "토스 결제 승인 응답 DTO")
	public record TossPayments(
		@NotBlank @Size(max = 200)
		@Schema(description = "결제의 키값, 결제를 식별하는 역할로 중복되지 않는 고유한 값, 결제 데이터 관리를 위해 반드시 저장해야하며, 결제 상태가 변해도 값이 유지됨")
		String paymentKey,
		@NotBlank
		@Schema(description = "결제 타입 정보: NORMAL(일반결제), BILLING(자동결제), BRANDPAY(브랜드페이)")
		String type,
		@NotBlank @Size(min = 6, max = 64)
		@Schema(description = "주문번호, 결제 요청에서 직접 생성한 영문 대소문자, 숫자, '-', '_'로 이루어진 6자 이상 64이하 문자열")
		String orderId,
		@NotBlank
		@Schema(description = "구매상품") @Size(max = 100)
		String orderName,
		@NotBlank
		@Schema(description = "상점아이디(MID)") @Size(max = 14)
		String mId,
		@NotBlank
		@Schema(description = "결제할 때 사용한 통화")
		String currency,
		@Nullable
		@Schema(description = "결제 수단: 카드, 가상계좌, 간편결제, 휴대폰, 계좌이체, 문화상품권, 도서문화상품권, 게임문화상품권")
		String method,
		@NotNull @Min(0)
		@Schema(description = "총 결제 금액")
		BigDecimal totalAmount,
		@NotNull @Min(0)
		@Schema(description = "취소할 수 있는 금액(잔고), 결제 취소나 부분 취소가 되고 나서 남은 값, 결제 상태 변화에 따라 값이 변함")
		BigDecimal balanceAmount,
		@NotBlank
		@Schema(description = "결제 처리 상태: READY, IN_PROGRESS, WAITING_FOR_DEPOSIT, DONE, CANCELED, PARTIAL_CANCELED, ABORTED, EXPIRED")
		String status,
		@NotBlank
		@Schema(description = "결제가 일어난 날짜와 시간 정보, yyyy-MM-dd'T'HH:mm:ss±hh:mm(ISO 8601 형식)", example = "2022-01-01T00:00:00+09:00")
		String requestedAt,
		@NotBlank
		@Schema(description = "결제 승인이 일어난 날짜와 시간 정보, yyyy-MM-dd'T'HH:mm:ss±hh:mm(ISO 8601 형식)", example = "2022-01-01T00:00:00+09:00")
		String approvedAt,
		@Nullable
		@Schema(description = "결제 취소 이력")
		List<Cancel> cancels,
		@Nullable
		Failure failure
	) {
		@Schema(description = "결제 취소 이력")
		public record Cancel(
			@Schema(description = "결제를 취소한 금액")
			BigDecimal cancelAmount,
			@Size(max = 200)
			@Schema(description = "결제를 취소한 이유")
			String cancelString,
			@Schema(description = "결제 취소가 일어난 날짜와 시간 정보, yyyy-MM-dd'T'HH:mm:ss±hh:mm(ISO 8601 형식)", example = "2022-01-01T00:00:00+09:00")
			String canceledAt,
			@Schema(description = "취소 상태, DONE이면 결제가 성공적으로 취소된 상태")
			String cancelStatus
		) {
		}

		@Schema(description = "결제 승인에 실패하면 응답으로 받는 에러 객체")
		public record Failure(
			@Schema(description = "오류 타입 에러 코드")
			String code,
			@Size(max = 510)
			@Schema(description = "에러 메시지")
			String message
		) {
		}
	}

}
