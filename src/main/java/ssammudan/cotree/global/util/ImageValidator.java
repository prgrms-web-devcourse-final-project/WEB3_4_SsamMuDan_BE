package ssammudan.cotree.global.util;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * PackageName : ssammudan.cotree.global.util
 * FileName    : ImageValidator
 * Author      : sangxxjin
 * Date        : 2025. 4. 13.
 * Description : 이미지 파일 형식 validator
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 13.     sangxxjin               Initial creation
 */
public class ImageValidator {

	private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
		"image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
	);

	public static boolean isValidImage(MultipartFile file) {
		if (file == null || file.isEmpty())
			return true;
		return ALLOWED_CONTENT_TYPES.contains(file.getContentType());
	}
}
