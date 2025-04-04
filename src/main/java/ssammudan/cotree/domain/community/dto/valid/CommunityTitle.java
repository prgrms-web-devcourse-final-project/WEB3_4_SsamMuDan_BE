package ssammudan.cotree.domain.community.dto.valid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

/**
 * PackageName : ssammudan.cotree.domain.community.dto
 * FileName    : CommunityTitle
 * Author      : Baekgwa
 * Date        : 2025-04-04
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-04     Baekgwa               Initial creation
 */
@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 1, max = 50, message = "제목은 1자 이상 50자 이하로 입력해주세요.")
public @interface CommunityTitle {
	String message() default "유효하지 않은 제목입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
