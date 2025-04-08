package ssammudan.cotree.domain.resume.dto;

import java.util.Set;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : BasicInfo
 * Author      : kwak
 * Date        : 2025. 3. 28.
 * Description : 기본정보에는 이미지, email, 연차, 자기소개 존재 + 개발직무와 기술스택은 별도의 API 호출
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 28.     kwak               Initial creation
 */
@Builder
public record BasicInfo(
	@Nullable
	String profileImage,

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "유효한 이메일 형식을 입력해주세요.")
	String email,

	@Min(value = 0, message = "연차는 0 이상이여야 합니다.")
	@Max(value = 10, message = "10년차가 최대 값 입니다.")
	Integer years,

	@NotBlank(message = "자기소개는 필수입니다.")
	@Size(min = 1, max = 299, message = "자기소개는 1자 이상 300자 미만이어야 합니다.")
	String introduction,

	@NotEmpty
	@Size(min = 1, max = 9, message = "개발 직무는 최소 1개에서 최대 10개 미만입니다.")
	Set<Long> developPositionIds,

	@NotEmpty(message = "기술 스택 ID는 최소 1개 이상이어야 합니다.")
	@Size(min = 1, max = 9, message = "기술 스택은 최소 1개에서 최대 10개 미만입니다.")
	Set<Long> techStackIds
) {
}
