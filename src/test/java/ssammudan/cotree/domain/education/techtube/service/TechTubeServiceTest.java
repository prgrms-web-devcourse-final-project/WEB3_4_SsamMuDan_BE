package ssammudan.cotree.domain.education.techtube.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
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
import ssammudan.cotree.domain.education.techtube.dto.TechTubeRequest;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.level.type.EducationLevelType;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.instantiator.Instantiator;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

/**
 * PackageName : ssammudan.cotree.domain.education.techtube.service
 * FileName    : TechTubeServiceTest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : TechTube 서비스 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 */
@Transactional
class TechTubeServiceTest extends SpringBootTestSupporter {

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
		return entityFixtureMonkey.giveMeBuilder(EducationLevel.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("name", "입문")
			.sample();
	}

	private TechTube createTechTube(final Member member, final EducationLevel educationLevel) {
		return entityFixtureMonkey.giveMeBuilder(TechTube.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("writer", member)
			.set("educationLevel", educationLevel)
			.set("techTubeDuration", Arbitraries.longs().greaterOrEqual(0).map(Duration::ofSeconds))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.sample();
	}

	private List<TechTube> createTechTubes(final Member member, final EducationLevel educationLevel, final int size) {
		return entityFixtureMonkey.giveMeBuilder(TechTube.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("writer", member)
			.set("educationLevel", educationLevel)
			.set("techTubeDuration", Arbitraries.longs().greaterOrEqual(0).map(Duration::ofSeconds))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.sampleList(size);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] createTechTube(): 신규 TechTube 생성")
	void createTechTube() {
		//Given
		setup();

		Member writer = createMember();
		em.persist(writer);

		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);

		TechTubeRequest.Create requestDto = dtoFixtureMonkey.giveMeBuilder(TechTubeRequest.Create.class)
			.set("educationLevel", EducationLevelType.BEGINNER)
			.set("techTubeDurationSeconds", Arbitraries.longs().greaterOrEqual(0))
			.sample();

		//When
		Long id = techTubeService.createTechTube(writer.getId(), requestDto);
		clearEntityContext();

		//Then
		TechTube savedTechTube = techTubeRepository.findById(id).get();

		assertNotNull(savedTechTube, "TechTube 엔티티 존재");
		assertEquals(requestDto.educationLevel().getId(), savedTechTube.getEducationLevel().getId(), "학습 난이도 일치");
		assertEquals(requestDto.title(), savedTechTube.getTitle(), "제목 일치");
		assertEquals(requestDto.description(), savedTechTube.getDescription(), "설명 일치");
		assertEquals(requestDto.introduction(), savedTechTube.getIntroduction(), "소개 일치");
		assertEquals(requestDto.techTubeUrl(), savedTechTube.getTechTubeUrl(), "영상 URL 일치");
		assertEquals(requestDto.techTubeDurationSeconds(), savedTechTube.getTechTubeDuration().getSeconds(),
			"영상 길이 일치");
		assertEquals(requestDto.techTubeThumbnailUrl(), savedTechTube.getTechTubeThumbnailUrl(), "썸네일 URL 일치");
		assertEquals(requestDto.price(), savedTechTube.getPrice(), "가격 일치");
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Exception] createTechTube_unknownEducationLevel(): 신규 TechTube 생성, 존재하지 않는 학습 난이도")
	void createTechTube_unknownEducationLevel() {
		//Given
		setup();

		Member writer = createMember();
		em.persist(writer);

		TechTubeRequest.Create requestDto = dtoFixtureMonkey.giveMeOne(TechTubeRequest.Create.class);

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techTubeService.createTechTube(writer.getId(), requestDto), "GlobalException 발생");

		assertAll(
			() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.EDUCATION_LEVEL_NOT_FOUND, "에러 코드 일치")
		);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Exception] createTechTube_unknownWriter(): 신규 TechTube 생성, 존재하지 않는 회원")
	void createTechTube_unknownWriter() {
		//Given
		setup();

		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);

		TechTubeRequest.Create requestDto = dtoFixtureMonkey.giveMeOne(TechTubeRequest.Create.class);

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techTubeService.createTechTube(UUID.randomUUID().toString(), requestDto), "GlobalException 발생");

		assertAll(
			() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND, "에러 코드 일치")
		);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findTechTubeById(): TechTube 단 건 조회")
	void findTechTubeById() {
		//Given
		setup();

		Member member = createMember();
		em.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);
		TechTube techTube = createTechTube(member, educationLevel);
		em.persist(techTube);

		Long id = techTube.getId();
		clearEntityContext();

		//When
		TechTubeResponse.Detail responseDto = techTubeService.findTechTubeById(id);

		//Then
		assertNotNull(responseDto, "TechTube 응답 DTO 존재");
		assertEquals(responseDto.id(), techTube.getId(), "PK 일치");
		assertEquals(responseDto.educationLevel(), techTube.getEducationLevel().getName(), "학습 난이도 일치");
		assertEquals(responseDto.title(), techTube.getTitle(), "제목 일치");
		assertEquals(responseDto.description(), techTube.getDescription(), "설명 일치");
		assertEquals(responseDto.introduction(), techTube.getIntroduction(), "소개 일치");
		assertEquals(responseDto.totalReviewCount(), techTube.getTotalReviewCount(), "전체 리뷰 수 일치");
		assertEquals(responseDto.techTubeUrl(), techTube.getTechTubeUrl(), "영상 URL 일치");
		assertEquals(responseDto.techTubeDurationSeconds(), techTube.getTechTubeDuration().getSeconds(), "영상 길이 일치");
		assertEquals(responseDto.techTubeThumbnailUrl(), techTube.getTechTubeThumbnailUrl(), "썸네일 URL 일치");
		assertEquals(responseDto.price(), techTube.getPrice(), "가격 일치");
		assertEquals(responseDto.viewCount(), techTube.getViewCount() + 1, "조회 수 일치");
		assertEquals(responseDto.likeCount(), techTube.getLikes().size(), "좋아요 수 일치");
		assertEquals(responseDto.createdAt(), techTube.getCreatedAt().toLocalDate(), "등록 일자 일치");
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Exception] findTechTubeById_unknownId(): TechTube 단 건 조회, 존재하지 않는 ID")
	void findTechTubeById_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techTubeService.findTechTubeById(unknownId), "GlobalException 발생");

		assertAll(
			() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.TECH_TUBE_NOT_FOUND, "에러 코드 일치")
		);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findAllTechTubes(): TechTube 다 건 조회, 페이징 적용")
	void findAllTechTubes() {
		//Given
		setup();

		Member member = createMember();
		em.persist(member);
		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);
		List<TechTube> techTubes = createTechTubes(member, educationLevel, 50);
		techTubes.forEach(techTube -> em.persist(techTube));
		clearEntityContext();

		String keyword = dtoFixtureMonkey.giveMeOne(String.class);
		PageRequest pageable = PageRequest.of(0, 16, Sort.Direction.DESC, "createdAt");

		//When
		List<TechTubeResponse.ListInfo> findAllTechTubeResponseDto = techTubeService.findAllTechTubes(
			keyword, pageable
		).getContent();

		//Then
		List<TechTubeResponse.ListInfo> filteredTechTubeResponseDto = techTubes.stream()
			.filter(techBook ->
				!StringUtils.hasText(keyword) || (techBook.getTitle().contains(keyword)
					|| techBook.getDescription().contains(keyword)
					|| techBook.getIntroduction().contains(keyword))
			)
			.sorted(Comparator.comparing(TechTube::getCreatedAt).reversed()
				.thenComparing(book -> book.getCreatedAt().truncatedTo(ChronoUnit.MILLIS), Comparator.reverseOrder()))
			.limit(pageable.getPageSize())
			.map(TechTubeResponse.ListInfo::from)
			.toList();

		assertEquals(filteredTechTubeResponseDto.size(), findAllTechTubeResponseDto.size(), "검색 결과 갯수 일치");
		for (int i = 0; i < filteredTechTubeResponseDto.size(); i++) {
			TechTubeResponse.ListInfo filteredTechTubeDto = filteredTechTubeResponseDto.get(i);
			TechTubeResponse.ListInfo findTechTubeDto = findAllTechTubeResponseDto.get(i);

			assertEquals(filteredTechTubeDto.id(), findTechTubeDto.id(), "PK 일치");
			assertEquals(filteredTechTubeDto.writer(), findTechTubeDto.writer(), "저자 일치");
			assertEquals(filteredTechTubeDto.title(), findTechTubeDto.title(), "제목 일치");
			assertEquals(filteredTechTubeDto.price(), findTechTubeDto.price(), "가격 일치");
			assertEquals(filteredTechTubeDto.techTubeThumbnailUrl(), findTechTubeDto.techTubeThumbnailUrl(),
				"썸네일 URL 일치");
			assertEquals(filteredTechTubeDto.likeCount(), findTechTubeDto.likeCount(), "좋아요 수 일치");
			assertEquals(filteredTechTubeDto.createdAt(), findTechTubeDto.createdAt(), "등록 일자 일치");
		}
	}

}
