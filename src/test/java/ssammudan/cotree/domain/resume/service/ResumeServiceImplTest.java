package ssammudan.cotree.domain.resume.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.transaction.Transactional;
import ssammudan.cotree.domain.resume.dto.BasicInfo;
import ssammudan.cotree.domain.resume.dto.BasicInfoResponse;
import ssammudan.cotree.domain.resume.dto.CareerInfo;
import ssammudan.cotree.domain.resume.dto.CareerInfoResponse;
import ssammudan.cotree.domain.resume.dto.PortfolioInfo;
import ssammudan.cotree.domain.resume.dto.PortfolioInfoResponse;
import ssammudan.cotree.domain.resume.dto.ResumeCreateRequest;
import ssammudan.cotree.domain.resume.dto.ResumeCreateResponse;
import ssammudan.cotree.domain.resume.dto.ResumeDetailResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.recruitment.career.career.entity.Career;
import ssammudan.cotree.model.recruitment.portfolio.portfolio.entity.Portfolio;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

/**
 * PackageName : ssammudan.cotree.domain.resume.service
 * FileName    : ResumeServiceImplTest
 * Author      : kwak
 * Date        : 2025. 3. 30.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 30.     kwak               Initial creation
 */

// @SpringBootTest
// @ActiveProfiles("test")
@Transactional
class ResumeServiceImplTest extends SpringBootTestSupporter {

	// @Autowired
	// private ResumeTestHelper resumeTestHelper;

	// @Autowired
	// private ResumeRepository resumeRepository;
	//
	// @Autowired
	// private CareerRepository careerRepository;
	//
	// @Autowired
	// private PortfolioRepository portfolioRepository;
	//
	// @Autowired
	// private ResumeService resumeService;

	/**
	 * MockBean
	 */
	// @MockitoBean
	// private S3Client s3Client;
	// @MockitoBean
	// private S3Uploader s3Uploader;
	// @MockitoBean
	// SmsService smsService;
	// @MockitoBean
	// EmailService emailService;

	private String memberId;

	@BeforeEach
	void setUp() {
		memberId = resumeDataFactory.setData();
	}

	@Test
	@DisplayName("이력서 정상 작성")
	void register() {

		//given
		ResumeCreateRequest request = createRequest();

		//when
		ResumeCreateResponse response = resumeService.register(request, memberId, null);

		//then
		// resume
		Resume savedResume = resumeRepository.findById(response.resumeId()).orElse(null);

		assertThat(response).isNotNull();
		assertThat(response.resumeId()).isEqualTo(savedResume.getId());

		assertThat(savedResume.getMember().getId()).isEqualTo(memberId);
		assertThat(savedResume.getProfileImage()).isEqualTo(null);
		assertThat(savedResume.getEmail()).isEqualTo(request.basicInfo().email());
		assertThat(savedResume.getYears()).isEqualTo(request.basicInfo().years());
		assertThat(savedResume.getIntroduction()).isEqualTo(request.basicInfo().introduction());

		assertThat(savedResume.getResumeDevelopmentPositions()).hasSize(
			request.basicInfo().developPositionIds().size());
		assertThat(savedResume.getResumeTechStacks()).hasSize(request.basicInfo().techStackIds().size());

		// career
		List<Career> careers = careerRepository.findByResume(savedResume);
		assertThat(careers).hasSize(2);

		Career career1 = careers.get(0);
		Career career2 = careers.get(1);

		assertThat(career1.getPosition()).isEqualTo("백엔드 엔지니어");
		assertThat(career1.getCompany()).isEqualTo("쌈무단");
		assertThat(career1.getDescription()).isEqualTo("웹 백엔드 개발");
		assertThat(career1.getStartDate()).isEqualTo(LocalDate.of(2018, 1, 1));
		assertThat(career1.getEndDate()).isEqualTo(LocalDate.of(2019, 12, 31));
		assertThat(career1.isWorking()).isFalse();
		assertThat(career1.getCareerTechStacks().size()).isEqualTo(3);

		assertThat(career2.getPosition()).isEqualTo("프론트 엔지니어");
		assertThat(career2.getCompany()).isEqualTo("삼성");
		assertThat(career2.getDescription()).isEqualTo("웹 프론트 개발");
		assertThat(career2.getStartDate()).isEqualTo(LocalDate.of(2020, 1, 1));
		assertThat(career2.getEndDate()).isEqualTo(LocalDate.of(2021, 12, 31));
		assertThat(career2.isWorking()).isFalse();
		assertThat(career2.getCareerTechStacks().size()).isEqualTo(3);

		// portfolio
		List<Portfolio> portfolios = portfolioRepository.findByResume(savedResume);
		assertThat(portfolios).hasSize(2);

		Portfolio portfolio1 = portfolios.get(0);
		Portfolio portfolio2 = portfolios.get(1);

		assertThat(portfolio1.getName()).isEqualTo("쌈무 프로젝트");
		assertThat(portfolio1.getDescription()).isEqualTo("무다무다");
		assertThat(portfolio1.getStartDate()).isEqualTo(LocalDate.of(2018, 1, 1));
		assertThat(portfolio1.getEndDate()).isEqualTo(LocalDate.of(2019, 12, 31));
		assertThat(portfolio1.getPortfolioTechStacks().size()).isEqualTo(3);

		assertThat(portfolio2.getName()).isEqualTo("쌈무 프로젝트2");
		assertThat(portfolio2.getDescription()).isEqualTo("무다무다2");
		assertThat(portfolio2.getStartDate()).isEqualTo(LocalDate.of(2020, 1, 1));
		assertThat(portfolio2.getEndDate()).isEqualTo(LocalDate.of(2021, 12, 31));
		assertThat(portfolio2.getPortfolioTechStacks().size()).isEqualTo(3);
		assertThat(portfolio2.getPortfolioTechStacks().size()).isEqualTo(3);
	}

