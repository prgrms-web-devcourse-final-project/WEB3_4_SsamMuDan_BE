package ssammudan.cotree.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import ssammudan.cotree.domain.community.service.CommunityService;
import ssammudan.cotree.integration.factory.CommunityDataFactory;
import ssammudan.cotree.integration.factory.MemberDataFactory;
import ssammudan.cotree.model.community.category.repository.CommunityCategoryRepository;
import ssammudan.cotree.model.community.community.repository.CommunityRepository;

/**
 * PackageName : ssammudan.cotree.integration
 * FileName    : SpringBootTestSupporter
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : SpringBoot Test Supporter / 공통 Spring Context 생성
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28     Baekgwa               Initial creation
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
	protected CommunityDataFactory communityFactory;
	@Autowired
	protected MemberDataFactory memberDataFactory;

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

	/**
	 * service
	 */
	@Autowired
	protected CommunityService communityService;
}
