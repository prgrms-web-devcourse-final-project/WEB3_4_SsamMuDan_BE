package ssammudan.cotree.domain.resume.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.transaction.Transactional;
import ssammudan.cotree.domain.resume.dto.BasicInfo;
import ssammudan.cotree.domain.resume.dto.CareerInfo;
import ssammudan.cotree.domain.resume.dto.PortfolioInfo;
import ssammudan.cotree.domain.resume.dto.ResumeCreateRequest;
import ssammudan.cotree.domain.resume.dto.ResumeCreateResponse;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.domain.resume.controller
 * FileName    : ResumeControllerTest
 * Author      : kwak
 * Date        : 2025. 3. 31.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 31.     kwak               Initial creation
 */
@Transactional
class ResumeControllerTest extends SpringBootTestSupporter {

	private Member member;
	private CustomUser customUser;

	@BeforeEach
	void setUp() {
		member = resumeProjectTestDataFactory.setData();
		customUser = customUserDetailsService.loadUserByUsername(member.getUsername());
	}

	@Test
	@DisplayName("이력서 작성 컨트롤러 테스트 성공, 사진 null 일시")
	void register() throws Exception {

		objectMapper.registerModule(new JavaTimeModule());
		ResumeCreateRequest request = createRegisterRequest();
		String jsonRequest = objectMapper.writeValueAsString(request);

		MockMultipartFile requestPart = new MockMultipartFile(
			"request",
			"request.json",
			"application/json",
			jsonRequest.getBytes("UTF-8"));

		ResultActions resultActions = mockMvc.perform(multipart("/api/v1/recruitment/resume")
			.file(requestPart)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.accept(MediaType.APPLICATION_JSON)
			.with(SecurityMockMvcRequestPostProcessors.user(customUser)));

		resultActions.andExpect(status().isCreated());
		resultActions.andExpect(jsonPath("$.data.resumeId").isNumber());

	}

	@Test
	@DisplayName("이력서 상세 조회 성공")
	void detailSuccess() throws Exception {

		ResumeCreateRequest request = createRegisterRequest();
		ResumeCreateResponse response = resumeService.register(request, member.getId(), null);

		ResultActions resultActions = mockMvc.perform(
			get("/api/v1/recruitment/resume/{id}", String.valueOf(response.resumeId()))
				.with(SecurityMockMvcRequestPostProcessors.user(customUser))
				.accept(MediaType.APPLICATION_JSON));

		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.basicInfoResponse.email").value(request.basicInfo().email()))
			.andExpect(jsonPath("$.data.careerInfos[0].startDate").value("2018-01-01"))
			.andExpect(jsonPath("$.data.careerInfos[0].endDate").value("2019-12-31"))
			.andExpect(jsonPath("$.data.careerInfos[0].position").value("백엔드 엔지니어"))
			.andExpect(jsonPath("$.data.careerInfos[0].companyName").value("쌈무단"))
			.andExpect(jsonPath("$.data.careerInfos[0].careerDescription").value("웹 백엔드 개발"))
			.andExpect(jsonPath("$.data.careerInfos[0].isWorking").value(false))
			.andExpect(jsonPath("$.data.careerInfos[0].techStackInfos[0].name").value("Java"))
			.andExpect(jsonPath("$.data.careerInfos[0].techStackInfos[0].imageUrl").value(
				"https://worldvectorlogo.com/download/java.svg"))
			.andExpect(jsonPath("$.data.careerInfos.length()").value(2))
			.andExpect(jsonPath("$.data.portfolioInfos[0].startDate").value("2018-01-01"))
			.andExpect(jsonPath("$.data.portfolioInfos[0].endDate").value("2019-12-31"))
			.andExpect(jsonPath("$.data.portfolioInfos[0].projectName").value("쌈무 프로젝트"))
			.andExpect(jsonPath("$.data.portfolioInfos[0].description").value("무다무다"))
			.andExpect(jsonPath("$.data.portfolioInfos[0].techStackInfos[0].name").value("Java"))
			.andExpect(jsonPath("$.data.portfolioInfos[0].techStackInfos[0].imageUrl").value(
				"https://worldvectorlogo.com/download/java.svg"))
			.andExpect(jsonPath("$.data.portfolioInfos.length()").value(2));
	}

