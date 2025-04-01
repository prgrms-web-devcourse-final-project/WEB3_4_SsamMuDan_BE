package ssammudan.cotree.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import ssammudan.cotree.global.annotation.RepositoryTest;
import ssammudan.cotree.model.education.level.repository.EducationLevelRepository;
import ssammudan.cotree.model.education.techbook.techbook.repository.TechBookRepository;
import ssammudan.cotree.model.review.reviewtype.repository.TechEducationTypeRepository;

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
 * 25. 4. 1.     loadingKKamo21       TestEntityManager -> EntityManager 변경, @BeforeEach 메서드 추가
 */
@ActiveProfiles("test")
@RepositoryTest
public abstract class DataJpaTestSupporter {

	protected final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.defaultNotNull(true)
		.build();

	@Autowired
	protected EntityManager entityManager;

	@Autowired
	protected TechBookRepository techBookRepository;

	@Autowired
	protected EducationLevelRepository educationLevelRepository;

	@Autowired
	protected TechEducationTypeRepository techEducationTypeRepository;

	@BeforeEach
	protected void setup() {
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();    //H2 DB 외래키 제약 해제

		entityManager.createNativeQuery("TRUNCATE TABLE member RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE education_level RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE tech_book RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE techEducation_type RESTART IDENTITY").executeUpdate();

		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();    //H2 DB 외래키 제약 설정
	}

	@AfterEach
	protected void clearEntityContext() {
		entityManager.flush();
		entityManager.clear();
	}

}
