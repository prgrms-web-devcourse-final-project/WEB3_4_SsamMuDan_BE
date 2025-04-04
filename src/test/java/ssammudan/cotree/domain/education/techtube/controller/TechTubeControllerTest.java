package ssammudan.cotree.domain.education.techtube.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Duration;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import net.jqwik.api.Arbitraries;

import autoparams.AutoSource;
import autoparams.Repeat;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeResponse;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.global.response.SuccessCode;
import ssammudan.cotree.integration.WebMvcTestSupporter;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;

/**
 * PackageName : ssammudan.cotree.domain.education.techtube.controller
 * FileName    : TechTubeControllerTest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : TechTube 컨트롤러 테스트
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 */
class TechTubeControllerTest extends WebMvcTestSupporter {

	private TechTubeResponse.Detail createTechTubeResponseDetail(final Long id) {
		return TechTubeResponse.Detail.from(entityFixtureMonkey.giveMeBuilder(TechTube.class)
			.set("id", id)
			.set("totalRating", Arbitraries.integers().greaterOrEqual(0))
			.set("totalReviewCount", Arbitraries.integers().greaterOrEqual(0))
			.set("techTubeDuration", Arbitraries.longs().greaterOrEqual(0).map(Duration::ofSeconds))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.set("viewCount", Arbitraries.integers().greaterOrEqual(0))
			.sample());
	}

	private List<TechTubeResponse.ListInfo> createTechTubeResponseListInfo(final int size) {
		return entityFixtureMonkey.giveMeBuilder(TechTube.class)
			.set("totalRating", Arbitraries.integers().greaterOrEqual(0))
			.set("totalReviewCount", Arbitraries.integers().greaterOrEqual(0))
			.set("techTubeDuration", Arbitraries.longs().greaterOrEqual(0).map(Duration::ofSeconds))
			.set("price", Arbitraries.integers().greaterOrEqual(0))
			.set("viewCount", Arbitraries.integers().greaterOrEqual(0))
			.sampleList(size)
			.stream()
			.map(TechTubeResponse.ListInfo::from)
			.toList();
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Success] getTechTubeById(): TechTube 단 건 조회")
	void getTechTubeById(@Min(1) @Max(Long.MAX_VALUE) final Long id) throws Exception {
		//Given
		TechTubeResponse.Detail responseDto = createTechTubeResponseDetail(id);

		when(techTubeService.findTechTubeById(id)).thenReturn(responseDto);

		//When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/education/techtube/{id}/info", id));

		//Then
		BaseResponse<TechTubeResponse.Detail> baseResponse = BaseResponse.success(
			SuccessCode.TECH_TUBE_READ_SUCCESS, responseDto
		);
		String responseBody = objectMapper.writeValueAsString(baseResponse);

		resultActions.andExpect(handler().handlerType(TechTubeController.class))
			.andExpect(handler().methodName("getTechTubeById"))
			.andExpect(status().isOk())
			.andExpect(content().json(responseBody))
			.andDo(print());
	}

	@ParameterizedTest
	@AutoSource
	@Repeat(10)
	@DisplayName("[Invalid] getTechTubeById_invalidId(): TechTube 단 건 조회, Invalid ID")
	void getTechTubeById_unknownId(@Min(Long.MIN_VALUE) @Max(0) final Long invalidId) throws Exception {
		//Given

		//When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/education/techtube/{id}/info", invalidId));

		//Then
		BaseResponse<Void> baseResponse = BaseResponse.fail(ErrorCode.INVALID_INPUT_VALUE);
		String responseBody = objectMapper.writeValueAsString(baseResponse);

		resultActions.andExpect(handler().handlerType(TechTubeController.class))
			.andExpect(handler().methodName("getTechTubeById"))
			.andExpect(status().isBadRequest())
			.andExpect(content().json(responseBody))
			.andDo(print());
	}

	//@RepeatedTest(10)
	@Test
	@DisplayName("[Success] getTechTubes(): TechTube 다 건 조회, 페이징 적용")
	void getTechBooks() throws Exception {
		//Given
		List<TechTubeResponse.ListInfo> content = createTechTubeResponseListInfo(50);
		PageResponse<TechTubeResponse.ListInfo> resopnseDto = PageResponse.of(new PageImpl<>(
			content,
			PageRequest.of(0, 16, Sort.Direction.DESC, "createdAt"),
			content.size()
		));

		when(techTubeService.findAllTechTubes(anyString(), any(Pageable.class))).thenReturn(resopnseDto);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", "0");
		params.add("size", "16");
		params.add("sort", "LATEST");
		params.add("keyword", dtoFixtureMonkey.giveMeOne(String.class));

		//When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/education/techtube").params(params));

		//Then
		BaseResponse<PageResponse<TechTubeResponse.ListInfo>> baseResponse = BaseResponse.success(
			SuccessCode.TECH_TUBE_LIST_FIND_SUCCESS, resopnseDto
		);
		String responseBody = objectMapper.writeValueAsString(baseResponse);

		resultActions.andExpect(handler().handlerType(TechTubeController.class))
			.andExpect(handler().methodName("getTechTubes"))
			.andExpect(status().isOk())
			.andExpect(content().json(responseBody))
			.andDo(print());
	}

}