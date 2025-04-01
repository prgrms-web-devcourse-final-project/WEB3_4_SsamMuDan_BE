package ssammudan.cotree.domain.education.supporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ssammudan.cotree.domain.education.techbook.controller.TechBookController;
import ssammudan.cotree.domain.education.techbook.service.TechBookService;
import ssammudan.cotree.global.config.SecurityConfig;
import ssammudan.cotree.global.config.TestWebConfig;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jackson.plugin.JacksonPlugin;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import com.navercorp.fixturemonkey.mockito.plugin.MockitoPlugin;

/**
 * PackageName : ssammudan.cotree.domain.education.supporter
 * FileName    : WebMvcTestSupporter
 * Author      : loadingKKamo21
 * Date        : 25. 3. 31.
 * Description : @WebMvcTest 활용 테스트 지원
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.    loadingKKamo21       Initial creation
 */
@WebMvcTest({TechBookController.class})
@Import({SecurityConfig.class, TestWebConfig.class})
public abstract class WebMvcTestSupporter {

	protected final FixtureMonkey entityFixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.defaultNotNull(true)
		.build();

	protected final FixtureMonkey dtoFixtureMonkey = FixtureMonkey.builder()
		.plugin(new JakartaValidationPlugin())
		.plugin(new JacksonPlugin())
		.plugin(new MockitoPlugin())
		.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
		.build();

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockitoBean
	protected TechBookService techBookService;

}
