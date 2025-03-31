package ssammudan.cotree.domain.member.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.domain.member.service.MemberService;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

/**
 * PackageName : ssammudan.cotree.domain.member.controller
 * FileName    : MemberControllerTest
 * Author      : hc
 * Date        : 25. 3. 28.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.     hc               Initial creation
 */
@WebMvcTest(MemberController.class)
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private MemberService memberService;

	@Test
	@WithMockUser
	void signUp() throws Exception {

		Member mockMember = new Member(
			"randomUUID()",
			"test123@naver.com",
			"이름",
			"닉네임",
			"비밀 번호",
			"01012345678",
			null,
			MemberRole.USER,
			MemberStatus.ACTIVE
		);

		Mockito.when(memberService.signUp(Mockito.any(MemberSignupRequest.class)))
			.thenReturn(mockMember);

		MemberSignupRequest memberSignupRequest = new MemberSignupRequest(
			"test123@naver.com",
			"password",
			"이름",
			"닉네임",
			"01012345678"
		);

		String requestJson = objectMapper.writeValueAsString(memberSignupRequest);

		// POST 요청을 보내고, 결과 검증
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member/signup")
				.contentType("application/json")
				.content(requestJson)  // JSON 형식으로 변환된 Request 객체 전달
				.with(csrf()))
			.andExpect(
				MockMvcResultMatchers.status().isOk())// Todo: Mock에는 AOP 적용 안됨  //.isCreated())// HTTP Status 201
			.andReturn();

		// 반환된 결과에서 BaseResponse 객체 추출
		String responseContent = result.getResponse().getContentAsString();
		BaseResponse<SuccessCode> response = objectMapper.readValue(responseContent, BaseResponse.class);

		// BaseResponse 객체에서 Data 객체 추출
		Assertions.assertThat(response.getIsSuccess()).isTrue();
		Assertions.assertThat(response.getCode()).isEqualTo("201");
		Assertions.assertThat(response.getMessage()).isEqualTo("회원가입을 완료했습니다.");
	}
}
