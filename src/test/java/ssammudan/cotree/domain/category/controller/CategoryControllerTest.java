package ssammudan.cotree.domain.category.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
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
import ssammudan.cotree.domain.category.service.CategoryService;
import ssammudan.cotree.infra.s3.S3Uploader;

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
@AutoConfigureMockMvc
@Transactional
@Profile("test")
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	CategoryTestHelper categoryTestHelper;

	@MockitoBean
	private S3Client s3Client;
	@MockitoBean
	private S3Uploader s3Uploader;

	@BeforeEach
	void setUp() {
		categoryTestHelper.setData();
	}

	@Test
	void findSkills() throws Exception {

		ResultActions resultActions = mockMvc.perform(get("/api/v1/category/skill")
			.accept(MediaType.APPLICATION_JSON));

		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].imageUrl").value("https://worldvectorlogo.com/download/java.svg"))
			.andExpect(jsonPath("$.data[0].name").value("Java"))
			.andExpect(jsonPath("$.data.size()").value(14));
	}
}