	@Test
	@DisplayName("이력서 등록 시 member id 가 일치하지 않을 경우 에러 발생")
	void notMatchMemberIdInRegister() {

		// given
		ResumeCreateRequest request = createRequest();
		String wrongId = "wrong";

		// when
		// then
		assertThatThrownBy(() -> resumeService.register(request, wrongId, null))
			.hasMessageContaining("회원을 찾을 수 없습니다.")
			.isInstanceOf(GlobalException.class);
	}

	@Test
	@DisplayName("잘못된 resumeID 입력 시 상세 조회 실패")
	void detailFailWrongIdInRegister() {

		// given
		Long wrongId = 9999999L;
		ResumeCreateRequest request = createRequest();
		resumeService.register(request, memberId, null);

		// when
		// then
		assertThatThrownBy(() -> resumeService.detail(wrongId))
			.hasMessageContaining("해당하는 이력서를 찾을 수 없습니다")
			.isInstanceOf(GlobalException.class);

	}

	@Test
	@DisplayName("이력서 상세 조회 성공")
	void detail() {

		// given
		ResumeCreateRequest request = createRequest();
		ResumeCreateResponse createResponse = resumeService.register(request, memberId, null);
		Long resumeId = createResponse.resumeId();

		// when
		ResumeDetailResponse response = resumeService.detail(resumeId);

		// then
		// Basic Info 검증
		BasicInfoResponse basic = response.basicInfoResponse();
		assertThat(basic.email()).isEqualTo(request.basicInfo().email());
		assertThat(basic.introduction()).isEqualTo(request.basicInfo().introduction());
		assertThat(basic.years()).isEqualTo(request.basicInfo().years());

		assertThat(basic.techStackInfos()).hasSize(request.basicInfo().techStackIds().size());
		assertThat(basic.positionNames()).hasSize(request.basicInfo().developPositionIds().size());

		// Career 검증
		assertThat(response.careerInfos()).hasSize(2);

		CareerInfoResponse career1 = response.careerInfos().get(0);
		CareerInfoResponse career2 = response.careerInfos().get(1);

		assertThat(career1.companyName()).isEqualTo("쌈무단");
		assertThat(career1.position()).isEqualTo("백엔드 엔지니어");
		assertThat(career1.careerDescription()).isEqualTo("웹 백엔드 개발");
		assertThat(career1.techStackInfos()).hasSize(3);

		assertThat(career2.companyName()).isEqualTo("삼성");
		assertThat(career2.position()).isEqualTo("프론트 엔지니어");
		assertThat(career2.careerDescription()).isEqualTo("웹 프론트 개발");
		assertThat(career2.techStackInfos()).hasSize(3);

		// Portfolio 검증
		assertThat(response.portfolioInfos()).hasSize(2);

		PortfolioInfoResponse portfolio1 = response.portfolioInfos().get(0);
		PortfolioInfoResponse portfolio2 = response.portfolioInfos().get(1);

		assertThat(portfolio1.projectName()).isEqualTo("쌈무 프로젝트");
		assertThat(portfolio1.description()).isEqualTo("무다무다");
		assertThat(portfolio1.techStackInfos()).hasSize(3);

		assertThat(portfolio2.projectName()).isEqualTo("쌈무 프로젝트2");
		assertThat(portfolio2.description()).isEqualTo("무다무다2");
		assertThat(portfolio2.techStackInfos()).hasSize(3);

	}

	private ResumeCreateRequest createRequest() {

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
