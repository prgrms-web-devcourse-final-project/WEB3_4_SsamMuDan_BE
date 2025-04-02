package ssammudan.cotree.domain.upload.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;
import ssammudan.cotree.infra.s3.S3Directory;
import ssammudan.cotree.infra.s3.S3UploadResult;
import ssammudan.cotree.infra.s3.S3Uploader;

/**
 * PackageName : ssammudan.cotree.domain.upload.controller
 * FileName    : FileUploadController
 * Author      : Baekgwa
 * Date        : 2025-03-31
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-31     Baekgwa               Initial creation
 */
@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Tag(name = "S3 File Upload", description = "S3 File Upload")
public class FileUploadController {

	private final S3Uploader s3Uploader;

	@PostMapping("/upload")
	public BaseResponse<S3UploadResult> uploadFile(
			@RequestParam("file") MultipartFile file,
			@RequestParam("directory") S3Directory directory,
			@AuthenticationPrincipal CustomUser customUser
	) {
		String memberId = customUser.getId();
		S3UploadResult result = s3Uploader.upload(memberId, file, directory);
		return BaseResponse.success(SuccessCode.S3_FILE_UPLOAD_SUCCESS, result);
	}
}
