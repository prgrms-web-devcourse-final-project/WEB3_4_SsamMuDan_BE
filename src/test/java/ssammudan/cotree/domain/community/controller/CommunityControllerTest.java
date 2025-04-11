// package ssammudan.cotree.domain.community.controller;
//
// import java.util.List;
//
// import org.jetbrains.annotations.NotNull;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.transaction.annotation.Transactional;
//
// import ssammudan.cotree.domain.community.dto.CommunityRequest;
// import ssammudan.cotree.integration.SpringBootTestSupporter;
// import ssammudan.cotree.integration.security.WithCustomUser;
// import ssammudan.cotree.model.community.category.entity.CommunityCategory;
//
// /**
//  * PackageName : ssammudan.cotree.domain.community.controller
//  * FileName    : CommunityControllerTest
//  * Author      : Baekgwa
//  * Date        : 2025-03-28
//  * Description : Community Domain Controller Layer Test
//  * =====================================================================================================================
//  * DATE          AUTHOR               NOTE
//  * ---------------------------------------------------------------------------------------------------------------------
//  * 2025-03-28     Baekgwa               Initial creation
//  */
// @Transactional
// class CommunityControllerTest extends SpringBootTestSupporter {
//
// 	private static final String THUMBNAIL_IMAGE_URL = "https://테스트_가상_이미지_url1.jpg";
// 	private static final String NOT_THUMBNAIL_IMAGE_URL = "https://테스트_가상_이미지_url2.jpg";
//
// 	@WithCustomUser
// 	@DisplayName("새로운 커뮤니티 글 작성")
// 	@Test
// 	void createNewBoard() throws Exception {
// 		// given
// 		List<CommunityCategory> savedCommunityCategoryList =
// 			communityDataFactory.createAndSaveCommunityCategory();
//
// 		CommunityCategory savedCommunityCategory = savedCommunityCategoryList.getFirst();
// 		Long communityCategoryId = savedCommunityCategory.getId();
//
// 		String newTitle = "새글 제목";
// 		String newContent = createNewMarkdownContent(true);
// 		CommunityRequest.CreateBoard createBoard =
// 			new CommunityRequest.CreateBoard(communityCategoryId, newTitle, newContent);
//
// 		// when
// 		ResultActions perform = mockMvc.perform(post("/api/v1/community/board")
// 			.contentType(MediaType.APPLICATION_JSON)
// 			.content(objectMapper.writeValueAsString(createBoard)));
//
// 		// then
// 		perform.andDo(print())
// 			.andExpect(status().isCreated())
// 			.andExpect(jsonPath("$.isSuccess").value(true))
// 			.andExpect(jsonPath("$.message").value(SuccessCode.COMMUNITY_BOARD_CREATE_SUCCESS.getMessage()))
// 			.andExpect(jsonPath("$.code").value(SuccessCode.COMMUNITY_BOARD_CREATE_SUCCESS.getCode()))
// 			.andExpect(jsonPath("$.data.boardId").isNotEmpty());
// 	}
//
// 	private @NotNull String createNewMarkdownContent(final boolean isImageExist) {
// 		String content = """
// 			# 새글 제목
// 			이것은 **마크다운(Markdown)** 형식으로 작성된 글입니다.
// 			## 주요 내용
// 			- 첫 번째 리스트 아이템
// 			- 두 번째 리스트 아이템
// 			- 세 번째 리스트 아이템
// 			### 코드 블록
// 			```java
// 			public static void main(String[] args) {
// 			    System.out.println("Hello, Markdown!");
// 			}
// 			```
// 			""";
//
// 		if (isImageExist) {
// 			content =
// 				content + markDownImageBuilder(THUMBNAIL_IMAGE_URL) + markDownImageBuilder(NOT_THUMBNAIL_IMAGE_URL);
// 		}
// 		return content;
// 	}
//
// 	private @NotNull String markDownImageBuilder(String imageUrl) {
// 		return String.format("%n![이미지](%s)%n", imageUrl);
// 	}
// }
