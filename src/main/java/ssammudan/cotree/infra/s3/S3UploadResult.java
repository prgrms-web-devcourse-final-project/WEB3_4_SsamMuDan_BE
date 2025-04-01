package ssammudan.cotree.infra.s3;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * PackageName : ssammudan.cotree.infra.s3
 * FileName    : S3UploadResult
 * Author      : Baekgwa
 * Date        : 2025-03-31
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-31     Baekgwa               Initial creation
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class S3UploadResult {

	private String saveUrl;
}
