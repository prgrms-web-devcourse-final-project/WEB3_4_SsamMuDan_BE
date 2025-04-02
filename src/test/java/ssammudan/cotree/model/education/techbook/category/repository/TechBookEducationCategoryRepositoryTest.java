package ssammudan.cotree.model.education.techbook.category.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;

import net.jqwik.api.Arbitraries;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.integration.DataJpaTestSupporter;
import ssammudan.cotree.model.education.category.entity.EducationCategory;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.techbook.category.entity.TechBookEducationCategory;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;
import ssammudan.cotree.model.member.member.entity.Member;

import com.navercorp.fixturemonkey.api.instantiator.Instantiator;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.category.repository
 * FileName    : TechBookEducationCategoryRepositoryTest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : TechBookEducationCategory 리포지토리 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 */
class TechBookEducationCategoryRepositoryTest extends DataJpaTestSupporter {

	private Member createMember() {
		return fixtureMonkey.giveMeBuilder(Member.class)
			.setNull("id")
			.sample();
	}

	private EducationLevel createEducationLevel() {
		return fixtureMonkey.giveMeBuilder(EducationLevel.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.sample();
	}

	private TechBook createTechBook(final Member member, final EducationLevel educationLevel) {
		return fixtureMonkey.giveMeBuilder(TechBook.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("writer", member)
			.set("educationLevel", educationLevel)
			.set("techBookPage", Arbitraries.integers().greaterOrEqual(0))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.sample();
	}

	private EducationCategory createEducationCategory() {
		return fixtureMonkey.giveMeBuilder(EducationCategory.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.sample();
	}

	private TechBookEducationCategory createTechBookEducationCategory(
		final TechBook techBook, final EducationCategory educationCategory
	) {
		return TechBookEducationCategory.create(techBook, educationCategory);
	}

	@RepeatedTest(10)
	@DisplayName("[Success] save(): TechBookEducationCategory 엔티티 저장")
	void save() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		TechBook techBook = createTechBook(member, educationLevel);
		entityManager.persist(techBook);
		EducationCategory educationCategory = createEducationCategory();
		entityManager.persist(educationCategory);
		TechBookEducationCategory techBookEducationCategory = createTechBookEducationCategory(
			techBook, educationCategory
		);

		//When
		Long id = techBookEducationCategoryRepository.save(techBookEducationCategory).getId();
		clearEntityContext();

		//Then
		TechBookEducationCategory savedTechBookEducationCategory = entityManager.find(
			TechBookEducationCategory.class, id
		);

		assertNotNull(savedTechBookEducationCategory, "TechBookEducationCategory 엔티티 존재");
		assertEquals(
			techBookEducationCategory.getTechBook().getId(),
			savedTechBookEducationCategory.getTechBook().getId(),
			"TechBook 엔티티 일치");
		assertEquals(
			techBookEducationCategory.getEducationCategory().getId(),
			savedTechBookEducationCategory.getEducationCategory().getId(),
			"EducationCategory 엔티티 일치"
		);
	}

	@RepeatedTest(10)
	@DisplayName("[Success] findById(): TechBookEducationCategory 엔티티 단 건 조회")
	void findById() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		TechBook techBook = createTechBook(member, educationLevel);
		entityManager.persist(techBook);
		EducationCategory educationCategory = createEducationCategory();
		entityManager.persist(educationCategory);
		TechBookEducationCategory techBookEducationCategory = createTechBookEducationCategory(
			techBook, educationCategory
		);
		entityManager.persist(techBookEducationCategory);
		Long id = techBookEducationCategory.getId();
		clearEntityContext();

		//When
		TechBookEducationCategory savedTechBookEducationCategory = techBookEducationCategoryRepository.findById(id)
			.get();

		//Then
		assertNotNull(savedTechBookEducationCategory, "TechBookEducationCategory 엔티티 존재");
		assertEquals(
			techBookEducationCategory.getTechBook().getId(),
			savedTechBookEducationCategory.getTechBook().getId(),
			"TechBook 엔티티 일치");
		assertEquals(
			techBookEducationCategory.getEducationCategory().getId(),
			savedTechBookEducationCategory.getEducationCategory().getId(),
			"EducationCategory 엔티티 일치"
		);
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Failure] findById_unknownId(): TechBookEducationCategory 엔티티 단 건 조회, 존재하지 않는 ID")
	void findById_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given

		//When
		Optional<TechBookEducationCategory> opTechBookEducationCategory = techBookEducationCategoryRepository.findById(
			unknownId);

		//Then
		assertFalse(opTechBookEducationCategory.isPresent(), "TechBookEducationCategory 엔티티 존재하지 않음");
	}

}
