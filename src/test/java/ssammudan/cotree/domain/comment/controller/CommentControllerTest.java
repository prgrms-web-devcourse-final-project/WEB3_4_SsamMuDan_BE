package ssammudan.cotree.domain.comment.controller;

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

import ssammudan.cotree.domain.comment.dto.CommentRequest;
import ssammudan.cotree.domain.comment.type.CommentCategory;
import ssammudan.cotree.global.response.SuccessCode;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.integration.security.WithCustomMember;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.domain.comment.controller
 * FileName    : CommentControllerTest
 * Author      : Baekgwa
 * Date        : 2025-04-14
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-14     Baekgwa               Initial creation
 */
@Transactional
class CommentControllerTest extends SpringBootTestSupporter {

	@AfterEach
	void tearDown() {
		memberRepository.deleteAll();
	}

	@WithCustomMember
	@DisplayName("새로운 글을 작성합니다.")
	@Test
	void postNewComment1() throws Exception {
		// given
		Member saveMember =
			memberRepository.findAll().getFirst();
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(List.of(saveMember), saveCommunityCategoryList, 1);

		Community saveCommunity = saveCommunityList.getFirst();

		CommentRequest.PostComment request = CommentRequest.PostComment
			.builder()
			.content("댓글")
			.category(CommentCategory.COMMUNITY)
			.whereId(saveCommunity.getId())
			.commentId(null)
			.build();

		// when
		ResultActions perform = mockMvc.perform(post("/api/v1/comment")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		perform.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.message").value(SuccessCode.COMMENT_CREATE_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.code").value(SuccessCode.COMMENT_CREATE_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("새로운 글을 작성합니다. 로그인 한 회원만 가능합니다.")
	@Test
	void postNewComment2() throws Exception {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);

		Community saveCommunity = saveCommunityList.getFirst();

		CommentRequest.PostComment request = CommentRequest.PostComment
			.builder()
			.content("댓글")
			.category(CommentCategory.COMMUNITY)
			.whereId(saveCommunity.getId())
			.commentId(null)
			.build();

		// when
		ResultActions perform = mockMvc.perform(post("/api/v1/comment")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		perform.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@WithCustomMember
	@DisplayName("댓글과 대댓글을 조회합니다.")
	@Test
	void getCommentList1() throws Exception {
		// given
		List<Member> saveMemberList = memberRepository.findAll();
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommunityList, 5);
		Community saveCommunity = saveCommunityList.getFirst();

		// when
		ResultActions perform = mockMvc.perform(get("/api/v1/comment")
			.param("itemId", saveCommunity.getId().toString())
			.param("category", CommentCategory.COMMUNITY.toString()));

		// then
		perform.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.message").value(SuccessCode.COMMENT_SEARCH_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.code").value(SuccessCode.COMMENT_SEARCH_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data.content").isArray());
	}

	@DisplayName("댓글과 대댓글을 조회합니다. 비회원도 조회가 가능합니다.")
	@Test
	void getCommentList2() throws Exception {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommunityList, 5);
		Community saveCommunity = saveCommunityList.getFirst();

		// when
		ResultActions perform = mockMvc.perform(get("/api/v1/comment")
			.param("itemId", saveCommunity.getId().toString())
			.param("category", CommentCategory.COMMUNITY.toString()));

		// then
		perform.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess").value(true))
			.andExpect(jsonPath("$.message").value(SuccessCode.COMMENT_SEARCH_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.code").value(SuccessCode.COMMENT_SEARCH_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data.content").isArray());
	}
}