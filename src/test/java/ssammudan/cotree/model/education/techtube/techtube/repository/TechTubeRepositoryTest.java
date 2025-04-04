package ssammudan.cotree.model.education.techtube.techtube.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import net.jqwik.api.Arbitraries;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.integration.DataJpaTestSupporter;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

import com.navercorp.fixturemonkey.api.instantiator.Instantiator;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.techtube.repository
 * FileName    : TechTubeRepositoryTest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : TechTube 리포지토리 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 */
class TechTubeRepositoryTest extends DataJpaTestSupporter {

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

	private List<TechTube> createTechTubes(final Member member, final EducationLevel educationLevel, final int size) {
		return fixtureMonkey.giveMeBuilder(TechTube.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("writer", member)
			.set("educationLevel", educationLevel)
			.set("techTubeDuration", Arbitraries.longs().greaterOrEqual(0).map(Duration::ofSeconds))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.sampleList(size);
	}

	private void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] save(): TechTube 엔티티 저장")
	void save() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		TechTube techTube = createTechTube(member, educationLevel);

		//When
		Long id = techTubeRepository.save(techTube).getId();
		clearEntityContext();

		//Then
		TechTube savedTechTube = entityManager.find(TechTube.class, id);

		assertNotNull(savedTechTube, "TechTube 엔티티 존재");
		assertEquals(techTube.getWriter().getId(), savedTechTube.getWriter().getId(), "저자 일치");
		assertEquals(techTube.getEducationLevel().getId(), savedTechTube.getEducationLevel().getId(), "학습 난이도 일치");
		assertEquals(techTube.getTitle(), savedTechTube.getTitle(), "제목 일치");
		assertEquals(techTube.getDescription(), savedTechTube.getDescription(), "설명 일치");
		assertEquals(techTube.getIntroduction(), savedTechTube.getIntroduction(), "소개 일치");
		assertEquals(techTube.getTechTubeUrl(), savedTechTube.getTechTubeUrl(), "영상 URL 일치");
		assertEquals(techTube.getTechTubeDuration(), savedTechTube.getTechTubeDuration(), "영상 길이 일치");
		assertEquals(techTube.getTechTubeThumbnailUrl(), savedTechTube.getTechTubeThumbnailUrl(), "썸네일 URL 일치");
		assertEquals(techTube.getPrice(), savedTechTube.getPrice(), "가격 일치");
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findById(): TechTube 엔티티 단 건 조회")
	void findById() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		TechTube techTube = createTechTube(member, educationLevel);
		entityManager.persist(techTube);
		Long id = techTube.getId();
		clearEntityContext();

		//When
		TechTube savedTechTube = techTubeRepository.findById(id).get();

		//Then
		assertNotNull(savedTechTube, "TechTube 엔티티 존재");
		assertEquals(techTube.getWriter().getId(), savedTechTube.getWriter().getId(), "저자 일치");
		assertEquals(techTube.getEducationLevel().getId(), savedTechTube.getEducationLevel().getId(), "학습 난이도 일치");
		assertEquals(techTube.getTitle(), savedTechTube.getTitle(), "제목 일치");
		assertEquals(techTube.getDescription(), savedTechTube.getDescription(), "설명 일치");
		assertEquals(techTube.getIntroduction(), savedTechTube.getIntroduction(), "소개 일치");
		assertEquals(techTube.getTechTubeUrl(), savedTechTube.getTechTubeUrl(), "영상 URL 일치");
		assertEquals(techTube.getTechTubeDuration(), savedTechTube.getTechTubeDuration(), "영상 길이 일치");
		assertEquals(techTube.getTechTubeThumbnailUrl(), savedTechTube.getTechTubeThumbnailUrl(), "썸네일 URL 일치");
		assertEquals(techTube.getPrice(), savedTechTube.getPrice(), "가격 일치");
	}

	@ParameterizedTest
	@Repeat(10)
	@AutoSource
	@DisplayName("[Failure] findById_unknownId(): TechTube 단 건 조회, 존재하지 않는 ID")
	void findById_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given

		//When
		Optional<TechTube> opTechTube = techTubeRepository.findById(unknownId);

		//Then
		assertFalse(opTechTube.isPresent(), "TechTube 엔티티 존재하지 않음");
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findAllTechTubesByKeyword(): TechTube 다 건 조회, 페이징 적용")
	void findAllTechBooksByKeyword() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		List<TechTube> techTubes = createTechTubes(member, educationLevel, 50);
		techTubes.forEach(techBook -> {
			sleep(1);
			entityManager.persist(techBook);
		});
		clearEntityContext();

		String keyword = fixtureMonkey.giveMeOne(String.class);
		Pageable pageable = PageRequest.of(0, 16, Sort.Direction.DESC, "createdAt");

		//When
		List<TechTube> findAllTechTubes = techTubeRepository.findAllTechTubesByKeyword(keyword, pageable).getContent();

		//Then
		List<TechTube> filteredTechTubes = techTubes.stream()
			.filter(techBook ->
				!StringUtils.hasText(keyword) || (techBook.getTitle().contains(keyword)
					|| techBook.getDescription().contains(keyword)
					|| techBook.getIntroduction().contains(keyword))
			)
			.sorted(Comparator.comparing(TechTube::getCreatedAt).reversed()
				.thenComparing(tube -> tube.getCreatedAt().truncatedTo(ChronoUnit.MILLIS), Comparator.reverseOrder()))
			.limit(pageable.getPageSize())
			.toList();

		assertEquals(filteredTechTubes.size(), findAllTechTubes.size(), "검색 결과 갯수 일치");
		for (int i = 0; i < filteredTechTubes.size(); i++) {
			TechTube filteredTechTube = filteredTechTubes.get(i);
			TechTube findTechTube = findAllTechTubes.get(i);

			assertEquals(filteredTechTube.getId(), findTechTube.getId(), "PK 일치");
			assertEquals(filteredTechTube.getWriter().getId(), findTechTube.getWriter().getId(), "저자 일치");
			assertEquals(filteredTechTube.getEducationLevel().getId(), findTechTube.getEducationLevel().getId(),
				"학습 난이도 일치");
			assertEquals(filteredTechTube.getTitle(), findTechTube.getTitle(), "제목 일치");
			assertEquals(filteredTechTube.getDescription(), findTechTube.getDescription(), "설명 일치");
			assertEquals(filteredTechTube.getIntroduction(), findTechTube.getIntroduction(), "소개 일치");
			assertEquals(filteredTechTube.getTechTubeUrl(), findTechTube.getTechTubeUrl(), "영상 URL 일치");
			assertEquals(filteredTechTube.getTechTubeDuration(), findTechTube.getTechTubeDuration(), "영상 길이 일치");
			assertEquals(filteredTechTube.getTechTubeThumbnailUrl(), findTechTube.getTechTubeThumbnailUrl(),
				"썸네일 URL 일치");
			assertEquals(filteredTechTube.getPrice(), findTechTube.getPrice(), "가격 일치");
		}
	}
}
