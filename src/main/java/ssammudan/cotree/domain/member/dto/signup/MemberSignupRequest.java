package ssammudan.cotree.domain.member.dto.signup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * PackageName : ssammudan.cotree.domain.member.dto.signup
 * FileName    : MemberSignupRequest
 * Author      : hc
 * Date        : 25. 3. 28.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.     hc               Initial creation
 */

public record MemberSignupRequest(
	@NotNull(message = "이메일을 입력해주세요.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	@Schema(description = "사용자 이메일", examples = "test@example.com")
	String email,

	@NotNull(message = "비밀번호를 입력해주세요.") // todo : 비밀번호 정규식 추가
	@Schema(description = "사용자 비밀번호", examples = "password123@@")
	String password,

	@NotNull(message = "이름을 입력해주세요.") // todo : 이름 길이 제한
	@Schema(description = "사용자 이름", examples = "홍길동")
	String name,

	@NotNull(message = "닉네임을 입력해주세요.") // todo : 닉네임 길이 제한
	@Schema(description = "사용자 닉네임", examples = "MrHong")
	String nickname,

	@NotNull(message = "휴대폰 번호를 입력해주세요.")
	@Pattern(regexp = "^01[016789][0-9]{7,8}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
	@Schema(description = "사용자 휴대폰 번호", example = "01012345678", pattern = "^01[016789][0-9]{7,8}$")
	String phoneNumber
) {
}
