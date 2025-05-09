package ssammudan.cotree.domain.category.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.transaction.annotation.Transactional;

import software.amazon.awssdk.services.s3.S3Client;
import ssammudan.cotree.domain.category.CategoryTestHelper;
import ssammudan.cotree.domain.email.service.EmailService;
import ssammudan.cotree.infra.s3.S3Uploader;
import ssammudan.cotree.infra.sms.MessageSender;
import ssammudan.cotree.infra.sms.SmsService;

/**
 * PackageName : ssammudan.cotree.domain.category.controller
 * FileName    : CategoryControllerTest
 * Author      : kwak
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     kwak               Initial creation
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Profile("test")
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	CategoryTestHelper categoryTestHelper;

	@MockitoBean
	private S3Client s3Client;
	@MockitoBean
	private S3Uploader s3Uploader;
	@MockitoBean
	private SmsService smsService;
	@MockitoBean
	private MessageSender messageSender;
	@MockitoBean
	private EmailService emailService;

	@BeforeEach
	void setUp() {
		categoryTestHelper.setData();
	}

	@Test
	@DisplayName("기술 스택 전체 조회 성공")
	void findSkills() throws Exception {

		ResultActions resultActions = mockMvc.perform(get("/api/v1/category/skill")
			.accept(MediaType.APPLICATION_JSON));

		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].imageUrl").value("https://worldvectorlogo.com/download/java.svg"))
			.andExpect(jsonPath("$.data[0].name").value("Java"))
			.andExpect(jsonPath("$.data.size()").value(14));
	}

	@Test
	@DisplayName("개발 직무 전체 조회 성공")
	void findPositions() throws Exception {

		ResultActions resultActions = mockMvc.perform(get("/api/v1/category/position")
			.accept(MediaType.APPLICATION_JSON));

		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].name").value("프론트엔드"))
			.andExpect(jsonPath("$.data.size()").value(3));
	}
}
