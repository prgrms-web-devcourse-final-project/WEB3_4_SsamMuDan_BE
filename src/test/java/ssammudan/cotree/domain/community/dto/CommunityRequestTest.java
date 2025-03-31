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
		CommunityRequest.CreateBoard createBoard = new CommunityRequest.CreateBoard("코드리뷰", "제목", "내용");

		// when
		Set<ConstraintViolation<CommunityRequest.CreateBoard>> validate = validator.validate(createBoard);

		// then
		assertThat(validate).hasSize(0);
	}

	@DisplayName("커뮤니티 새글 작성 시, 잘못된 데이터 검증")
	@Test
	void createBoard2() {
		// given
		String tooLongTitle = "A".repeat(51);
		String tooLongContent = "B".repeat(1001);
		CommunityRequest.CreateBoard createBoard = new CommunityRequest.CreateBoard("", tooLongTitle, tooLongContent);

		// when
		Set<ConstraintViolation<CommunityRequest.CreateBoard>> validate = validator.validate(createBoard);

		// then
		assertThat(validate).hasSize(3);
		assertThat(validate.stream()
				.anyMatch(v -> v.getMessage().equals("글 카테고리는 필수값 입니다."))).isTrue();
		assertThat(validate.stream()
				.anyMatch(v -> v.getMessage().equals("제목은 1자 이상 50자 이하로 입력해주세요."))).isTrue();
		assertThat(validate.stream()
				.anyMatch(v -> v.getMessage().equals("내용은 1자 이상 1000자 이하로 입력해주세요."))).isTrue();
	}
}
