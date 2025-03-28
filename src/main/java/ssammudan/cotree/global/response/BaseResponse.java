package ssammudan.cotree.global.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseResponse<T> {

	@JsonIgnore
	private final HttpStatus httpStatus;
	@NotNull
	private final Boolean isSuccess;
	@NotNull
	private final String code;
	@NotNull
	private final String message;
	private final T data;

	public static <T> BaseResponse<T> success(final SuccessCode successCode, final T data) {
		return BaseResponse.<T>builder()
				.httpStatus(successCode.getStatus())
				.isSuccess(true)
				.code(successCode.getCode())
				.message(successCode.getMessage())
				.data(data)
				.build();
	}

	public static <T> BaseResponse<T> success(final SuccessCode successCode) {
		return BaseResponse.<T>builder()
				.httpStatus(successCode.getStatus())
				.isSuccess(true)
				.code(successCode.getCode())
				.message(successCode.getMessage())
				.data(null)
				.build();
	}

	public static <T> BaseResponse<T> fail(final ErrorCode errorCode) {
		return BaseResponse.<T>builder()
				.httpStatus(errorCode.getStatus())
				.isSuccess(false)
				.code(errorCode.getCode())
				.message(errorCode.getMessage())
				.build();
	}

	public static <T> BaseResponse<T> fail(final ErrorCode errorCode, final String message) {
		return BaseResponse.<T>builder()
				.httpStatus(errorCode.getStatus())
				.isSuccess(false)
				.code(errorCode.getCode())
				.message(message)
				.build();
	}

}
