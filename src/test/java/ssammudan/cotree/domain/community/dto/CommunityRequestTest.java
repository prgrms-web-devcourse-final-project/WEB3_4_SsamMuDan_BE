package ssammudan.cotree.domain.community.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * PackageName : ssammudan.cotree.domain.community.dto
 * FileName    : CommunityRequestTest
 * Author      : Baekgwa
 * Date        : 2025-03-31
 * Description : CommunityRequest dto 유효성 검증 Test
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-31     Baekgwa               Initial creation
 * 2025-04-08     Baekgwa               커뮤니티 글 작성 시, community category 입력 형식 변경. 기존 : String / 변경 : Long id
 */
class CommunityRequestTest {

	private static Validator validator;
	private static ValidatorFactory factory;

	@BeforeAll
	static void setupValidator() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@AfterAll
	static void tearDownValidator() {
		if (factory != null) {
			factory.close();
		}
	}

	@DisplayName("커뮤니티 새글 작성 dto 유효성 검증")
	@Test
	void createBoard1() {
		// given
		String longTitle = "A".repeat(49);
		String longContent = "B".repeat(9999);
		CommunityRequest.CreateBoard createBoard =
			new CommunityRequest.CreateBoard(2L, longTitle, longContent);

		// when
		Set<ConstraintViolation<CommunityRequest.CreateBoard>> validate = validator.validate(createBoard);

		// then
		assertThat(validate).isEmpty();
	}

	@DisplayName("커뮤니티 새글 작성 시, 잘못된 데이터 검증")
	@Test
	void createBoard2() {
		// given
		String tooLongTitle = "A".repeat(51);
		String tooLongContent = "B".repeat(10001);
		CommunityRequest.CreateBoard createBoard = new CommunityRequest.CreateBoard(0L, tooLongTitle, tooLongContent);

		// when
		Set<ConstraintViolation<CommunityRequest.CreateBoard>> validate = validator.validate(createBoard);

		// then
		assertThat(validate).hasSize(2);
		assertThat(validate.stream()
			.anyMatch(v -> v.getMessage().equals("제목은 1자 이상 50자 이하로 입력해주세요."))).isTrue();
		assertThat(validate.stream()
			.anyMatch(v -> v.getMessage().equals("내용은 1자 이상 10,000자 이하로 입력해주세요."))).isTrue();
	}

	@DisplayName("커뮤니티 글 수정 dto 유효성 검증")
	@Test
	void modifyBoard1() {
		// given
		String longTitle = "A".repeat(49);
		String longContent = "B".repeat(9999);
		CommunityRequest.ModifyBoard modifyBoard = new CommunityRequest.ModifyBoard(longTitle, longContent);

		// when
		Set<ConstraintViolation<CommunityRequest.ModifyBoard>> validate = validator.validate(modifyBoard);

		// then
		assertThat(validate).isEmpty();
	}

	@DisplayName("커뮤니티 글 수정 dto 유효성 검증")
	@Test
	void modifyBoard2() {
		// given
		String tooLongTitle = "A".repeat(51);
		String tooLongContent = "B".repeat(10001);
		CommunityRequest.ModifyBoard modifyBoard = new CommunityRequest.ModifyBoard(tooLongTitle, tooLongContent);

		// when
		Set<ConstraintViolation<CommunityRequest.ModifyBoard>> validate = validator.validate(modifyBoard);

		// then
		assertThat(validate).hasSize(2);
		assertThat(validate.stream()
			.anyMatch(v -> v.getMessage().equals("제목은 1자 이상 50자 이하로 입력해주세요."))).isTrue();
		assertThat(validate.stream()
			.anyMatch(v -> v.getMessage().equals("내용은 1자 이상 10,000자 이하로 입력해주세요."))).isTrue();
	}
}
