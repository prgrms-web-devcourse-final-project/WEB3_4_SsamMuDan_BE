package ssammudan.cotree.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

/**
 * PackageName : ssammudan.cotree.global.config
 * FileName    : QuerydslConfig
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : Querydsl에서 사용하는 JPAQueryFactory 빈 등록
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 */
@Configuration
public class QuerydslConfig {

	@Bean
	public JPAQueryFactory jpaQueryFactory(final EntityManager em) {
		return new JPAQueryFactory(em);
	}

}
