package ssammudan.cotree.integration.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * PackageName : ssammudan.cotree.integration.security
 * FileName    : WithCustomUser
 * Author      : Baekgwa
 * Date        : 2025-04-11
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-11     Baekgwa               Initial creation
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomUserSecurityContextFactory.class)
public @interface WithCustomUser {
}
