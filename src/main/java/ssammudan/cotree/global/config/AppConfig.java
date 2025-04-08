package ssammudan.cotree.global.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * PackageName : ssammudan.cotree.global.config
 * FileName    : AppConfig
 * Author      : loadingKKamo21
 * Date        : 25. 4. 7.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 7.     loadingKKamo21       Initial creation
 */
@Configuration
public class AppConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer addModules() {
		return jacksonObjectMapperBuilder -> {
			jacksonObjectMapperBuilder.modules(new JavaTimeModule());
			jacksonObjectMapperBuilder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		};
	}

}
