package ssammudan.cotree.infra.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * PackageName : ssammudan.cotree.global.config
 * FileName    : S3Config
 * Author      : Baekgwa
 * Date        : 2025-03-31
 * Description : Credentials 인증 방법, AWS CLI 로 처리.
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-31     Baekgwa               Initial creation
 */
@Configuration
public class S3Config {

	@Value("${cloud.aws.region.static}")
	private String region;

	@Bean
	public S3Client s3Client() {
		return S3Client.builder()
				.region(Region.of(region))
				.build();
	}
}
