package ssammudan.cotree.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonInclude(Include.NON_NULL)
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseResponse<T> {

    @NotNull
    private final Boolean isSuccess;
    @NotNull
    private final String  code;
    @NotNull
    private final String  message;
    private final T       data;

    public static <T> BaseResponse<T> success(final SuccessCode successCode, final T data) {
        return BaseResponse.<T>builder()
                           .isSuccess(true)
                           .code(successCode.getCode())
                           .message(successCode.getMessage())
                           .data(data)
                           .build();
    }

    public static <T> BaseResponse<T> success(final SuccessCode successCode) {
        return BaseResponse.<T>builder()
                           .isSuccess(true)
                           .code(successCode.getCode())
                           .message(successCode.getMessage())
                           .data(null)
                           .build();
    }

    public static <T> BaseResponse<T> fail(final ErrorCode errorCode) {
        return BaseResponse.<T>builder()
                           .isSuccess(true)
                           .code(errorCode.getCode())
                           .message(errorCode.getMessage())
                           .build();
    }

    public static <T> BaseResponse<T> fail(final ErrorCode errorCode, final String message) {
        return BaseResponse.<T>builder()
                           .isSuccess(true)
                           .code(errorCode.getCode())
                           .message(message)
                           .build();
    }

}
