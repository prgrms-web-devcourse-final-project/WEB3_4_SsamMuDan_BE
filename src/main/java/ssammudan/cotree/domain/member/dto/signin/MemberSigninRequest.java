package ssammudan.cotree.domain.member.dto.signin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * PackageName : ssammudan.cotree.domain.member.dto.signin
 * FileName    : MemberSigninRequest
 * Author      : hc
 * Date        : 25. 3. 31.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.     hc               Initial creation
 */
public record MemberSigninRequest(
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	@Schema(description = "사용자 이메일", examples = "test@example.com")
	String email,

	@NotNull(message = "비밀번호를 입력해주세요.")
	@Schema(description = "사용자 비밀번호", examples = "password123@@")
	String password
) {
}
