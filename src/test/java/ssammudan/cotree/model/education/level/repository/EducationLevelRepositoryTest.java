package ssammudan.cotree.model.education.level.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import net.jqwik.api.Arbitraries;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.integration.DataJpaTestSupporter;
import ssammudan.cotree.model.education.level.entity.EducationLevel;

import com.navercorp.fixturemonkey.api.instantiator.Instantiator;

/**
 * PackageName : ssammudan.cotree.model.education.level.repository
 * FileName    : EducationLevelRepositoryTest
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : EducationLevel 리포지토리 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
class EducationLevelRepositoryTest extends DataJpaTestSupporter {

	private EducationLevel createEducationLevel() {
		return fixtureMonkey.giveMeBuilder(EducationLevel.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("name", Arbitraries.of("입문", "초급", "중급"))
			.sample();
	}

	@RepeatedTest(10)
	@DisplayName("[Success] save(): EducationLevel 엔티티 저장")
	void save() {
		//Given
		setup();

		EducationLevel educationLevel = createEducationLevel();

		//When
		Long id = educationLevelRepository.save(educationLevel).getId();
		clearEntityContext();

		//Then
		EducationLevel savedEducationLevel = entityManager.find(EducationLevel.class, id);

		assertNotNull(savedEducationLevel, "EducationLevel 엔티티 존재");
		assertEquals(educationLevel.getName(), savedEducationLevel.getName(), "학습 난이도 일치");
	}

	@RepeatedTest(10)
	@DisplayName("[Success] findById(): EducationLevel 엔티티 단 건 조회")
	void findById() {
		//Given
		setup();

		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		Long id = educationLevel.getId();
		clearEntityContext();

		//When
		EducationLevel savedEducationLevel = educationLevelRepository.findById(id).get();

		//Then
		assertNotNull(savedEducationLevel, "EducationLevel 엔티티 존재");
		assertEquals(educationLevel.getName(), savedEducationLevel.getName(), "학습 난이도 일치");
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Failure] findById_unknownId(): EducationLevel 엔티티 단 건 조회, 존재하지 않는 ID")
	void findById_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given

		//When
		Optional<EducationLevel> opEducationLevel = educationLevelRepository.findById(unknownId);

		//Then
		assertFalse(opEducationLevel.isPresent(), "EducationLevel 엔티티 존재하지 않음");
	}

	@RepeatedTest(10)
	@DisplayName("[Success] findByNameIgnoreCase(): EducationLevel 엔티티 단 건 조회")
	void findByNameIgnoreCase() {
		//Given
		setup();

		String name = fixtureMonkey.giveMeOne(String.class);
		EducationLevel educationLevel = EducationLevel.create(name);
		entityManager.persist(educationLevel);
		clearEntityContext();

		//When
		EducationLevel savedEducationLevel = educationLevelRepository.findByNameIgnoreCase(name).get();

		//Then
		assertNotNull(savedEducationLevel, "EducationLevel 엔티티 존재");
		assertEquals(educationLevel.getName(), savedEducationLevel.getName(), "학습 난이도 일치");
	}

	@RepeatedTest(10)
	@DisplayName("[Failure] findByNameIgnoreCase_unknownName(): EducationLevel 엔티티 단 건 조회, 존재하지 않는 난이도 명칭")
	void findByNameIgnoreCase_unknownName() {
		//Given
		String name = fixtureMonkey.giveMeOne(String.class);

		//When
		Optional<EducationLevel> opEducationLevel = educationLevelRepository.findByNameIgnoreCase(name);

		//Then
		assertFalse(opEducationLevel.isPresent(), "EducationLevel 엔티티 존재하지 않음");
	}

	@Test
	@DisplayName("[Success] findAllByNameContainsIgnoreCase(): EducationLevel 엔티티 다 건 조회, 키워드 사용")
	void findAllByNameContainsIgnoreCase() {
		//Given
		List.of("입문", "초급", "중급").forEach(name -> entityManager.persist(EducationLevel.create(name)));
		clearEntityContext();

		String keyword = "급";

		//When
		List<EducationLevel> findEducationLevels = educationLevelRepository.findAllByNameContainsIgnoreCase(keyword);

		//Then
		assertEquals(2, findEducationLevels.size(), "검색 결과는 총 2개");
		findEducationLevels.forEach(educationLevel -> {
			assertNotNull(educationLevel, "EducationLevel 엔티티 존재");
			assertTrue(educationLevel.getName().contains(keyword), "학습 난이도 명칭에 검색어 '급' 포함");
		});
	}

}
