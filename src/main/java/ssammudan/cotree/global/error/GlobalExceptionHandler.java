package ssammudan.cotree.global.error;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<BaseResponse<Void>> httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
		final ErrorCode errorCode = ErrorCode.NOT_SUPPORTED_METHOD;
		return ResponseEntity.status(errorCode.getStatus())
				.body(BaseResponse.fail(errorCode, e.getMessage()));
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<BaseResponse<Void>> handleHandlerMethodValidationException(
			HandlerMethodValidationException e) {
		final ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
		return ResponseEntity.status(errorCode.getStatus())
				.body(BaseResponse.fail(errorCode));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<BaseResponse<Void>> handle(DataIntegrityViolationException e) {
		final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		return ResponseEntity.status(errorCode.getStatus())
				.body(BaseResponse.fail(errorCode));
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<BaseResponse<Void>> validationException(BindException e) {
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		StringBuilder message = new StringBuilder();
		for (ObjectError allError : allErrors) {
			if (!message.isEmpty())
				message.append("\n");
			message.append(allError.getDefaultMessage());
		}

		final ErrorCode errorCode = ErrorCode.VALIDATION_FAIL_ERROR;
		BaseResponse<Void> response = BaseResponse.fail(errorCode, message.toString());

		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<BaseResponse<Void>> noResourceFoundException(NoResourceFoundException e) {
		final ErrorCode errorCode = ErrorCode.NOT_FOUND_URL;
		return ResponseEntity.status(errorCode.getStatus())
				.body(BaseResponse.fail(errorCode));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<BaseResponse<Void>> methodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException e) {
		final ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_TYPE_MISS_MATCH;
		return ResponseEntity.status(errorCode.getStatus())
				.body(BaseResponse.fail(errorCode));
	}

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<BaseResponse<Void>> handleDomainException(GlobalException e) {
		return ResponseEntity.status(e.getErrorCode().getStatus())
				.body(BaseResponse.fail(e.getErrorCode()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
		final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		return ResponseEntity.status(errorCode.getStatus())
				.body(BaseResponse.fail(errorCode));
	}
}
