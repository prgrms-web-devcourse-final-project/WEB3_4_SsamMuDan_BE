package ssammudan.cotree.infra.s3;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;

/**
 * PackageName : ssammudan.cotree.infra.s3
 * FileName    : S3Uploader
 * Author      : Baekgwa
 * Date        : 2025-03-31
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-31     Baekgwa               Initial creation
 * 2025-04-04     Baekgwa               버그로 인한 S3 Upload 시 Encoding 처리 삭제. 대신, 특수문자만 _ 로 replace 진행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Value("${cloud.aws.region.static}")
	private String region;

	/**
	 * 단일 멀티파트파일 전송
	 * 파일 S3 업로드 시, 다음 관례에 따라 경로 저장
	 * domain/sub-domain/member_uuid/{currentTime}_file_name;
	 * currentTime 은, 하나의 멤버 가 여러개의 파일을 가질 수 있는 경우, 파일별 식별을 위한 용도
	 * return : 읽기 가능한 저장된 파일 url
	 */
	public S3UploadResult upload(String memberId, MultipartFile file, S3Directory directory) {
		String fileName = file.getOriginalFilename()
			.replaceAll("[^a-zA-Z0-9ㄱ-ㅎ가-힣.-]", "_");
		String key = fileKeyGenerator(memberId, fileName, directory);
		try {
			PutObjectRequest objectRequest = PutObjectRequest
				.builder()
				.bucket(bucketName)
				.key(key)
				.contentType(file.getContentType())
				.contentLength(file.getSize())
				.build();
			s3Client.putObject(objectRequest, RequestBody.fromBytes(file.getBytes()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GlobalException(ErrorCode.FILE_UPLOAD_FAIL);
		}
		String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
		return S3UploadResult.builder().saveUrl(fileUrl).build();
	}

	private String fileKeyGenerator(String memberId, String fileName, S3Directory directory) {
		return directory.isMultiFile()
			? directory.getPath() + memberId + "/" + System.currentTimeMillis() + "_" + fileName
			: directory.getPath() + memberId + "/" + fileName;
	}
}
