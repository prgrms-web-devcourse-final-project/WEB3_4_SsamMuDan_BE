package ssammudan.cotree.model.review.reviewtype.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.integration.DataJpaTestSupporter;
import ssammudan.cotree.model.review.reviewtype.entity.TechEducationType;

import com.navercorp.fixturemonkey.api.instantiator.Instantiator;

/**
 * PackageName : ssammudan.cotree.model.review.reviewtype.repository
 * FileName    : TechEducationTypeRepositoryTest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : TechEducationType 리포지토리 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 */
class TechEducationTypeRepositoryTest extends DataJpaTestSupporter {

	private TechEducationType createTechEducationType() {
		return fixtureMonkey.giveMeBuilder(TechEducationType.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.sample();
	}

	@RepeatedTest(10)
	@DisplayName("[Success] save(): TechEducationType 엔티티 저장")
	void save() {
		//Given
		setup();

		TechEducationType techEducationType = createTechEducationType();

		//When
		Long id = techEducationTypeRepository.save(techEducationType).getId();
		clearEntityContext();

		//Then
		TechEducationType savedTechEducationType = entityManager.find(TechEducationType.class, id);

		assertNotNull(savedTechEducationType, "TechEducationType 엔티티 존재");
		assertEquals(techEducationType.getName(), savedTechEducationType.getName(), "교육 컨텐츠 타입 일치");
	}

	@RepeatedTest(10)
	@DisplayName("[Success] findById(): TechEducationType 엔티티 단 건 조회")
	void findById() {
		//Given
		setup();

		TechEducationType techEducationType = createTechEducationType();
		entityManager.persist(techEducationType);
		Long id = techEducationType.getId();
		clearEntityContext();

		//When
		TechEducationType savedTechEducationType = techEducationTypeRepository.findById(id).get();

		//Then
		assertNotNull(savedTechEducationType, "TechEducationType 엔티티 존재");
		assertEquals(techEducationType.getName(), savedTechEducationType.getName(), "교육 컨텐츠 타입 일치");
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Failure] findById_unknownId(): TechEducationType 엔티티 단 건 조회, 존재하지 않는 ID")
	void findById_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given

		//When
		Optional<TechEducationType> opTechEducationType = techEducationTypeRepository.findById(unknownId);

		//Then
		assertFalse(opTechEducationType.isPresent(), "TechEducationType 엔티티 존재하지 않음");
	}

	@RepeatedTest(10)
	@DisplayName("[Success] findByNameIgnoreCase(): TechEducationType 엔티티 단 건 조회")
	void findByNameIgnoreCase() {
		//Given
		setup();

		String name = fixtureMonkey.giveMeOne(String.class);
		TechEducationType techEducationType = TechEducationType.create(name);
		entityManager.persist(techEducationType);
		clearEntityContext();

		//When
		TechEducationType savedTechEducationType = techEducationTypeRepository.findByNameIgnoreCase(name).get();

		//Then
		assertNotNull(savedTechEducationType, "TechEducationType 엔티티 존재");
		assertEquals(techEducationType.getName(), savedTechEducationType.getName(), "교육 컨텐츠 타입 일치");
	}

	@RepeatedTest(10)
	@DisplayName("[Failure] findByNameIgnoreCase_unknownName(): TechEducationType 엔티티 단 건 조회, 존재하지 않는 컨텐츠 명칭")
	void findByNameIgnoreCase_unknownName() {
		//Given
		String name = fixtureMonkey.giveMeOne(String.class);

		//When
		Optional<TechEducationType> opTechEducationType = techEducationTypeRepository.findByNameIgnoreCase(name);

		//Then
		assertFalse(opTechEducationType.isPresent(), "TechEducationType 엔티티 존재하지 않음");
	}

}
