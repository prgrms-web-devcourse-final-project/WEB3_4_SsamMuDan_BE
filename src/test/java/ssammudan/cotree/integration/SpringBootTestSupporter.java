package ssammudan.cotree.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import software.amazon.awssdk.services.s3.S3Client;
import ssammudan.cotree.domain.comment.service.CommentService;
import ssammudan.cotree.domain.community.service.CommunityService;
import ssammudan.cotree.domain.education.techbook.service.TechBookService;
import ssammudan.cotree.domain.education.techtube.service.TechTubeService;
import ssammudan.cotree.domain.email.service.EmailService;
import ssammudan.cotree.domain.resume.service.ResumeService;
import ssammudan.cotree.domain.review.service.TechEducationReviewService;
import ssammudan.cotree.global.config.security.user.CustomUserDetailsService;
import ssammudan.cotree.infra.s3.S3Uploader;
import ssammudan.cotree.infra.sms.MessageSender;
import ssammudan.cotree.infra.sms.SmsService;
import ssammudan.cotree.infra.viewcount.persistence.ViewCountScheduler;
import ssammudan.cotree.infra.viewcount.persistence.ViewCountStore;
import ssammudan.cotree.integration.factory.CommentDataFactory;
import ssammudan.cotree.integration.factory.CommunityDataFactory;
import ssammudan.cotree.integration.factory.DevelopmentPositionDataFactory;
import ssammudan.cotree.integration.factory.LikeDataFactory;
import ssammudan.cotree.integration.factory.MemberDataFactory;
import ssammudan.cotree.integration.factory.TechStackDataFactory;
import ssammudan.cotree.integration.factory.shared.ResumeProjectTestDataFactory;
import ssammudan.cotree.model.common.comment.repository.CommentRepository;
import ssammudan.cotree.model.common.like.repository.LikeRepository;
import ssammudan.cotree.model.community.category.repository.CommunityCategoryRepository;
import ssammudan.cotree.model.community.community.repository.CommunityRepository;
import ssammudan.cotree.model.education.techbook.techbook.repository.TechBookRepository;
import ssammudan.cotree.model.education.techtube.techtube.repository.TechTubeRepository;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.recruitment.career.career.repository.CareerRepository;
import ssammudan.cotree.model.recruitment.portfolio.portfolio.repository.PortfolioRepository;
import ssammudan.cotree.model.recruitment.resume.resume.repository.ResumeRepository;
import ssammudan.cotree.model.review.review.repository.TechEducationReviewRepository;

/**
 * PackageName : ssammudan.cotree.integration
 * FileName    : SpringBootTestSupporter
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : SpringBoot Test Supporter / 공통 Spring Context 생성
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28    Baekgwa              Initial creation
 * 2025-03-31    loadingKKamo21		  TechBook 관련 리포지토리, 서비스 추가
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class SpringBootTestSupporter {

	/**
	 * mock Mvc
	 */
	@Autowired
	public MockMvc mockMvc;

	/**
	 * Entity save factory
	 */
	@Autowired
	protected CommunityDataFactory communityDataFactory;
	@Autowired
	protected MemberDataFactory memberDataFactory;
	@Autowired
	protected LikeDataFactory likeDataFactory;
	@Autowired
	protected CommentDataFactory commentDataFactory;
	@Autowired
	protected ResumeProjectTestDataFactory resumeProjectTestDataFactory;
	@Autowired
	protected DevelopmentPositionDataFactory developmentPositionDataFactory;
	@Autowired
	protected TechStackDataFactory techStackDataFactory;

	/**
	 * Common
	 */
	@Autowired
	protected EntityManager em;
	@Autowired
	protected PasswordEncoder passwordEncoder;
	@Autowired
	protected ObjectMapper objectMapper;

	/**
	 * Repository
	 */
	@Autowired
	protected CommunityCategoryRepository communityCategoryRepository;
	@Autowired
	protected CommunityRepository communityRepository;
	@Autowired
	protected TechBookRepository techBookRepository;
	@Autowired
	protected TechTubeRepository techTubeRepository;
	@Autowired
	protected TechEducationReviewRepository techEducationReviewRepository;
	@Autowired
	protected ResumeRepository resumeRepository;
	@Autowired
	protected CareerRepository careerRepository;
	@Autowired
	protected PortfolioRepository portfolioRepository;
	@Autowired
	protected MemberRepository memberRepository;
	@Autowired
	protected LikeRepository likeRepository;
	@Autowired
	protected CommentRepository commentRepository;

	/**
	 * service
	 */
	@Autowired
	protected CommunityService communityService;
	@Autowired
	protected TechBookService techBookService;
	@Autowired
	protected TechTubeService techTubeService;
	@Autowired
	protected TechEducationReviewService techEducationReviewService;
	@Autowired
	protected ResumeService resumeService;
	@Autowired
	protected CustomUserDetailsService customUserDetailsService;
	@Autowired
	protected CommentService commentService;

	/**
	 * MockBean
	 */
	@MockitoBean
	private S3Client s3Client;
	@MockitoBean
	private S3Uploader s3Uploader;
	@MockitoBean
	private SmsService smsService;
	@MockitoBean
	private EmailService emailService;
	@MockitoBean
	private MessageSender messageSender;
	@MockitoBean
	protected ViewCountStore viewCountStore;
	@MockitoBean
	private ViewCountScheduler viewCountScheduler;
}