	@Test
	@DisplayName("이력서 전체 조회 성공")
	void getResumeListSuccess() throws Exception {

		ResumeCreateRequest request = createRegisterRequest();
		resumeService.register(request, member.getId(), null);
		resumeService.register(request, member.getId(), null);

		ResultActions resultActions = mockMvc
			.perform(get("/api/v1/recruitment/resume")
				.accept(MediaType.APPLICATION_JSON));

		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.totalElements").isNumber())
			.andExpect(jsonPath("$.data.totalPages").isNumber())
			.andExpect(jsonPath("$.data.pageSize").value(16))
			.andExpect(jsonPath("$.data.pageNo").value(0))
			.andExpect(jsonPath("$.data.content.length()").value(2))
			.andExpect(jsonPath("$.data.content[0].resumeId").isNumber())
			.andExpect(jsonPath("$.data.content[0].profileImage").isEmpty())
			.andExpect(jsonPath("$.data.content[0].isOpen").value(true))
			.andExpect(jsonPath("$.data.content[0].positions.length()").value(2))
			.andExpect(jsonPath("$.data.content[0].tackStacksId.length()").value(3))
			.andExpect(jsonPath("$.data.content[0].year").value(1))
			.andExpect(jsonPath("$.data.content[0].introduction").value("안녕하세요 쌈무단입니다."))
			.andExpect(jsonPath("$.data.content[0].viewCount").value(0));

	}

	private ResumeCreateRequest createRegisterRequest() {
		Set<Long> positionIds = Set.of(1L, 2L);
		Set<Long> techStackIds1 = Set.of(1L, 2L, 3L);
		Set<Long> techStackIds2 = Set.of(4L, 5L, 6L);

		BasicInfo basicInfo = BasicInfo.builder()
			.email("test@example.com")
			.years(1)
			.introduction("안녕하세요 쌈무단입니다.")
			.developPositionIds(positionIds)
			.techStackIds(techStackIds1)
			.build();

		CareerInfo customCareer1 = CareerInfo.builder()
			.startDate(LocalDate.of(2018, 1, 1))
			.endDate(LocalDate.of(2019, 12, 31))
			.position("백엔드 엔지니어")
			.companyName("쌈무단")
			.careerDescription("웹 백엔드 개발")
			.isWorking(false)
			.techStackIds(techStackIds1)
			.build();

		CareerInfo customCareer2 = CareerInfo.builder()
			.startDate(LocalDate.of(2020, 1, 1))
			.endDate(LocalDate.of(2021, 12, 31))
			.position("프론트 엔지니어")
			.companyName("삼성")
			.careerDescription("웹 프론트 개발")
			.isWorking(false)
			.techStackIds(techStackIds2)
			.build();

		PortfolioInfo portfolioInfo1 = PortfolioInfo.builder()
			.startDate(LocalDate.of(2018, 1, 1))
			.endDate(LocalDate.of(2019, 12, 31))
			.projectName("쌈무 프로젝트")
			.projectDescription("무다무다")
			.techStackIds(techStackIds1)
			.build();

		PortfolioInfo portfolioInfo2 = PortfolioInfo.builder()
			.startDate(LocalDate.of(2020, 1, 1))
			.endDate(LocalDate.of(2021, 12, 31))
			.projectName("쌈무 프로젝트2")
			.projectDescription("무다무다2")
			.techStackIds(techStackIds2)
			.build();

		return ResumeCreateRequest.builder()
			.basicInfo(basicInfo)
			.careerInfos(List.of(customCareer1, customCareer2))
			.portfolioInfos(List.of(portfolioInfo1, portfolioInfo2))
			.build();
	}
}
