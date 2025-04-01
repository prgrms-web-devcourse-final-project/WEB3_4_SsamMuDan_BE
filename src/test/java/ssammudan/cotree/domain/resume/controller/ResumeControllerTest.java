package ssammudan.cotree.domain.resume.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.transaction.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import ssammudan.cotree.domain.resume.ResumeTestHelper;
import ssammudan.cotree.domain.resume.dto.BasicInfo;
import ssammudan.cotree.domain.resume.dto.CareerInfo;
import ssammudan.cotree.domain.resume.dto.PortfolioInfo;
import ssammudan.cotree.domain.resume.dto.ResumeCreateRequest;
import ssammudan.cotree.domain.resume.service.ResumeService;
import ssammudan.cotree.infra.s3.S3Uploader;

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
@SpringBootTest
@Profile("test")
@Transactional
@AutoConfigureMockMvc
class ResumeControllerTest {

	@Autowired
	private ResumeTestHelper resumeTestHelper;

	@Autowired
	private ResumeService resumeService;

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	private S3Client s3Client;
	@MockitoBean
	private S3Uploader s3Uploader;

	private String memberId;

	@BeforeEach
	void setUp() {
		memberId = resumeTestHelper.setData();
	}

	@Test
	@DisplayName("이력서 작성 컨트롤러 테스트 성공")
	void register() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		ResumeCreateRequest request = createRegisterRequest();

		ResultActions resultActions = mockMvc.perform(post("/api/v1/recruitment/resume")
			.content(objectMapper.writeValueAsString(request))
			.param("id", memberId)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isCreated());
		resultActions.andExpect(jsonPath("$.data.resumeId").value(1L));

	}

	private ResumeCreateRequest createRegisterRequest() {
		Set<Long> positionIds = Set.of(1L, 2L);
		Set<Long> techStackIds1 = Set.of(1L, 2L, 3L);
		Set<Long> techStackIds2 = Set.of(4L, 5L, 6L);

		BasicInfo basicInfo = BasicInfo.builder()
			.profileImage("1.jpg")
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
