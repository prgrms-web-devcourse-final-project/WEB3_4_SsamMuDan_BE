package ssammudan.cotree.global.config;

import java.util.Arrays;
import java.util.List;

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
 * 25. 4.10.     sangxxjin        여러 url 전달하게 수정 및 primary url 제공
 */
@Getter
@Configuration
public class FrontendConfig {

	private final List<String> frontendUrls;

	public FrontendConfig(@Value("${frontend.url}") String frontendUrl) {
		this.frontendUrls = Arrays.stream(frontendUrl.split(","))
			.map(String::trim)
			.toList();
	}

	public String getPrimaryFrontendUrl() {
		return frontendUrls.getFirst();
	}
}
