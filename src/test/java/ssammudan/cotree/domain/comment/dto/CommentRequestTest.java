package ssammudan.cotree.domain.comment.dto;

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
import ssammudan.cotree.domain.comment.type.CommentCategory;

/**
 * PackageName : ssammudan.cotree.domain.comment.dto
 * FileName    : CommentRequestTest
 * Author      : Baekgwa
 * Date        : 2025-04-14
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-14     Baekgwa               Initial creation
 */
class CommentRequestTest {

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

	@DisplayName("댓글 작성 유효성 검증")
	@Test
	void postComment() {
		// given
		String longContent = "A".repeat(1000);

		CommentRequest.PostComment request = CommentRequest.PostComment
			.builder()
			.content(longContent)
			.category(CommentCategory.COMMUNITY)
			.whereId(1L)
			.commentId(null)
			.build();

		// when
		Set<ConstraintViolation<CommentRequest.PostComment>> validate = validator.validate(request);

		// then
		assertThat(validate).isEmpty();
	}

	@DisplayName("댓글 작성 유효성 검증. 잘못된 데이터")
	@Test
	void postComment2() {
		// given
		String longContent = "A".repeat(1001);

		CommentRequest.PostComment request = CommentRequest.PostComment
			.builder()
			.content(longContent)
			.category(null)
			.whereId(null)
			.commentId(null)
			.build();

		// when
		Set<ConstraintViolation<CommentRequest.PostComment>> validate = validator.validate(request);

		// then
		assertThat(validate).hasSize(3);
		assertThat(validate.stream()
			.anyMatch(v -> v.getMessage().equals("내용은 1자 이상 1000자 이하로 입력해주세요."))).isTrue();
		assertThat(validate.stream()
			.anyMatch(v -> v.getMessage().equals("댓글을 달 Item 의 id 값은 필수입니다."))).isTrue();
		assertThat(validate.stream()
			.anyMatch(v -> v.getMessage().equals("댓글 카테고리는 필수 입니다."))).isTrue();
	}
}