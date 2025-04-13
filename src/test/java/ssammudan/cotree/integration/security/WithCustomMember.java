package ssammudan.cotree.integration.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * PackageName : ssammudan.cotree.integration.security
 * FileName    : WithCustomMember
 * Author      : Baekgwa
 * Date        : 2025-04-11
 * Description : 무작위 회원을 한명 생성하여 저장 후, securityContext 에 저장합니다.
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-11     Baekgwa               Initial creation
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMemberSecurityContextFactory.class)
public @interface WithCustomMember {
}
