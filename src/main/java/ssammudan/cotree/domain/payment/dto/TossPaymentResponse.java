package ssammudan.cotree.domain.payment.dto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.infra.payment.dto.ApiPaymentResponse;

/**
 * PackageName : ssammudan.cotree.domain.payment.dto
 * FileName    : TossPaymentResponse
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
@Schema(description = "토스 결제 승인 응답 DTO")
public class TossPaymentResponse extends ApiPaymentResponse {

	@NotBlank
	@Size(max = 200)
	@Schema(description = "결제의 키값, 결제를 식별하는 역할로 중복되지 않는 고유한 값, 결제 데이터 관리를 위해 반드시 저장해야하며, 결제 상태가 변해도 값이 유지됨")
	private String paymentKey;

	@NotBlank
	@Schema(description = "결제 타입 정보: NORMAL(일반결제), BILLING(자동결제), BRANDPAY(브랜드페이)")
	private String type;

	@NotBlank
	@Size(min = 6, max = 64)
	@Schema(description = "주문번호, 결제 요청에서 직접 생성한 영문 대소문자, 숫자, '-', '_'로 이루어진 6자 이상 64이하 문자열")
	private String orderId;

	@NotBlank
	@Schema(description = "구매상품")
	@Size(max = 100)
	private String orderName;

	@NotBlank
	@Schema(description = "상점아이디(MID)")
	@Size(max = 14)
	private String mId;

	@NotBlank
	@Schema(description = "결제할 때 사용한 통화")
	private String currency;

	@Nullable
	@Schema(description = "결제 수단: 카드, 가상계좌, 간편결제, 휴대폰, 계좌이체, 문화상품권, 도서문화상품권, 게임문화상품권")
	private String method;

	@NotNull
	@Min(0)
	@Schema(description = "총 결제 금액")
	private BigDecimal totalAmount;

	@NotNull
	@Min(0)
	@Schema(description = "취소할 수 있는 금액(잔고), 결제 취소나 부분 취소가 되고 나서 남은 값, 결제 상태 변화에 따라 값이 변함")
	private BigDecimal balanceAmount;

	@NotBlank
	@Schema(description = "결제 처리 상태: READY, IN_PROGRESS, WAITING_FOR_DEPOSIT, DONE, CANCELED, PARTIAL_CANCELED, ABORTED, EXPIRED")
	private String status;

	@NotBlank
	@Schema(description = "결제가 일어난 날짜와 시간 정보, yyyy-MM-dd'T'HH:mm:ss±hh:mm(ISO 8601 형식)", example = "2022-01-01T00:00:00+09:00")
	private String requestedAt;

	@NotBlank
	@Schema(description = "결제 승인이 일어난 날짜와 시간 정보, yyyy-MM-dd'T'HH:mm:ss±hh:mm(ISO 8601 형식)", example = "2022-01-01T00:00:00+09:00")
	private String approvedAt;

	@Nullable
	@Schema(description = "결제 취소 이력")
	private List<Cancel> cancels;

	@Nullable
	private Failure failure;

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
