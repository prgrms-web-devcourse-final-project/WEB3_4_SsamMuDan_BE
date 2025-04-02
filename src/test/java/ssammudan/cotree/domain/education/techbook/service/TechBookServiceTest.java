package ssammudan.cotree.domain.education.techbook.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import net.jqwik.api.Arbitraries;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.domain.education.techbook.dto.TechBookRequest;
import ssammudan.cotree.domain.education.techbook.dto.TechBookResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;
import ssammudan.cotree.model.member.member.entity.Member;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.instantiator.Instantiator;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

/**
 * PackageName : ssammudan.cotree.domain.education.techbook.service
 * FileName    : TechBookServiceTest
 * Author      : loadingKKamo21
 * Date        : 25. 3. 31.
 * Description : TechBook 서비스 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.    loadingKKamo21       Initial creation
 * 25. 4. 1.     loadingKKamo21       findAllTechBooks() 테스트 추가
 */
@Transactional
class TechBookServiceTest extends SpringBootTestSupporter {

	private final FixtureMonkey entityFixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.defaultNotNull(true)
		.build();

	private final FixtureMonkey dtoFixtureMonkey = FixtureMonkey.builder()
		.plugin(new JakartaValidationPlugin())
		.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
		.build();

	@BeforeEach
	void setup() {
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();    //H2 DB 외래키 제약 해제

		em.createNativeQuery("TRUNCATE TABLE member RESTART IDENTITY").executeUpdate();
		em.createNativeQuery("TRUNCATE TABLE education_level RESTART IDENTITY").executeUpdate();
		em.createNativeQuery("TRUNCATE TABLE tech_book RESTART IDENTITY").executeUpdate();

		em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();    //H2 DB 외래키 제약 설정
	}

	@AfterEach
	void clearEntityContext() {
		em.flush();
		em.clear();
	}

	private Member createMember() {
		return entityFixtureMonkey.giveMeBuilder(Member.class)
			.setNull("id")
			.sample();
	}

	private EducationLevel createEducationLevel() {
		return entityFixtureMonkey.giveMeBuilder(EducationLevel.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("name", Arbitraries.of("입문", "초급", "중급"))
			.sample();
	}

	private TechBook createTechBook(final Member member, final EducationLevel educationLevel) {
		return entityFixtureMonkey.giveMeBuilder(TechBook.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("writer", member)
			.set("educationLevel", educationLevel)
			.set("techBookPage", Arbitraries.integers().greaterOrEqual(0))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.sample();
	}

	private List<TechBook> createTechBooks(final Member member, final EducationLevel educationLevel, final int size) {
		return entityFixtureMonkey.giveMeBuilder(TechBook.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("writer", member)
			.set("educationLevel", educationLevel)
			.set("techBookPage", Arbitraries.integers().greaterOrEqual(0))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.sampleList(size);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] createTechBook(): 신규 TechBook 생성")
	void createTechBook() {
		//Given
		setup();

		Member writer = createMember();
		em.persist(writer);

		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);

		TechBookRequest.Create requestDto = dtoFixtureMonkey.giveMeBuilder(TechBookRequest.Create.class)
			.set("educationLevel", educationLevel.getName())
			.sample();

		//When
		Long id = techBookService.createTechBook(writer.getId(), requestDto);
		clearEntityContext();

		//Then
		TechBook savedTechBook = techBookRepository.findById(id).get();

		assertNotNull(savedTechBook, "TechBook 엔티티 존재");
		assertEquals(requestDto.educationLevel(), savedTechBook.getEducationLevel().getName(), "학습 난이도 일치");
		assertEquals(requestDto.title(), savedTechBook.getTitle(), "제목 일치");
		assertEquals(requestDto.description(), savedTechBook.getDescription(), "설명 일치");
		assertEquals(requestDto.introduction(), savedTechBook.getIntroduction(), "소개 일치");
		assertEquals(requestDto.techBookUrl(), savedTechBook.getTechBookUrl(), "PDF URL 일치");
		assertEquals(requestDto.techBookPreviewUrl(), savedTechBook.getTechBookPreviewUrl(), "PDF 미리보기 URL 일치");
		assertEquals(requestDto.techBookThumbnailUrl(), savedTechBook.getTechBookThumbnailUrl(),
			"썸네일 URL 일치");
		assertEquals(requestDto.techBookPage(), savedTechBook.getTechBookPage(), "페이지 수 일치");
		assertEquals(requestDto.price(), savedTechBook.getPrice(), "가격 일치");
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Exception] createTechBook_unknownEducationLevel(): 신규 TechBook 생성, 존재하지 않는 학습 난이도")
	void createTechBook_unknownEducationLevel() {
		//Given
		setup();

		Member writer = createMember();
		em.persist(writer);

		TechBookRequest.Create requestDto = dtoFixtureMonkey.giveMeOne(TechBookRequest.Create.class);

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techBookService.createTechBook(writer.getId(), requestDto), "GlobalException 발생");

		assertAll(
			() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.EDUCATION_LEVEL_NOT_FOUND, "에러 코드 일치")
		);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Exception] createTechBook_unknownWriter(): 신규 TechBook 생성, 존재하지 않는 회원")
	void createTechBook_unknownWriter() {
		//Given
		setup();

		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);

