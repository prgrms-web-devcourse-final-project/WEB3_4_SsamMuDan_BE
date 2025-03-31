package ssammudan.cotree.domain.education.supporter;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import ssammudan.cotree.domain.education.techbook.service.TechBookService;
import ssammudan.cotree.model.education.techbook.techbook.repository.TechBookRepository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

/**
 * PackageName : ssammudan.cotree.model.education.supporter
 * FileName    : SpringBootTestSupporter
 * Author      : loadingKKamo21
 * Date        : 25. 3. 31.
 * Description : @SpringBootTest 활용 테스트 지원
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.    loadingKKamo21       Initial creation
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public abstract class SpringBootTestSupporter {

	protected final FixtureMonkey entityFixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.defaultNotNull(true)
		.build();

	protected final FixtureMonkey dtoFixtureMonkey = FixtureMonkey.builder()
		.plugin(new JakartaValidationPlugin())
		.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
		.build();

	@Autowired
	protected EntityManager entityManager;

	@Autowired
	protected TechBookService techBookService;

	@Autowired
	protected TechBookRepository techBookRepository;

	@AfterEach
	protected void clearEntityContext() {
		entityManager.flush();
		entityManager.clear();
	}

}
