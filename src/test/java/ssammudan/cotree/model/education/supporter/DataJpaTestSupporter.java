package ssammudan.cotree.model.education.supporter;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import ssammudan.cotree.global.annotation.RepositoryTest;
import ssammudan.cotree.model.education.level.repository.EducationLevelRepository;
import ssammudan.cotree.model.education.techbook.techbook.repository.TechBookRepository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

/**
 * PackageName : ssammudan.cotree.model.education.supporter
 * FileName    : DataJpaTestSupporter
 * Author      : loadingKKamo21
 * Date        : 25. 3. 31.
 * Description : @DataJpaTest 활용 테스트 지원
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.    loadingKKamo21       Initial creation
 */
@ActiveProfiles("test")
@RepositoryTest
public abstract class DataJpaTestSupporter {

	protected final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.defaultNotNull(true)
		.build();

	@Autowired
	protected TestEntityManager entityManager;

	@Autowired
	protected TechBookRepository techBookRepository;

	@Autowired
	protected EducationLevelRepository educationLevelRepository;

	@AfterEach
	protected void clearEntityContext() {
		entityManager.flush();
		entityManager.clear();
	}

}
