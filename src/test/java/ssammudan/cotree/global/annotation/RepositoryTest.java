package ssammudan.cotree.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import ssammudan.cotree.global.config.EnableJpaAuditingConfig;
import ssammudan.cotree.global.config.QuerydslConfig;

/**
 * PackageName : ssammudan.cotree.global.annotation
 * FileName    : RepositoryTest
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : JPA 리포지토리 테스트용
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21               Initial creation
 */
@DataJpaTest
@Import({EnableJpaAuditingConfig.class, QuerydslConfig.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RepositoryTest {
}