		TechBookRequest.Create requestDto = dtoFixtureMonkey.giveMeOne(TechBookRequest.Create.class);

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techBookService.createTechBook(UUID.randomUUID().toString(), requestDto), "GlobalException 발생");

		assertAll(
			() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.NOT_FOUND_MEMBER, "에러 코드 일치")
		);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findTechBookById(): TechBook 단 건 조회")
	void findTechBookById() {
		//Given
		setup();

		Member member = createMember();
		em.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);
		TechBook techBook = createTechBook(member, educationLevel);
		em.persist(techBook);

		Long id = techBook.getId();
		clearEntityContext();

		//When
		TechBookResponse.Detail responseDto = techBookService.findTechBookById(id);

		//Then
		assertNotNull(responseDto, "TechBook 응답 DTO 존재");
		assertEquals(responseDto.id(), techBook.getId(), "PK 일치");
		assertEquals(responseDto.educationLevel(), techBook.getEducationLevel().getName(), "학습 난이도 일치");
		assertEquals(responseDto.title(), techBook.getTitle(), "제목 일치");
		assertEquals(responseDto.description(), techBook.getDescription(), "설명 일치");
		assertEquals(responseDto.introduction(), techBook.getIntroduction(), "소개 일치");
		assertEquals(responseDto.totalReviewCount(), techBook.getTotalReviewCount(), "전체 리뷰 수 일치");
		assertEquals(responseDto.techBookUrl(), techBook.getTechBookUrl(), "PDF URL 일치");
		assertEquals(responseDto.techBookPreviewUrl(), techBook.getTechBookPreviewUrl(), "PDF 미리보기 URL 일치");
		assertEquals(responseDto.techBookThumbnailUrl(), techBook.getTechBookThumbnailUrl(), "썸네일 URL 일치");
		assertEquals(responseDto.techBookPage(), techBook.getTechBookPage(), "페이지 수 일치");
		assertEquals(responseDto.price(), techBook.getPrice(), "가격 일치");
		assertEquals(responseDto.viewCount(), techBook.getViewCount(), "조회 수 일치");
		assertEquals(responseDto.createdAt(), techBook.getCreatedAt().toLocalDate(), "등록 일자 일치");
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Exception] findTechBookById_unknownId(): TechBook 단 건 조회, 존재하지 않는 ID")
	void findTechBookById_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techBookService.findTechBookById(unknownId), "GlobalException 발생");

		assertAll(
			() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.TECH_BOOK_NOT_FOUND, "에러 코드 일치")
		);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findAllTechBooks(): TechBook 다 건 조회, 페이징 적용")
	void findAllTechBooks() {
		//Given
		setup();

		Member member = createMember();
		em.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);
		List<TechBook> techBooks = createTechBooks(member, educationLevel, 50);
		techBooks.forEach(techBook -> em.persist(techBook));
		clearEntityContext();

		String keyword = dtoFixtureMonkey.giveMeOne(String.class);
		PageRequest pageable = PageRequest.of(0, 16, Sort.Direction.DESC, "createdAt");

		//When
		List<TechBookResponse.ListInfo> findAllTechBookResponseDto = techBookService.findAllTechBooks(
			keyword, pageable
		).getContent();

		//Then
		List<TechBookResponse.ListInfo> filteredTechBookResponseDto = techBooks.stream()
			.filter(techBook ->
				!StringUtils.hasText(keyword) || (techBook.getTitle().contains(keyword)
					|| techBook.getDescription().contains(keyword)
					|| techBook.getIntroduction().contains(keyword))
			)
			.sorted(Comparator.comparing(TechBook::getCreatedAt).reversed())
			.limit(pageable.getPageSize())
			.map(TechBookResponse.ListInfo::from)
			.toList();

		assertEquals(filteredTechBookResponseDto.size(), findAllTechBookResponseDto.size(), "검색 결과 갯수 일치");
		for (int i = 0; i < filteredTechBookResponseDto.size(); i++) {
			TechBookResponse.ListInfo filteredTechBookDto = filteredTechBookResponseDto.get(i);
			TechBookResponse.ListInfo findTechBookDto = findAllTechBookResponseDto.get(i);

			assertEquals(filteredTechBookDto.id(), findTechBookDto.id(), "PK 일치");
			assertEquals(filteredTechBookDto.writer(), findTechBookDto.writer(), "저자 일치");
			assertEquals(filteredTechBookDto.price(), findTechBookDto.price(), "가격 일치");
			assertEquals(filteredTechBookDto.techBookThumbnailUrl(), findTechBookDto.techBookThumbnailUrl(),
				"썸네일 URL 일치");
			assertEquals(filteredTechBookDto.createdAt(), findTechBookDto.createdAt(), "등록 일자 일치");
		}
	}

}