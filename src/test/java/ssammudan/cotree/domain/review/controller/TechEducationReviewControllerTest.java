package ssammudan.cotree.domain.review.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import net.jqwik.api.Arbitraries;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.domain.review.dto.TechEducationReviewRequest;
import ssammudan.cotree.domain.review.dto.TechEducationReviewResponse;
import ssammudan.cotree.global.annotation.CustomWithMockUser;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.global.response.SuccessCode;
import ssammudan.cotree.integration.WebMvcTestSupporter;
import ssammudan.cotree.model.education.type.EducationType;
import ssammudan.cotree.model.review.review.entity.TechEducationReview;
import ssammudan.cotree.model.review.reviewtype.entity.TechEducationType;

/**
 * PackageName : ssammudan.cotree.domain.review.controller
 * FileName    : TechEducationReviewControllerTest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 3.
 * Description : TechEducationReviwe 컨트롤러 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 3.     loadingKKamo21       Initial creation
 */
class TechEducationReviewControllerTest extends WebMvcTestSupporter {

	private TechEducationType createTechEducationType() {
		return entityFixtureMonkey.giveMeBuilder(TechEducationType.class)
			.set("id", 1L)
			.set("name", "TechTube")
			.sample();
	}

	private List<TechEducationReviewResponse.ReviewDetail> createTechEducationReviews(
		final TechEducationType techEducationType, final int size
	) {
		return entityFixtureMonkey.giveMeBuilder(TechEducationReview.class)
			.set("techEducationType", techEducationType)
			.sampleList(size)
			.stream()
			.map(TechEducationReviewResponse.ReviewDetail::from)
			.toList();
	}

	//@RepeatedTest(10)
	@Test
	@CustomWithMockUser
	@DisplayName("[Success] createTechEducationReview(): 교육 컨텐츠 리뷰 작성")
	void createTechEducationReview() throws Exception {
		//Given
		TechEducationReviewRequest.ReviewCreate requestDto = dtoFixtureMonkey.giveMeBuilder(
				TechEducationReviewRequest.ReviewCreate.class
			).set("techEducationType", EducationType.TECH_TUBE)
			.set("rating", Arbitraries.integers().between(0, 5))
			.sample();
		String requestBody = objectMapper.writeValueAsString(requestDto);

		when(techEducationReviewService.createTechEducationReview(anyString(),
			any(TechEducationReviewRequest.ReviewCreate.class))).thenReturn(1L);

		//When
		ResultActions resultActions = mockMvc.perform(post("/api/v1/education/review")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(requestBody));

		//Then
		BaseResponse<Object> baseResponse = BaseResponse.success(SuccessCode.TECH_EDUCATION_REVIEW_CREATE_SUCCESS);
		String responseBody = objectMapper.writeValueAsString(baseResponse);

		resultActions.andExpect(handler().handlerType(TechEducationReviewController.class))
			.andExpect(handler().methodName("createTechEducationReview"))
			.andExpect(status().isCreated())
			.andExpect(content().json(responseBody))
			.andDo(print());
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@CustomWithMockUser
	@DisplayName("[Success] getTechEducationReviews(): TechEducation 리뷰 다 건 조회, 페이징 적용")
	void getTechEducationReviews(@Min(1) @Max(Long.MAX_VALUE) final Long itemId) throws Exception {
		//Given
		List<TechEducationReviewResponse.ReviewDetail> content = createTechEducationReviews(createTechEducationType(),
			50);
		PageResponse<TechEducationReviewResponse.ReviewDetail> responseDto = PageResponse.of(new PageImpl<>(
			content,
			PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt"),
			content.size())
		);

		TechEducationReviewRequest.ReviewRead requestDto = dtoFixtureMonkey.giveMeBuilder(
				TechEducationReviewRequest.ReviewRead.class
			).set("techEducationType", EducationType.TECH_TUBE)
			.set("itemId", itemId)
			.sample();

		when(techEducationReviewService.findAllTechEducationReviews(eq(requestDto), any(Pageable.class))).thenReturn(
			responseDto);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", "0");
		params.add("size", "10");
		params.add("sort", "LATEST");
		params.add("reviewType", "TECH_TUBE");
		params.add("itemId", itemId.toString());

		//When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/education/review").params(params));

		//Then
		BaseResponse<PageResponse<TechEducationReviewResponse.ReviewDetail>> baseResponse = BaseResponse.success(
			SuccessCode.TECH_EDUCATION_REVIEW_LIST_FIND_SUCCESS, responseDto
		);
		String responseBody = objectMapper.writeValueAsString(baseResponse);

		resultActions.andExpect(handler().handlerType(TechEducationReviewController.class))
			.andExpect(handler().methodName("getTechEducationReviews"))
			.andExpect(status().isOk())
			.andExpect(content().json(responseBody))
			.andDo(print());
	}

}