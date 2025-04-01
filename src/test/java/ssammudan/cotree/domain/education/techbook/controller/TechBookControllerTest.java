package ssammudan.cotree.domain.education.techbook.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.data.domain.Page;
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
import ssammudan.cotree.domain.education.techbook.dto.TechBookResponse;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.SuccessCode;
import ssammudan.cotree.integration.WebMvcTestSupporter;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;

/**
 * PackageName : ssammudan.cotree.domain.education.techbook.controller
 * FileName    : TechBookControllerTest
 * Author      : loadingKKamo21
 * Date        : 25. 3. 31.
 * Description : TechBook 컨트롤러 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.    loadingKKamo21       Initial creation
 * 25. 4. 1.     loadingKKamo21       GET: /api/v1/education/techbook 테스트 추가
 */
class TechBookControllerTest extends WebMvcTestSupporter {

	private TechBookResponse.Detail createTechBookResponseDetail(final Long id) {
		return TechBookResponse.Detail.from(entityFixtureMonkey.giveMeBuilder(TechBook.class)
			.set("id", id)
			.set("totalRating", Arbitraries.integers().greaterOrEqual(0))
			.set("totalReviewCount", Arbitraries.integers().greaterOrEqual(0))
			.set("techBookPage", Arbitraries.integers().greaterOrEqual(0))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.set("viewCount", Arbitraries.integers().greaterOrEqual(0))
			.sample());
	}

	private List<TechBookResponse.ListInfo> createTechBookResponseListInfo(final int size) {
		return entityFixtureMonkey.giveMeBuilder(TechBook.class)
			.set("totalRating", Arbitraries.integers().greaterOrEqual(0))
			.set("totalReviewCount", Arbitraries.integers().greaterOrEqual(0))
			.set("techBookPage", Arbitraries.integers().greaterOrEqual(0))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.set("viewCount", Arbitraries.integers().greaterOrEqual(0))
			.sampleList(size)
			.stream()
			.map(TechBookResponse.ListInfo::from)
			.toList();
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Success] getTechBookById(): TechBook 단 건 조회")
	void getTechBookById(@Min(1) @Max(Long.MAX_VALUE) final Long id) throws Exception {
		//Given
		TechBookResponse.Detail responseDto = createTechBookResponseDetail(id);

		when(techBookService.findTechBookById(id)).thenReturn(responseDto);

		//When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/education/techbook/{id}/info", id));

		//Then
		BaseResponse<TechBookResponse.Detail> baseResponse = BaseResponse.success(
			SuccessCode.TECH_BOOK_READ_SUCCESS, responseDto
		);
		String responseBody = objectMapper.writeValueAsString(baseResponse);

		resultActions.andExpect(handler().handlerType(TechBookController.class))
			.andExpect(handler().methodName("getTechBookById"))
			.andExpect(status().isOk())
			.andExpect(content().json(responseBody))
			.andDo(print());
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Invalid] getTechBookById_invalidId(): TechBook 단 건 조회, Invalid ID")
	void getTechBookById_unknownId(@Min(Long.MIN_VALUE) @Max(0) final Long invalidId) throws Exception {
		//Given

		//When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/education/techbook/{id}/info", invalidId)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE));

		//Then
		BaseResponse<Void> baseResponse = BaseResponse.fail(ErrorCode.INVALID_INPUT_VALUE);
		String responseBody = objectMapper.writeValueAsString(baseResponse);

		resultActions.andExpect(handler().handlerType(TechBookController.class))
			.andExpect(handler().methodName("getTechBookById"))
			.andExpect(status().isBadRequest())
			.andExpect(content().json(responseBody))
			.andDo(print());
	}

	@RepeatedTest(10)
	@DisplayName("[Success] getTechBooks(): TechBook 다 건 조회, 페이징 적용")
	void getTechBooks() throws Exception {
		//Given
		List<TechBookResponse.ListInfo> content = createTechBookResponseListInfo(50);
		Page<TechBookResponse.ListInfo> resopnseDto = new PageImpl<>(
			content,
			PageRequest.of(0, 16, Sort.Direction.DESC, "createdAt"),
			content.size()
		);

		when(techBookService.findAllTechBooks(anyString(), any(Pageable.class))).thenReturn(resopnseDto);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", "0");
		params.add("size", "16");
		params.add("sort", "createdAt");
		params.add("direction", "DESC");
		params.add("keyword", dtoFixtureMonkey.giveMeOne(String.class));

		//When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/education/techbook").params(params));

		//Then
		BaseResponse<Object> baseResponse = BaseResponse.success(SuccessCode.TECH_BOOK_LIST_FIND_SUCCESS, resopnseDto);
		String responseBody = objectMapper.writeValueAsString(baseResponse);

		resultActions.andExpect(handler().handlerType(TechBookController.class))
			.andExpect(handler().methodName("getTechBooks"))
			.andExpect(status().isOk())
			.andExpect(content().json(responseBody))
			.andDo(print());
	}

}