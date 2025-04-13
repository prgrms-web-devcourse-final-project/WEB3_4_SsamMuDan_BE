package ssammudan.cotree.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.persistence.EntityManager;
import ssammudan.cotree.global.annotation.RepositoryTest;
import ssammudan.cotree.model.education.category.repository.EducationCategoryRepository;
import ssammudan.cotree.model.education.level.repository.EducationLevelRepository;
import ssammudan.cotree.model.education.techbook.category.repository.TechBookEducationCategoryRepository;
import ssammudan.cotree.model.education.techbook.techbook.repository.TechBookRepository;
import ssammudan.cotree.model.education.techtube.category.repository.TechTubeEducationCategoryRepository;
import ssammudan.cotree.model.education.techtube.techtube.repository.TechTubeRepository;
import ssammudan.cotree.model.project.project.mapper.ProjectMapper;
import ssammudan.cotree.model.project.project.repository.support.ProjectQuerySupport;
import ssammudan.cotree.model.review.review.repository.TechEducationReviewRepository;
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
	protected TechTubeRepository techTubeRepository;

	@Autowired
	protected EducationLevelRepository educationLevelRepository;

	@Autowired
	protected EducationCategoryRepository educationCategoryRepository;

	@Autowired
	protected TechEducationTypeRepository techEducationTypeRepository;

	@Autowired
	protected TechEducationReviewRepository techEducationReviewRepository;

	@Autowired
	protected TechBookEducationCategoryRepository techBookEducationCategoryRepository;

	@Autowired
	protected TechTubeEducationCategoryRepository techTubeEducationCategoryRepository;

	@MockitoBean
	protected ProjectQuerySupport projectQuerySupport;

	@MockitoBean
	protected ProjectMapper projectMapper;

	@BeforeEach
	protected void setup() {
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();    //H2 DB 외래키 제약 해제

		entityManager.createNativeQuery("TRUNCATE TABLE member RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE education_level RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE education_category RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE tech_tube RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE tech_book RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE techEducation_type RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE techEducation_review RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE techTube_educationCategory RESTART IDENTITY").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE techBook_educationCategory RESTART IDENTITY").executeUpdate();

		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();    //H2 DB 외래키 제약 설정
	}

	@AfterEach
	protected void clearEntityContext() {
		entityManager.flush();
		entityManager.clear();
	}

}
