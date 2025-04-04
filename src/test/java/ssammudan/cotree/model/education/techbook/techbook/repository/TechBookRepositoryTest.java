package ssammudan.cotree.model.education.techbook.techbook.repository;

import static org.junit.jupiter.api.Assertions.*;

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
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

import com.navercorp.fixturemonkey.api.instantiator.Instantiator;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.techbook.repository
 * FileName    : TechBookRepositoryTest
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBook 리포지토리 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 * 25. 4. 1.     loadingKKamo21       findAllTechBooksByKeyword() 테스트 추가
 */
class TechBookRepositoryTest extends DataJpaTestSupporter {

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

	private TechBook createTechBook(final Member member, final EducationLevel educationLevel) {
		return fixtureMonkey.giveMeBuilder(TechBook.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("writer", member)
			.set("educationLevel", educationLevel)
			.set("techBookPage", Arbitraries.integers().greaterOrEqual(0))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.sample();
	}

	private List<TechBook> createTechBooks(final Member member, final EducationLevel educationLevel, final int size) {
		return fixtureMonkey.giveMeBuilder(TechBook.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("writer", member)
			.set("educationLevel", educationLevel)
			.set("techBookPage", Arbitraries.integers().greaterOrEqual(0))
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
	@DisplayName("[Success] save(): TechBook 엔티티 저장")
	void save() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		TechBook techBook = createTechBook(member, educationLevel);

		//When
		Long id = techBookRepository.save(techBook).getId();
		clearEntityContext();

		//Then
		TechBook savedTechBook = entityManager.find(TechBook.class, id);

		assertNotNull(savedTechBook, "TechBook 엔티티 존재");
		assertEquals(techBook.getWriter().getId(), savedTechBook.getWriter().getId(), "저자 일치");
		assertEquals(techBook.getEducationLevel().getId(), savedTechBook.getEducationLevel().getId(), "학습 난이도 일치");
		assertEquals(techBook.getTitle(), savedTechBook.getTitle(), "제목 일치");
		assertEquals(techBook.getDescription(), savedTechBook.getDescription(), "설명 일치");
		assertEquals(techBook.getIntroduction(), savedTechBook.getIntroduction(), "소개 일치");
		assertEquals(techBook.getTechBookUrl(), savedTechBook.getTechBookUrl(), "PDF URL 일치");
		assertEquals(techBook.getTechBookPreviewUrl(), savedTechBook.getTechBookPreviewUrl(), "PDF 미리보기 URL 일치");
		assertEquals(techBook.getTechBookThumbnailUrl(), savedTechBook.getTechBookThumbnailUrl(), "썸네일 URL 일치");
		assertEquals(techBook.getTechBookPage(), savedTechBook.getTechBookPage(), "페이지 수 일치");
		assertEquals(techBook.getPrice(), savedTechBook.getPrice(), "가격 일치");
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findById(): TechBook 엔티티 단 건 조회")
	void findById() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		TechBook techBook = createTechBook(member, educationLevel);
		entityManager.persist(techBook);
		Long id = techBook.getId();
		clearEntityContext();

		//When
		TechBook savedTechBook = techBookRepository.findById(id).get();

		//Then
		assertNotNull(savedTechBook, "TechBook 엔티티 존재");
		assertEquals(techBook.getWriter().getId(), savedTechBook.getWriter().getId(), "저자 일치");
		assertEquals(techBook.getEducationLevel().getId(), savedTechBook.getEducationLevel().getId(), "학습 난이도 일치");
		assertEquals(techBook.getTitle(), savedTechBook.getTitle(), "제목 일치");
		assertEquals(techBook.getDescription(), savedTechBook.getDescription(), "설명 일치");
		assertEquals(techBook.getIntroduction(), savedTechBook.getIntroduction(), "소개 일치");
		assertEquals(techBook.getTechBookUrl(), savedTechBook.getTechBookUrl(), "PDF URL 일치");
		assertEquals(techBook.getTechBookPreviewUrl(), savedTechBook.getTechBookPreviewUrl(), "PDF 미리보기 URL 일치");
		assertEquals(techBook.getTechBookThumbnailUrl(), savedTechBook.getTechBookThumbnailUrl(), "썸네일 URL 일치");
		assertEquals(techBook.getTechBookPage(), savedTechBook.getTechBookPage(), "페이지 수 일치");
		assertEquals(techBook.getPrice(), savedTechBook.getPrice(), "가격 일치");
	}

	@ParameterizedTest
	@Repeat(10)
	@AutoSource
	@DisplayName("[Failure] findById_unknownId(): TechBook 단 건 조회, 존재하지 않는 ID")
	void findById_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given

		//When
		Optional<TechBook> opTechBook = techBookRepository.findById(unknownId);

		//Then
		assertFalse(opTechBook.isPresent(), "TechBook 엔티티 존재하지 않음");
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findAllTechBooksByKeyword(): TechBook 다 건 조회, 페이징 적용")
	void findAllTechBooksByKeyword() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		entityManager.persist(educationLevel);
		List<TechBook> techBooks = createTechBooks(member, educationLevel, 50);
		techBooks.forEach(techBook -> {
			sleep(1);
			entityManager.persist(techBook);
		});
		clearEntityContext();

		String keyword = fixtureMonkey.giveMeOne(String.class);
		Pageable pageable = PageRequest.of(0, 16, Sort.Direction.DESC, "createdAt");

		//When
		List<TechBook> findAllTechBooks = techBookRepository.findAllTechBooksByKeyword(keyword, pageable).getContent();

		//Then
		List<TechBook> filteredTechBooks = techBooks.stream()
			.filter(techBook ->
				!StringUtils.hasText(keyword) || (techBook.getTitle().contains(keyword)
					|| techBook.getDescription().contains(keyword)
					|| techBook.getIntroduction().contains(keyword))
			)
			.sorted(Comparator.comparing(TechBook::getCreatedAt).reversed()
				.thenComparing(book -> book.getCreatedAt().truncatedTo(ChronoUnit.MILLIS), Comparator.reverseOrder()))
			.limit(pageable.getPageSize())
			.toList();

		assertEquals(filteredTechBooks.size(), findAllTechBooks.size(), "검색 결과 갯수 일치");
		for (int i = 0; i < filteredTechBooks.size(); i++) {
			TechBook filteredTechBook = filteredTechBooks.get(i);
			TechBook findTechBook = findAllTechBooks.get(i);

			assertEquals(filteredTechBook.getId(), findTechBook.getId(), "PK 일치");
			assertEquals(filteredTechBook.getWriter().getId(), findTechBook.getWriter().getId(), "저자 일치");
			assertEquals(filteredTechBook.getEducationLevel().getId(), findTechBook.getEducationLevel().getId(),
				"학습 난이도 일치");
			assertEquals(filteredTechBook.getTitle(), findTechBook.getTitle(), "제목 일치");
			assertEquals(filteredTechBook.getDescription(), findTechBook.getDescription(), "설명 일치");
			assertEquals(filteredTechBook.getIntroduction(), findTechBook.getIntroduction(), "소개 일치");
			assertEquals(filteredTechBook.getTechBookUrl(), findTechBook.getTechBookUrl(), "PDF URL 일치");
			assertEquals(filteredTechBook.getTechBookPreviewUrl(), findTechBook.getTechBookPreviewUrl(),
				"PDF 미리보기 URL 일치");
			assertEquals(filteredTechBook.getTechBookThumbnailUrl(), findTechBook.getTechBookThumbnailUrl(),
				"썸네일 URL 일치");
			assertEquals(filteredTechBook.getTechBookPage(), findTechBook.getTechBookPage(), "페이지 수 일치");
			assertEquals(filteredTechBook.getPrice(), findTechBook.getPrice(), "가격 일치");
		}
	}

}