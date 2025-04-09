package ssammudan.cotree.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * PackageName : ssammudan.cotree.infra.frontend
 * FileName    : FrontendConfig
 * Author      : hc
 * Date        : 25. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     hc               Initial creation
 */

@Configuration
public class FrontendConfig {
	@Getter
	@Value("${frontend.url}")
	private String frontendUrl;
}
