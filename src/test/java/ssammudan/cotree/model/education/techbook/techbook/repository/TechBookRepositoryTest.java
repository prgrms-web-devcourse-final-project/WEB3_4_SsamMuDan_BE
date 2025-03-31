package ssammudan.cotree.model.education.techbook.techbook.repository;

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
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.supporter.DataJpaTestSupporter;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;
import ssammudan.cotree.model.member.member.entity.Member;

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
 */
class TechBookRepositoryTest extends DataJpaTestSupporter {

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

	@RepeatedTest(10)
	@DisplayName("[Success] save(): TechBook 엔티티 저장")
	void save() {
		//Given
		TechBook techBook = createTechBook(entityManager.persist(createMember()),
			entityManager.persist(createEducationLevel()));

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

	@RepeatedTest(10)
	@DisplayName("[Success] findById(): TechBook 엔티티 단 건 조회")
	void findById() {
		//Given
		TechBook techBook = createTechBook(entityManager.persist(createMember()),
			entityManager.persist(createEducationLevel()));
		Long id = entityManager.persist(techBook).getId();
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

}