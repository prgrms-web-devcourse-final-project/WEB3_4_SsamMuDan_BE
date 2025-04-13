package ssammudan.cotree.domain.community.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import ssammudan.cotree.domain.community.dto.CommunityRequest;
import ssammudan.cotree.global.response.SuccessCode;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.integration.security.WithCustomMember;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.domain.community.controller
 * FileName    : CommunityControllerTest
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : Community Domain Controller Layer Test
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28     Baekgwa               Initial creation
 */
@Transactional
class CommunityControllerTest extends SpringBootTestSupporter {

	@AfterEach
	void tearDown() {
		memberRepository.deleteAll();
	}

	@WithCustomMember
	@DisplayName("새로운 커뮤니티 글 작성")
	@Test
	void createNewBoard1() throws Exception {
		// given
		List<CommunityCategory> savedCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();

		CommunityCategory savedCommunityCategory = savedCommunityCategoryList.getFirst();
		Long communityCategoryId = savedCommunityCategory.getId();

		CommunityRequest.CreateBoard createBoard =
			new CommunityRequest.CreateBoard(communityCategoryId, "title", "content");

		// when
		ResultActions perform = mockMvc.perform(post("/api/v1/community/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createBoard)));

		// then
		perform.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.message").value(SuccessCode.COMMUNITY_BOARD_CREATE_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.code").value(SuccessCode.COMMUNITY_BOARD_CREATE_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data.boardId").isNotEmpty());
	}

	@DisplayName("새로운 커뮤니티 글 작성은, 로그인 된 사용자만 가능합니다.")
	@Test
	void createNewBoard2() throws Exception {
		// given
		List<CommunityCategory> savedCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();

		CommunityCategory savedCommunityCategory = savedCommunityCategoryList.getFirst();
		Long communityCategoryId = savedCommunityCategory.getId();

		CommunityRequest.CreateBoard createBoard =
			new CommunityRequest.CreateBoard(communityCategoryId, "title", "content");

		// when
		ResultActions perform = mockMvc.perform(post("/api/v1/community/board")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createBoard)));

		// then
		perform.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@WithCustomMember
	@DisplayName("커뮤니티 글 목록을 조회합니다.")
	@Test
	void getBoardList1() throws Exception {
		// given

		// when
		ResultActions perform = mockMvc.perform(get("/api/v1/community/board"));

		// then
		perform.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.message").value(SuccessCode.COMMUNITY_BOARD_SEARCH_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.code").value(SuccessCode.COMMUNITY_BOARD_SEARCH_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data.content").isArray());
	}

	@DisplayName("커뮤니티 글 목록을 조회합니다. 비회원도 조회 가능합니다.")
	@Test
	void getBoardList2() throws Exception {
		// given

		// when
		ResultActions perform = mockMvc.perform(get("/api/v1/community/board"));

		// then
		perform.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.message").value(SuccessCode.COMMUNITY_BOARD_SEARCH_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.code").value(SuccessCode.COMMUNITY_BOARD_SEARCH_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data.content").isArray());
	}

	@WithCustomMember
	@DisplayName("커뮤니티 글 내용을 상세 조회합니다.")
	@Test
	void getBoardDetail1() throws Exception {
		// given
		List<Member> saveMemberList = memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList = communityDataFactory.createAndSaveCommunityCategory();
		Community saveCommunity = communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList,
				1)
			.getFirst();

		// when
		ResultActions perform = mockMvc.perform(get("/api/v1/community/board/{id}", saveCommunity.getId()));

		// then
		perform.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.message").value(SuccessCode.COMMUNITY_BOARD_DETAIL_SEARCH_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.code").value(SuccessCode.COMMUNITY_BOARD_DETAIL_SEARCH_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data.content").isNotEmpty());
	}

	@DisplayName("커뮤니티 글 내용을 상세 조회합니다. 비회원도 조회가 가능합니다.")
	@Test
	void getBoardDetail2() throws Exception {
		// given

		// when
		ResultActions perform = mockMvc.perform(get("/api/v1/community/board/{id}", 1));

		// then
		perform.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.message").value(SuccessCode.COMMUNITY_BOARD_DETAIL_SEARCH_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.code").value(SuccessCode.COMMUNITY_BOARD_DETAIL_SEARCH_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data.content").isNotEmpty());
	}

	@WithCustomMember
	@DisplayName("글 수정을 진행합니다.")
	@Test
	void modifyBoard1() throws Exception {
		// given
		List<Member> saveMemberList = memberRepository.findAll();
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		Community saveCommunity =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1).getFirst();
		CommunityRequest.ModifyBoard modifyBoard = new CommunityRequest.ModifyBoard("title", "content");

		// when
		ResultActions perform = mockMvc.perform(put("/api/v1/community/board/{id}", saveCommunity.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(modifyBoard)));

		// then
		perform.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.message").value(SuccessCode.COMMUNITY_BOARD_MODIFY_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.code").value(SuccessCode.COMMUNITY_BOARD_MODIFY_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data").isNotEmpty());
	}

	@DisplayName("글 수정을 진행합니다. 로그인 된 사용자만 이용할 수 있습니다.")
	@Test
	void modifyBoard2() throws Exception {
		// given
		CommunityRequest.ModifyBoard modifyBoard = new CommunityRequest.ModifyBoard("title", "content");

		// when
		ResultActions perform = mockMvc.perform(put("/api/v1/community/board/{id}", 1)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(modifyBoard)));

		// then
		perform.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@WithCustomMember
	@DisplayName("커뮤니티 글 삭제")
	@Test
	void deleteBoard1() throws Exception {
		// given
		List<Member> saveMemberList = memberRepository.findAll();
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		Community saveCommunity =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1).getFirst();

		// when
		ResultActions perform = mockMvc.perform(delete("/api/v1/community/board/{id}", saveCommunity.getId()));

		// then
		perform.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.message").value(SuccessCode.COMMUNITY_BOARD_DELETE_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.code").value(SuccessCode.COMMUNITY_BOARD_DELETE_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("커뮤니티 글 삭제. 로그인 된 사용자만 이용할 수 있습니다.")
	@Test
	void deleteBoard2() throws Exception {
		// given

		// when
		ResultActions perform = mockMvc.perform(delete("/api/v1/community/board/{id}", 1));

		// then
		perform.andDo(print())
			.andExpect(status().isUnauthorized());
	}
}
