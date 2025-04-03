package ssammudan.cotree.domain.review.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import net.jqwik.api.Arbitraries;

import ssammudan.cotree.domain.review.dto.TechEducationReviewRequest;
import ssammudan.cotree.domain.review.dto.TechEducationReviewResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;
import ssammudan.cotree.model.review.review.entity.TechEducationReview;
import ssammudan.cotree.model.review.reviewtype.type.TechEducationType;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.instantiator.Instantiator;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

/**
 * PackageName : ssammudan.cotree.domain.review.review.service
 * FileName    : TechEducationReviewServiceTest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : TechEducationReview 서비스 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 */
@Transactional
class TechEducationReviewServiceTest extends SpringBootTestSupporter {

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
		em.createNativeQuery("TRUNCATE TABLE techEducation_type RESTART IDENTITY").executeUpdate();
		em.createNativeQuery("TRUNCATE TABLE techEducation_review RESTART IDENTITY").executeUpdate();
		em.createNativeQuery("TRUNCATE TABLE tech_book RESTART IDENTITY").executeUpdate();
		em.createNativeQuery("TRUNCATE TABLE tech_tube RESTART IDENTITY").executeUpdate();

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
			.setNotNull("email")
			.setNotNull("username")
			.setNotNull("nickname")
			.setNotNull("password")
			.setNotNull("phoneNumber")
			.set("role", MemberRole.USER)
			.set("memberStatus", MemberStatus.ACTIVE)
			.sample();
	}

	private List<Member> createMembers(final int size) {
		return entityFixtureMonkey.giveMeBuilder(Member.class)
			.setNull("id")
			.sampleList(size);
	}

	private EducationLevel createEducationLevel() {
		return entityFixtureMonkey.giveMeBuilder(EducationLevel.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("name", Arbitraries.of("입문", "초급", "중급"))
			.sample();
	}

	private ssammudan.cotree.model.review.reviewtype.entity.TechEducationType createTechEducationType() {
		return entityFixtureMonkey.giveMeBuilder(
				ssammudan.cotree.model.review.reviewtype.entity.TechEducationType.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("name", "TechTube")
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

	private TechTube createTechTube(final Member member, final EducationLevel educationLevel) {
		return entityFixtureMonkey.giveMeBuilder(TechTube.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("writer", member)
			.set("educationLevel", educationLevel)
			.set("techTubeDuration", Arbitraries.longs().greaterOrEqual(0).map(Duration::ofSeconds))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.sample();
	}

	private TechEducationReview createTechEducationReview(
		final Member member, final ssammudan.cotree.model.review.reviewtype.entity.TechEducationType techEducationType,
		final Long itemId
	) {
		return entityFixtureMonkey.giveMeBuilder(TechEducationReview.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("reviewer", member)
			.set("techEducationType", techEducationType)
			.set("rating", Arbitraries.integers().between(0, 5))
			.set("itemId", itemId)
			.sample();
	}

	private List<TechEducationReview> createTechEducationReviews(
		final List<Member> members,
		final ssammudan.cotree.model.review.reviewtype.entity.TechEducationType techEducationType, final Long itemId
	) {
		List<TechEducationReview> reviews = new ArrayList<>();
		members.forEach(member -> reviews.add(
			entityFixtureMonkey.giveMeBuilder(TechEducationReview.class)
				.instantiate(Instantiator.factoryMethod("create"))
				.set("reviewer", member)
				.set("techEducationType", techEducationType)
				.set("rating", Arbitraries.integers().between(0, 5))
				.set("itemId", itemId)
				.sample()
		));
		return reviews;
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
	@DisplayName("[Success] createTechEducationReview(): 신규 TechEducationReview 생성")
	void createTechEducationReview() {
		//Given
		setup();

		Member creator = createMember();
		em.persist(creator);
		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);
		TechTube techTube = createTechTube(creator, educationLevel);
		em.persist(techTube);

		Member reviewer = createMember();
		em.persist(reviewer);
		ssammudan.cotree.model.review.reviewtype.entity.TechEducationType techEducationType = createTechEducationType();
		em.persist(techEducationType);
		clearEntityContext();

		TechEducationReviewRequest.Create requestDto = dtoFixtureMonkey.giveMeBuilder(
				TechEducationReviewRequest.Create.class
			).set("techEducationType", TechEducationType.TECH_TUBE)
			.set("rating", Arbitraries.integers().between(0, 5))
			.set("itemId", techTube.getId())
			.sample();

		//When
		Long id = techEducationReviewService.createTechEducationReview(reviewer.getId(), requestDto);
		clearEntityContext();

		//Then
		TechEducationReview savedTechEducationReview = techEducationReviewRepository.findById(id).get();
		TechTube reviewedTechTube = techTubeRepository.findById(techTube.getId()).get();

		assertNotNull(savedTechEducationReview, "TechEducationReview 엔티티 존재");
		assertEquals(requestDto.techEducationType().getId(), savedTechEducationReview.getTechEducationType().getId(),
			"리뷰 타입 일치");
		assertEquals(requestDto.rating(), savedTechEducationReview.getRating(), "평점 일치");
		assertEquals(requestDto.content(), savedTechEducationReview.getContent(), "리뷰 내용 일치");
		assertEquals(requestDto.itemId(), savedTechEducationReview.getItemId(), "리뷰 컨텐츠 일치");
		assertEquals(techTube.getTotalReviewCount() + 1, reviewedTechTube.getTotalReviewCount(), "컨텐츠 내 리뷰 수 일치");
		assertEquals(techTube.getTotalRating() + requestDto.rating(), reviewedTechTube.getTotalRating(),
			"컨텐츠 내 누적 평점 일치");
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Exception] createTechEducationReview_unknownReviewer(): 신규 TechEducationReview 생성, 존재하지 않는 회원")
	void createTechEducationReview_unknownReviewer() {
		//Given
		setup();

		String unknownMemberId = UUID.randomUUID().toString();
		ssammudan.cotree.model.review.reviewtype.entity.TechEducationType techEducationType = createTechEducationType();
		em.persist(techEducationType);

		TechEducationReviewRequest.Create requestDto = dtoFixtureMonkey.giveMeBuilder(
				TechEducationReviewRequest.Create.class
			).set("techEducationType", TechEducationType.TECH_TUBE)
			.set("rating", Arbitraries.integers().between(0, 5))
			.set("itemId", Arbitraries.longs().greaterOrEqual(1))
			.sample();

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techEducationReviewService.createTechEducationReview(unknownMemberId, requestDto));

		assertAll(
			() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND, "에러 코드 일치")
		);
	}

	//TODO: 리뷰 중복 생성 시도 처리에 따라 테스트 변경 가능성
	//@RepeatedTest(10)
	@Test
	@DisplayName("[Exception] createTechEducationReview_duplicatedReview(): 신규 TechEducationReview 생성, 이미 존재하는 리뷰")
	void createTechEducationReview_duplicatedReview() {
		//Given
		setup();

		Long itemId = 1L;

		Member member = createMember();
		em.persist(member);
		ssammudan.cotree.model.review.reviewtype.entity.TechEducationType techEducationType = createTechEducationType();
		em.persist(techEducationType);
		TechEducationReview techEducationReview = createTechEducationReview(member, techEducationType, itemId);
		em.persist(techEducationReview);

		TechEducationReviewRequest.Create requestDto = dtoFixtureMonkey.giveMeBuilder(
				TechEducationReviewRequest.Create.class
			).set("techEducationType", TechEducationType.TECH_TUBE)
			.set("rating", Arbitraries.integers().between(0, 5))
			.set("itemId", itemId)
			.sample();

		//When

		//Then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> techEducationReviewService.createTechEducationReview(member.getId(), requestDto));

		assertAll(
			() -> assertNotNull(globalException, "예외 존재"),
			() -> assertEquals(globalException.getErrorCode(), ErrorCode.TECH_EDUCATION_REVIEW_DUPLICATED, "에러 코드 일치")
		);
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findAllTechEducationReviews(): TechEducation 리뷰 다 건 조회, 페이징 적용")
	void findAllTechEducationReviews() {
		//Given
		setup();

		Member creator = createMember();
		em.persist(creator);
		List<Member> reviewers = createMembers(50);
		reviewers.forEach(reviewer -> {
			sleep(1);
			em.persist(reviewer);
		});
		EducationLevel educationLevel = createEducationLevel();
		em.persist(educationLevel);
		ssammudan.cotree.model.review.reviewtype.entity.TechEducationType techEducationType = createTechEducationType();
		em.persist(techEducationType);
		TechTube techTube = createTechTube(creator, educationLevel);
		em.persist(techTube);
		List<TechEducationReview> reviews = createTechEducationReviews(reviewers, techEducationType, techTube.getId());
		reviews.forEach(review -> {
			sleep(1);
			em.persist(review);
		});
		clearEntityContext();

		TechEducationReviewRequest.Read requestDto = dtoFixtureMonkey.giveMeBuilder(
				TechEducationReviewRequest.Read.class
			)
			.set("techEducationType", TechEducationType.TECH_TUBE)
			.set("itemId", techTube.getId())
			.sample();

		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

		//When
		List<TechEducationReviewResponse.Detail> findAllTechEducationReviewResopnseDto = techEducationReviewService.findAllTechEducationReviews(
			requestDto, pageable
		).getContent();

		//Then
		List<TechEducationReviewResponse.Detail> sortedTechEducationReviewResponseDto = reviews.stream()
			.sorted(Comparator.comparing(TechEducationReview::getCreatedAt).reversed())
			.limit(pageable.getPageSize())
			.map(TechEducationReviewResponse.Detail::from)
			.toList();

		assertEquals(sortedTechEducationReviewResponseDto.size(), findAllTechEducationReviewResopnseDto.size(),
			"조회 결과 갯수 일치");
		for (int i = 0; i < sortedTechEducationReviewResponseDto.size(); i++) {
			TechEducationReviewResponse.Detail sortedReviewDto = sortedTechEducationReviewResponseDto.get(i);
			TechEducationReviewResponse.Detail findReviewDto = findAllTechEducationReviewResopnseDto.get(i);

			assertEquals(sortedReviewDto.id(), findReviewDto.id(), "PK 일치");
			assertEquals(sortedReviewDto.techEducationType(), findReviewDto.techEducationType(), "교육 컨텐츠 타입 일치");
			assertEquals(sortedReviewDto.itemId(), findReviewDto.itemId(), "교육 컨텐츠 ID 일치");
			assertEquals(sortedReviewDto.reviewer(), findReviewDto.reviewer(), "리뷰 작성자 일치");
			assertEquals(sortedReviewDto.rating(), findReviewDto.rating(), "리뷰 점수 일치");
			assertEquals(sortedReviewDto.content(), findReviewDto.content(), "리뷰 내용 일치");
		}
	}

}
