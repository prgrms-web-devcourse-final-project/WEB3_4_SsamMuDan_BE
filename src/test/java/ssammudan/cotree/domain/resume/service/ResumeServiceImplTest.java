package ssammudan.cotree.domain.resume.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.transaction.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import ssammudan.cotree.domain.email.service.EmailService;
import ssammudan.cotree.domain.phone.service.SmsService;
import ssammudan.cotree.domain.resume.ResumeTestHelper;
import ssammudan.cotree.domain.resume.dto.BasicInfo;
import ssammudan.cotree.domain.resume.dto.CareerInfo;
import ssammudan.cotree.domain.resume.dto.PortfolioInfo;
import ssammudan.cotree.domain.resume.dto.ResumeCreateRequest;
import ssammudan.cotree.domain.resume.dto.ResumeCreateResponse;
import ssammudan.cotree.infra.s3.S3Uploader;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;
import ssammudan.cotree.model.recruitment.resume.resume.repository.ResumeRepository;

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

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ResumeServiceImplTest {

	@Autowired
	private ResumeTestHelper resumeTestHelper;

	@Autowired
	private ResumeRepository resumeRepository;

	@Autowired
	private ResumeService resumeService;

	/**
	 * MockBean
	 */
	@MockitoBean
	private S3Client s3Client;
	@MockitoBean
	private S3Uploader s3Uploader;
	@MockitoBean
	SmsService service;
	@MockitoBean
	EmailService emailService;

	private String memberId;

	@BeforeEach
	void setUp() {
		memberId = resumeTestHelper.setData();
	}

	@Test
	@DisplayName("이력서 정상 작성")
	void register() {

		//given
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

		ResumeCreateRequest resumeCreateRequest = ResumeCreateRequest.builder()
			.basicInfo(basicInfo)
			.careerInfos(List.of(customCareer1, customCareer2))
			.portfolioInfos(List.of(portfolioInfo1, portfolioInfo2))
			.build();

		//when
		ResumeCreateResponse response = resumeService.register(resumeCreateRequest, memberId, null);

		//then
		Resume savedResume = resumeRepository.findById(response.resumeId()).orElse(null);

		assertThat(response).isNotNull();
		assertThat(response.resumeId()).isEqualTo(savedResume.getId());

		assertThat(savedResume.getMember().getId()).isEqualTo(memberId);
		assertThat(savedResume.getProfileImage()).isEqualTo(null);
		assertThat(savedResume.getEmail()).isEqualTo(basicInfo.email());
		assertThat(savedResume.getYears()).isEqualTo(basicInfo.years());
		assertThat(savedResume.getIntroduction()).isEqualTo(basicInfo.introduction());

		assertThat(savedResume.getResumeDevelopmentPositions()).hasSize(positionIds.size());
		assertThat(savedResume.getResumeTechStacks()).hasSize(techStackIds1.size());

	}
}
