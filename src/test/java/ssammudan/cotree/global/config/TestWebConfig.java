package ssammudan.cotree.global.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import ssammudan.cotree.global.handler.BaseResponseReturnValueHandler;
import ssammudan.cotree.global.handler.ResponseEntityBaseResponseReturnValueHandler;

/**
 * PackageName : ssammudan.cotree.global.config
 * FileName    : TestWebConfig
 * Author      : loadingKKamo21
 * Date        : 25. 3. 31.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.    loadingKKamo21       Initial creation
 */
@TestConfiguration
public class TestWebConfig {

	@Autowired
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private List<HttpMessageConverter<?>> converters;

	@PostConstruct
	public void setup() {
		List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(
			requestMappingHandlerAdapter.getReturnValueHandlers()
		);
		handlers.addFirst(new BaseResponseReturnValueHandler(response, converters));
		handlers.addFirst(new ResponseEntityBaseResponseReturnValueHandler(response, converters));
		requestMappingHandlerAdapter.setReturnValueHandlers(handlers);
	}

}
