package ssammudan.cotree.model.education.techtube.category.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import net.jqwik.api.Arbitraries;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.integration.DataJpaTestSupporter;
import ssammudan.cotree.model.education.category.entity.EducationCategory;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.techtube.category.entity.TechTubeEducationCategory;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

import com.navercorp.fixturemonkey.api.instantiator.Instantiator;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.category.repository
 * FileName    : TechTubeEducationCategoryRepositoryTest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : TechTubeEducationCategory 리포지토리 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 */
class TechTubeEducationCategoryRepositoryTest extends DataJpaTestSupporter {

	private Member createMember() {
		return fixtureMonkey.giveMeBuilder(Member.class)
			.setNull("id")
			.set("email", Arbitraries.strings()
				.withCharRange('a', 'z')
				.ofMinLength(1)
				.ofMaxLength(219)
				.map(s -> s + UUID.randomUUID()))
			.set("username", Arbitraries.strings()
				.withCharRange('a', 'z')
				.ofMinLength(1)
				.ofMaxLength(219)
				.map(s -> s + UUID.randomUUID()))
			.set("nickname", Arbitraries.strings()
				.withCharRange('a', 'z')
				.ofMinLength(1)
				.ofMaxLength(219)
				.map(s -> s + UUID.randomUUID()))
			.set("password", Arbitraries.strings()
				.withCharRange('a', 'z')
				.ofMinLength(1)
				.ofMaxLength(219)
				.map(s -> s + UUID.randomUUID()))
			.set("phoneNumber",
				Arbitraries.strings()
					.withCharRange('a', 'z')
					.ofMinLength(1)
					.ofMaxLength(219)
					.map(s -> s + UUID.randomUUID()))
			.set("role", MemberRole.USER)
			.set("memberStatus", MemberStatus.ACTIVE)
			.sample();
	}

	private EducationLevel createEducationLevel() {
		return fixtureMonkey.giveMeBuilder(EducationLevel.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("name", Arbitraries.of("입문", "초급", "중급"))
			.sample();
	}

	private TechTube createTechTube(final Member member, final EducationLevel educationLevel) {
		return fixtureMonkey.giveMeBuilder(TechTube.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("writer", member)
			.set("educationLevel", educationLevel)
			.set("techTubeDuration", Arbitraries.longs().greaterOrEqual(0).map(Duration::ofSeconds))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.sample();
	}

	private EducationCategory createEducationCategory() {
		return fixtureMonkey.giveMeBuilder(EducationCategory.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.sample();
	}

	private TechTubeEducationCategory createTechTubeEducationCategory(
		final TechTube techTube, final EducationCategory educationCategory
	) {
		return TechTubeEducationCategory.create(techTube, educationCategory);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] save(): TechTubeEducationCategory 엔티티 저장")
	void save() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		TechTube techTube = createTechTube(member, educationLevel);
		entityManager.persist(techTube);
		EducationCategory educationCategory = createEducationCategory();
		entityManager.persist(educationCategory);
		TechTubeEducationCategory techTubeEducationCategory = createTechTubeEducationCategory(
			techTube, educationCategory
		);

		//When
		Long id = techTubeEducationCategoryRepository.save(techTubeEducationCategory).getId();
		clearEntityContext();

		//Then
		TechTubeEducationCategory savedTechTubeEducationCategory = entityManager.find(
			TechTubeEducationCategory.class, id
		);

		assertNotNull(savedTechTubeEducationCategory, "TechTubeEducationCategory 엔티티 존재");
		assertEquals(
			techTubeEducationCategory.getTechTube().getId(),
			savedTechTubeEducationCategory.getTechTube().getId(),
			"TechTube 엔티티 일치");
		assertEquals(
			techTubeEducationCategory.getEducationCategory().getId(),
			savedTechTubeEducationCategory.getEducationCategory().getId(),
			"EducationCategory 엔티티 일치"
		);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findById(): TechTubeEducationCategory 엔티티 단 건 조회")
	void findById() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		TechTube techTube = createTechTube(member, educationLevel);
		entityManager.persist(techTube);
		EducationCategory educationCategory = createEducationCategory();
		entityManager.persist(educationCategory);
		TechTubeEducationCategory techTubeEducationCategory = createTechTubeEducationCategory(
			techTube, educationCategory
		);
		entityManager.persist(techTubeEducationCategory);
		Long id = techTubeEducationCategory.getId();
		clearEntityContext();

		//When
		TechTubeEducationCategory savedTechTubeEducationCategory = techTubeEducationCategoryRepository.findById(id)
			.get();

		//Then
		assertNotNull(savedTechTubeEducationCategory, "TechTubeEducationCategory 엔티티 존재");
		assertEquals(
			techTubeEducationCategory.getTechTube().getId(),
			savedTechTubeEducationCategory.getTechTube().getId(),
			"TechTube 엔티티 일치");
		assertEquals(
			techTubeEducationCategory.getEducationCategory().getId(),
			savedTechTubeEducationCategory.getEducationCategory().getId(),
			"EducationCategory 엔티티 일치"
		);
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Failure] findById_unknownId(): TechTubeEducationCategory 엔티티 단 건 조회, 존재하지 않는 ID")
	void findById_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given

		//When
		Optional<TechTubeEducationCategory> opTechTubeEducationCategory = techTubeEducationCategoryRepository.findById(
			unknownId);

		//Then
		assertFalse(opTechTubeEducationCategory.isPresent(), "TechTubeEducationCategory 엔티티 존재하지 않음");
	}
}
