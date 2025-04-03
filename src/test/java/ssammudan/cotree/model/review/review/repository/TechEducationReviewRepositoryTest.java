package ssammudan.cotree.model.review.review.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

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
			.set("email", Arbitraries.strings().alpha())
			.set("username", Arbitraries.strings().alpha())
			.set("nickname", Arbitraries.strings().alpha())
			.set("password", Arbitraries.strings().alpha())
			.set("phoneNumber", Arbitraries.strings().alpha())
			.set("role", MemberRole.USER)
			.set("memberStatus", MemberStatus.ACTIVE)
			.sample();
	}

	private TechEducationType createTechEducationType() {
		return fixtureMonkey.giveMeBuilder(TechEducationType.class)
			.instantiate(Instantiator.factoryMethod("create"))
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
	@DisplayName("[Success] findByReviewer_IdAndTechEducationType_IdAndItemId(): ")
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

}