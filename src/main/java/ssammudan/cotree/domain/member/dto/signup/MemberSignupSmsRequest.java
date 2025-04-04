package ssammudan.cotree.domain.member.dto.signup;

import jakarta.validation.constraints.Pattern;

/**
 * PackageName : ssammudan.cotree.domain.member.dto.signup
 * FileName    : SmsAuthenticationRequest
 * Author      : kwak
 * Date        : 2025. 4. 4.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 4.     kwak               Initial creation
 */
public record MemberSignupSmsRequest(

	@Pattern(
		regexp = "^01[016789]\\d{3,4}\\d{4}$",
		message = "휴대폰 번호 형식이 올바르지 않습니다."
	)
	String receiverNumber
) {
}
