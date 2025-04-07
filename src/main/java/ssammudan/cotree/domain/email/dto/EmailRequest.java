package ssammudan.cotree.domain.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * PackageName : ssammudan.cotree.domain.email.dto
 * FileName    : EmailRequest
 * Author      : hc
 * Date        : 25. 4. 6.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 6.     hc               Initial creation
 */
public record EmailRequest(
	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일을 입력해주세요.")
	String email
) {
}
