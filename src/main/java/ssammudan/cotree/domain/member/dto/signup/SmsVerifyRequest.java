package ssammudan.cotree.domain.member.dto.signup;

import jakarta.validation.constraints.Pattern;

/**
 * PackageName : ssammudan.cotree.domain.member.dto.signup
 * FileName    : SmsVerifyRequest
 * Author      : kwak
 * Date        : 2025. 4. 4.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 4.     kwak               Initial creation
 */
public record SmsVerifyRequest(
	@Pattern(
		regexp = "^01[016789]\\d{3,4}\\d{4}$",
		message = "휴대폰 번호 형식이 올바르지 않습니다."
	)
	String receiverNumber,
	@Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리 숫자여야 합니다.")
	String code
) {
}
