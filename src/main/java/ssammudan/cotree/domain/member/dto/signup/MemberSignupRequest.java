package ssammudan.cotree.domain.member.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

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
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	String email,

	@NotNull(message = "비밀번호를 입력해주세요.") // todo : 비밀번호 정규식 추가
	String password,

	@NotNull(message = "이름을 입력해주세요.") // todo : 이름 길이 제한
	String name,

	@NotNull(message = "닉네임을 입력해주세요.") // todo : 닉네임 길이 제한
	String nickname,

	@NotNull(message = "휴대폰 번호를 입력해주세요.") // todo : 휴대폰 번호 정규식 추가
	String phoneNumber
) {
};
