package ssammudan.cotree.global.handler;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import jakarta.servlet.http.HttpServletResponse;
import ssammudan.cotree.global.response.BaseResponse;

/**
 * PackageName : ssammudan.cotree.global.handler
 * FileName    : BaseResponseReturnValueHandler
 * Author      : loadingKKamo21
 * Date        : 25. 3. 31.
 * Description : BaseResponse 리턴 시 HttpStatus 동기화
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.    loadingKKamo21       Initial creation
 */
@Component
public class BaseResponseReturnValueHandler implements HandlerMethodReturnValueHandler {

	private final HttpServletResponse response;
	private final RequestResponseBodyMethodProcessor delegate;

	public BaseResponseReturnValueHandler(HttpServletResponse response, List<HttpMessageConverter<?>> converters) {
		this.response = response;
		this.delegate = new RequestResponseBodyMethodProcessor(converters);
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.getParameterType().equals(BaseResponse.class);
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest) throws Exception {
		if (returnValue instanceof BaseResponse<?> baseResponse) {
			response.setStatus(baseResponse.getHttpStatus().value());
		}
		delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
	}

}
