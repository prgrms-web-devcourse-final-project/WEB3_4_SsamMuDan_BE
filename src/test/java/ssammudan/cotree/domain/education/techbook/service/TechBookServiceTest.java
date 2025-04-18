package ssammudan.cotree.domain.education.techbook.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.transaction.annotation.Transactional;

import net.jqwik.api.Arbitraries;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.domain.education.techbook.dto.TechBookRequest;
import ssammudan.cotree.domain.education.techbook.dto.TechBookResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.common.like.entity.Like;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.level.type.EducationLevelType;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

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
			.set("phoneNumber", Arbitraries.strings()
				.withCharRange('a', 'z')
				.ofMinLength(1)
				.ofMaxLength(219)
				.map(s -> s + UUID.randomUUID()))
			.set("role", MemberRole.USER)
			.set("memberStatus", MemberStatus.ACTIVE)
			.sample();
	}

	private EducationLevel createEducationLevel() {
		return entityFixtureMonkey.giveMeBuilder(EducationLevel.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("name", "입문")
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

	private Like createLike(final Member member, final TechBook techBook) {
		return Like.create(member, techBook);
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

		TechBookRequest.TechBookCreate requestDto = dtoFixtureMonkey.giveMeBuilder(TechBookRequest.TechBookCreate.class)
			.set("educationLevel", EducationLevelType.BEGINNER)
			.sample();

		//When
		Long id = techBookService.createTechBook(writer.getId(), requestDto);
		clearEntityContext();

		//Then
		TechBook savedTechBook = techBookRepository.findById(id).get();

		assertNotNull(savedTechBook, "TechBook 엔티티 존재");
		assertEquals(requestDto.educationLevel().getId(), savedTechBook.getEducationLevel().getId(), "학습 난이도 일치");
		assertEquals(requestDto.title(), savedTechBook.getTitle(), "제목 일치");
		assertEquals(requestDto.description(), savedTechBook.getDescription(), "설명 일치");
		assertEquals(requestDto.introduction(), savedTechBook.getIntroduction(), "소개 일치");
		assertEquals(requestDto.techBookUrl(), savedTechBook.getTechBookUrl(), "PDF URL 일치");
		assertEquals(requestDto.techBookPreviewUrl(), savedTechBook.getTechBookPreviewUrl(), "PDF 미리보기 URL 일치");
		assertEquals(requestDto.techBookThumbnailUrl(), savedTechBook.getTechBookThumbnailUrl(), "썸네일 URL 일치");
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

		TechBookRequest.TechBookCreate requestDto = dtoFixtureMonkey.giveMeOne(TechBookRequest.TechBookCreate.class);

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techBookService.createTechBook(writer.getId(), requestDto), "GlobalException 발생");

		assertAll(() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.EDUCATION_LEVEL_NOT_FOUND, "에러 코드 일치"));
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Exception] createTechBook_unknownWriter(): 신규 TechBook 생성, 존재하지 않는 회원")
	void createTechBook_unknownWriter() {
		//Given
		setup();

		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);

		TechBookRequest.TechBookCreate requestDto = dtoFixtureMonkey.giveMeOne(TechBookRequest.TechBookCreate.class);

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techBookService.createTechBook(UUID.randomUUID().toString(), requestDto), "GlobalException 발생");

		assertAll(() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND, "에러 코드 일치"));
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findTechBookById(): 좋아요한 회원 ID와 함께 TechBook 단 건 조회")
	void findTechBookByIdAndMemberId() {
		//Given
		setup();

		Member member = createMember();
		em.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);
		TechBook techBook = createTechBook(member, educationLevel);
		em.persist(techBook);
		Like like = createLike(member, techBook);
		em.persist(like);

		Long id = techBook.getId();
		clearEntityContext();

		//When
		TechBookResponse.TechBookDetail responseDto = techBookService.findTechBookById(id, member.getId());

		//Then
		assertNotNull(responseDto, "TechBook 응답 DTO 존재");
		assertEquals(responseDto.id(), techBook.getId(), "PK 일치");
		assertEquals(responseDto.educationLevel(), techBook.getEducationLevel().getName(), "학습 난이도 일치");
		assertEquals(responseDto.title(), techBook.getTitle(), "제목 일치");
		assertEquals(responseDto.description(), techBook.getDescription(), "설명 일치");
		assertEquals(responseDto.introduction(), techBook.getIntroduction(), "소개 일치");
		assertEquals(responseDto.totalReviewCount(), techBook.getTotalReviewCount(), "전체 리뷰 수 일치");
		assertEquals(responseDto.techBookPreviewUrl(), techBook.getTechBookPreviewUrl(), "PDF 미리보기 URL 일치");
		assertEquals(responseDto.techBookThumbnailUrl(), techBook.getTechBookThumbnailUrl(), "썸네일 URL 일치");
		assertEquals(responseDto.techBookPage(), techBook.getTechBookPage(), "페이지 수 일치");
		assertEquals(responseDto.price(), techBook.getPrice(), "가격 일치");
		assertEquals(responseDto.likeCount(), techBook.getLikes().size(), "좋아요 수 일치");
		assertTrue(responseDto.isLike(), "좋아요 여부");
		assertEquals(responseDto.createdAt().toLocalDate(), techBook.getCreatedAt().toLocalDate(), "등록 일자 일치");
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Exception] findTechBookById_unknownId(): 좋아요한 회원 ID와 함께 TechBook 단 건 조회, 존재하지 않는 ID")
	void findTechBookByIdAndMemberId_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given
		setup();

		Member member = createMember();
		em.persist(member);
		clearEntityContext();

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techBookService.findTechBookById(unknownId, member.getId()), "GlobalException 발생");

		assertAll(() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.TECH_BOOK_NOT_FOUND, "에러 코드 일치"));
	}

}