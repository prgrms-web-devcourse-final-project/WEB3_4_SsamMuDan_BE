package ssammudan.cotree.domain.member.dto.info;

import org.hibernate.validator.constraints.URL;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * PackageName : ssammudan.cotree.domain.member.dto.info
 * FileName    : MemberInfoRequest
 * Author      : hc
 * Date        : 25. 4. 4.
 * Description : 회원정보 변경 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     hc               Initial creation
 */
@Schema(description = "회원정보 변경 요청")
public record MemberInfoRequest(
	@Schema(description = "수정할 사용자 이름", examples = "김철수")
	@NotNull(message = "이름을 입력해주세요.")
	String username,

	@Schema(description = "수정할 사용자 별칭", examples = "철수123")
	@NotNull(message = "닉네임을 입력해주세요.")
	String nickname,

	@Schema(description = "수정할 사용자 프로필 이미지", examples = "https://example.com/images/profile.jpg")
	@URL(message = "프로필 이미지 URL 형식이 올바르지 않습니다.")
	String profileImageUrl
) {
}
