package ssammudan.cotree.model.review.review.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

import net.jqwik.api.Arbitraries;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.integration.DataJpaTestSupporter;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;
import ssammudan.cotree.model.review.review.entity.TechEducationReview;
import ssammudan.cotree.model.review.reviewtype.entity.TechEducationType;

import com.navercorp.fixturemonkey.api.instantiator.Instantiator;

/**
 * PackageName : ssammudan.cotree.model.review.review.repository
 * FileName    : TechEducationReviewRepositoryTest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : TechEducationReview 리포지토리 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 */
class TechEducationReviewRepositoryTest extends DataJpaTestSupporter {

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

	private List<Member> createMembers(final int size) {
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
			.set("phoneNumber", Arbitraries.strings()
				.withCharRange('a', 'z')
				.ofMinLength(1)
				.ofMaxLength(219)
				.map(s -> s + UUID.randomUUID()))
			.set("role", MemberRole.USER)
			.set("memberStatus", MemberStatus.ACTIVE)
			.sampleList(size);
	}

	private TechEducationType createTechEducationType() {
		return fixtureMonkey.giveMeBuilder(TechEducationType.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("name", Arbitraries.strings()
				.withCharRange('a', 'z')
				.ofMinLength(1)
				.ofMaxLength(219)
				.map(s -> s + UUID.randomUUID()))
			.sample();
	}

	private TechEducationReview createTechEducationReview(
		final Member member, final TechEducationType techEducationType
	) {
		return fixtureMonkey.giveMeBuilder(TechEducationReview.class)
			.instantiate(Instantiator.factoryMethod("create"))
			.set("reviewer", member)
			.set("techEducationType", techEducationType)
			.set("rating", Arbitraries.integers().between(0, 5))
			.set("itemId", Arbitraries.longs().greaterOrEqual(1))
			.sample();
	}

	private List<TechEducationReview> createTechEducationReviews(
		final List<Member> members, final TechEducationType techEducationType, final Long itemId
	) {
		List<TechEducationReview> reviews = new ArrayList<>();
		members.forEach(member -> reviews.add(
			fixtureMonkey.giveMeBuilder(TechEducationReview.class)
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
	@DisplayName("[Success] save(): TechEducationReview 엔티티 저장")
	void save() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		TechEducationType techEducationType = createTechEducationType();
		entityManager.persist(techEducationType);
		TechEducationReview techEducationReview = createTechEducationReview(member, techEducationType);

		//When
		Long id = techEducationReviewRepository.save(techEducationReview).getId();
		clearEntityContext();

		//Then
		TechEducationReview savedTechEducationReview = entityManager.find(TechEducationReview.class, id);

		assertNotNull(savedTechEducationReview, "TechEducationReview 엔티티 존재");
		assertEquals(techEducationReview.getReviewer().getId(), savedTechEducationReview.getReviewer().getId(),
			"리뷰 작성자 일치");
		assertEquals(techEducationReview.getTechEducationType().getId(),
			savedTechEducationReview.getTechEducationType().getId(), "교육 컨텐츠 타입 일치");
		assertEquals(techEducationReview.getItemId(), savedTechEducationReview.getItemId(), "리뷰 컨텐츠 일치");
		assertEquals(techEducationReview.getRating(), savedTechEducationReview.getRating(), "리뷰 평점 일치");
		assertEquals(techEducationReview.getContent(), savedTechEducationReview.getContent(), "리뷰 내용 일치");
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findById(): TechEducationReview 엔티티 단 건 조회")
	void findById() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		TechEducationType techEducationType = createTechEducationType();
		entityManager.persist(techEducationType);
		TechEducationReview techEducationReview = createTechEducationReview(member, techEducationType);
		entityManager.persist(techEducationReview);
		Long id = techEducationReview.getId();
		clearEntityContext();

		//When
		TechEducationReview savedTechEducationReview = techEducationReviewRepository.findById(id).get();

		//Then
		assertNotNull(savedTechEducationReview, "TechEducationReview 엔티티 존재");
		assertEquals(techEducationReview.getReviewer().getId(), savedTechEducationReview.getReviewer().getId(),
			"리뷰 작성자 일치");
		assertEquals(techEducationReview.getTechEducationType().getId(),
			savedTechEducationReview.getTechEducationType().getId(), "교육 컨텐츠 타입 일치");
		assertEquals(techEducationReview.getItemId(), savedTechEducationReview.getItemId(), "리뷰 컨텐츠 일치");
		assertEquals(techEducationReview.getRating(), savedTechEducationReview.getRating(), "리뷰 평점 일치");
		assertEquals(techEducationReview.getContent(), savedTechEducationReview.getContent(), "리뷰 내용 일치");
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Failure] findById_unknownId(): TechEducationReview 엔티티 단 건 조회, 존재하지 않는 ID")
	void findById_unknownId(@Min(1) @Max(Long.MAX_VALUE) final Long unknownId) {
		//Given

		//When
		Optional<TechEducationReview> opTechEducationReview = techEducationReviewRepository.findById(unknownId);

		//Then
		assertFalse(opTechEducationReview.isPresent(), "TechEducationReview 엔티티 존재하지 않음");
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findByReviewer_IdAndTechEducationType_IdAndItemId(): TechEducationReview 엔티티, 리뷰 작성자 ID, 교육 컨텐츠 타입 ID, 리뷰 컨텐츠 ID 기반 단 건 조회")
	void findByReviewer_IdAndTechEducationType_IdAndItemId() {
		//Given
		setup();

		Member member = createMember();
		entityManager.persist(member);
		TechEducationType techEducationType = createTechEducationType();
		entityManager.persist(techEducationType);
		TechEducationReview techEducationReview = createTechEducationReview(member, techEducationType);
		entityManager.persist(techEducationReview);
		clearEntityContext();

		//When
		TechEducationReview savedTechEducationReview = techEducationReviewRepository.findByReviewer_IdAndTechEducationType_IdAndItemId(
			member.getId(), techEducationType.getId(), techEducationReview.getItemId()
		).get();

		//Then
		assertNotNull(savedTechEducationReview, "TechEducationReview 엔티티 존재");
		assertEquals(techEducationReview.getReviewer().getId(), savedTechEducationReview.getReviewer().getId(),
			"리뷰 작성자 일치");
		assertEquals(techEducationReview.getTechEducationType().getId(),
			savedTechEducationReview.getTechEducationType().getId(), "교육 컨텐츠 타입 일치");
		assertEquals(techEducationReview.getItemId(), savedTechEducationReview.getItemId(), "리뷰 컨텐츠 일치");
		assertEquals(techEducationReview.getRating(), savedTechEducationReview.getRating(), "리뷰 평점 일치");
		assertEquals(techEducationReview.getContent(), savedTechEducationReview.getContent(), "리뷰 내용 일치");
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] findAllTechEducationReviews(): TechEducationReview 엔티티 다 건 조회, 페이징 적용")
	void findAllTechEducationReviews() {
		//Given
		setup();

		Long itemId = 1234567890L;
		List<Member> members = createMembers(50);
		members.forEach(member -> entityManager.persist(member));
		TechEducationType techEducationType = createTechEducationType();
		entityManager.persist(techEducationType);
		List<TechEducationReview> reviews = createTechEducationReviews(members, techEducationType, itemId);
		reviews.forEach(review -> {
			sleep(1);
			entityManager.persist(review);
		});
		clearEntityContext();

		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "rating");

		//When
		List<TechEducationReview> findAllTechEducationReviews = techEducationReviewRepository.findAllTechEducationReviews(
			techEducationType.getId(), itemId, pageable
		).getContent();

		//Then
		List<TechEducationReview> sortedTechEducationReviews = reviews.stream()
			.sorted(Comparator.comparing(TechEducationReview::getRating).reversed()
				.thenComparing(type -> type.getCreatedAt().truncatedTo(ChronoUnit.MILLIS), Comparator.reverseOrder()))
			.limit(pageable.getPageSize())
			.toList();

		assertEquals(sortedTechEducationReviews.size(), findAllTechEducationReviews.size(), "조회 결과 갯수 일치");
		for (int i = 0; i < sortedTechEducationReviews.size(); i++) {
			TechEducationReview sortedReview = sortedTechEducationReviews.get(i);
			TechEducationReview findReview = findAllTechEducationReviews.get(i);

			assertEquals(sortedReview.getId(), findReview.getId(), "PK 일치");
			assertEquals(sortedReview.getReviewer().getId(), findReview.getReviewer().getId(), "리뷰 작성자 일치");
			assertEquals(sortedReview.getTechEducationType().getId(), findReview.getTechEducationType().getId(),
				"교육 컨텐츠 타입 일치");
			assertEquals(sortedReview.getItemId(), findReview.getItemId(), "리뷰 컨텐츠 일치");
			assertEquals(sortedReview.getRating(), findReview.getRating(), "리뷰 평점 일치");
			assertEquals(sortedReview.getContent(), findReview.getContent(), "리뷰 내용 일치");
		}
	}

}