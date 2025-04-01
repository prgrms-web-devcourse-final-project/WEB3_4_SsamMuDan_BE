package ssammudan.cotree.global.handler;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import jakarta.servlet.http.HttpServletResponse;
import ssammudan.cotree.global.response.BaseResponse;

/**
 * PackageName : ssammudan.cotree.global.response.handler
 * FileName    : ResponseEntityBaseResponseReturnValueHandler
 * Author      : loadingKKamo21
 * Date        : 25. 3. 31.
 * Description : ResponseEntity<BaseResonse> 리턴 시 HttpStatus 동기화
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.    loadingKKamo21       Initial creation
 */
@Component
public class ResponseEntityBaseResponseReturnValueHandler implements HandlerMethodReturnValueHandler {

	private final HttpServletResponse response;
	private final RequestResponseBodyMethodProcessor delegate;

	public ResponseEntityBaseResponseReturnValueHandler(HttpServletResponse response,
		List<HttpMessageConverter<?>> converters) {
		this.response = response;
		this.delegate = new RequestResponseBodyMethodProcessor(converters);
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		if (!ResponseEntity.class.isAssignableFrom(returnType.getParameterType())) {
			return false;
		}

		ResolvableType resolvableType = ResolvableType.forMethodParameter(returnType);
		ResolvableType genericType = resolvableType.getGeneric(0);

		return genericType != ResolvableType.NONE && BaseResponse.class.isAssignableFrom(genericType.toClass());
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest) throws Exception {
		if (returnValue instanceof ResponseEntity<?> responseEntity) {
			if (responseEntity.getBody() instanceof BaseResponse<?> baseResponse) {
				response.setStatus(baseResponse.getHttpStatus().value());
			}
		}
		delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
	}

}
