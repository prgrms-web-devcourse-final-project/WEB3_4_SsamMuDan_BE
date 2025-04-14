package ssammudan.cotree.domain.member.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * PackageName : ssammudan.cotree.domain.member.dto.update
 * FileName    : MemberPasswordRequest
 * Author      : hc
 * Date        : 25. 4. 14.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 14.     hc               Initial creation
 */
public record MemberPasswordRequest(
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	String email,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Schema(description = "사용자 비밀번호", examples = "password123@@")
	String password
) {
}
