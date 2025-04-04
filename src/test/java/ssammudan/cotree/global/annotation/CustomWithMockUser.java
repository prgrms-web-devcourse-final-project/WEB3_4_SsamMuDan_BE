package ssammudan.cotree.global.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

import ssammudan.cotree.global.security.CustomWithSecurityContextFactory;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

/**
 * PackageName : ssammudan.cotree.global.annotation
 * FileName    : CustomWithMockUser
 * Author      : loadingKKamo21
 * Date        : 25. 4. 3.
 * Description : 테스트용 시큐리티 인증 객체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 3.     loadingKKamo21       Initial creation
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomWithSecurityContextFactory.class)
public @interface CustomWithMockUser {

	String id() default "6ace1641-4980-42b3-a861-38eabbe6b360";

	String email() default "test@example.com";

	String username() default "test";

	String nickname() default "test";

	MemberRole role() default MemberRole.USER;

	MemberStatus status() default MemberStatus.ACTIVE;

}
