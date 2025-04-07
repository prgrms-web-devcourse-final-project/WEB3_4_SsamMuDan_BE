package ssammudan.cotree.domain.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * PackageName : ssammudan.cotree.domain.email.dto
 * FileName    : EmailVerifyRequest
 * Author      : hc
 * Date        : 25. 4. 6.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 6.     hc               Initial creation
 */
public record EmailVerifyRequest(
	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일을 입력해주세요.")
	String email,
	@NotBlank(message = "인증코드를 입력해주세요.")
	@Pattern(regexp = "^\\d{6}$", message = "6자리 숫자여야 합니다.")
	String code
) {
}
