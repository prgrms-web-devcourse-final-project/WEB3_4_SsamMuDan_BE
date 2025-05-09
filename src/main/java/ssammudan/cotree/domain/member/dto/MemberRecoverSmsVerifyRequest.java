package ssammudan.cotree.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * PackageName : ssammudan.cotree.domain.member.dto
 * FileName    : MemberRecoverSmsVeriftRequest
 * Author      : kwak
 * Date        : 2025. 4. 7.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 7.     kwak               Initial creation
 */
public record MemberRecoverSmsVerifyRequest(
	@NotBlank(message = "가입하신 회원 이름을 입력해주세요.")
	String username,

	@Pattern(
		regexp = "^01[016789]\\d{3,4}\\d{4}$",
		message = "휴대폰 번호 형식이 올바르지 않습니다."
	)
	String receiverNumber,

	@Pattern(
		regexp = "^\\d{6}$",
		message = "인증번호는 6자리 숫자여야 합니다.")
	String code
) {
}
