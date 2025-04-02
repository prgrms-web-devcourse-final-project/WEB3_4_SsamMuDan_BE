package ssammudan.cotree.model.education.category.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.integration.DataJpaTestSupporter;
import ssammudan.cotree.model.education.category.entity.EducationCategory;

import com.navercorp.fixturemonkey.api.instantiator.Instantiator;

/**
 * PackageName : ssammudan.cotree.model.education.category.repository
 * FileName    : EducationCategoryRepositoryTest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 1.
 * Description : EducationCategory 리포지토리 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     loadingKKamo21               Initial creation
 */
class EducationCategoryRepositoryTest extends DataJpaTestSupporter {

	private EducationCategory createEducationCategory() {
		return fixtureMonkey.giveMeBuilder(EducationCategory.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.sample();
	}

	@RepeatedTest(10)
	@DisplayName("[Success] save(): EducationCategory 엔티티 저장")
	void save() {
		//Given
		setup();

		EducationCategory educationCategory = createEducationCategory();

		//When
		Long id = educationCategoryRepository.save(educationCategory).getId();
		clearEntityContext();

		//Then
		EducationCategory savedEducationCategory = entityManager.find(EducationCategory.class, id);

		assertNotNull(savedEducationCategory, "EducationCategory 엔티티 존재");
		assertEquals(educationCategory.getName(), savedEducationCategory.getName(), "교육 카테고리 일치");
	}

	@RepeatedTest(10)
	@DisplayName("[Success] findById(): EducationCategory 엔티티 단 건 조회")
	void findById() {
		//Given
		setup();

		EducationCategory educationCategory = createEducationCategory();
		entityManager.persist(educationCategory);
		Long id = educationCategory.getId();
		clearEntityContext();

		//When
		EducationCategory savedEducationCategory = educationCategoryRepository.findById(id).get();

		//Then
		assertNotNull(savedEducationCategory, "EducationCategory 엔티티 존재");
		assertEquals(educationCategory.getName(), savedEducationCategory.getName(), "교육 카테고리 일치");
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Failure] findById_unknownId(): EducationCategory 엔티티 단 건 조회, 존재하지 않는 ID")
	void findById_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given

		//When
		Optional<EducationCategory> opEducationCategory = educationCategoryRepository.findById(unknownId);

		//Then
		assertFalse(opEducationCategory.isPresent(), "EducationCategory 엔티티 존재하지 않음");
	}

	@RepeatedTest(10)
	@DisplayName("[Success] findByNameIgnoreCase(): EducationCategory 엔티티 단 건 조회")
	void findByNameIgnoreCase() {
		//Given
		setup();

		String name = fixtureMonkey.giveMeOne(String.class);
		EducationCategory educationCategory = EducationCategory.create(name);
		entityManager.persist(educationCategory);
		clearEntityContext();

		//When
		EducationCategory savedEducationCategory = educationCategoryRepository.findByNameIgnoreCase(name).get();

		//Then
		assertNotNull(savedEducationCategory, "EducationCategory 엔티티 존재");
		assertEquals(educationCategory.getName(), savedEducationCategory.getName(), "교육 카테고리 일치");
	}

	@RepeatedTest(10)
	@DisplayName("[Failure] findByNameIgnoreCase_unknownName(): EducationCategory 엔티티 단 건 조회, 존재하지 않는 카테고리 명칭")
	void findByNameIgnoreCase_unknownName() {
		//Given
		String name = fixtureMonkey.giveMeOne(String.class);

		//When
		Optional<EducationCategory> opEducationCategory = educationCategoryRepository.findByNameIgnoreCase(name);

		//Then
		assertFalse(opEducationCategory.isPresent(), "EducationCategory 엔티티 존재하지 않음");
	}

	@Test
	@DisplayName("[Success] findAllByNameContainsIgnoreCase(): EducationCategory 엔티티 다 건 조회, 키워드 사용")
	void findAllByNameContainsIgnoreCase() {
		//Given
		List.of("백엔드", "프론트엔드", "웹 개발", "데이터", "프로그래밍 언어", "자료구조/알고리즘", "개발도구", "모바일")
			.forEach(name -> entityManager.persist(EducationCategory.create(name)));

		String keyword = "엔드";

		//When
		List<EducationCategory> findEducationCategories = educationCategoryRepository.findAllByNameContainsIgnoreCase(
			keyword
		);

		//Then
		assertEquals(2, findEducationCategories.size(), "검색 결과는 총 2개");
		findEducationCategories.forEach(educationCategory -> {
			assertNotNull(educationCategory, "EducationCategory 엔티티 존재");
			assertTrue(educationCategory.getName().contains(keyword), "교육 카테고리 명칭에 검색어 '엔드' 포함");
		});
	}

}
