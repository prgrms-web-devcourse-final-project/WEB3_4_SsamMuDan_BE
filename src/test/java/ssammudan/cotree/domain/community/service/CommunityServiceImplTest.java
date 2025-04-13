package ssammudan.cotree.domain.community.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.ThrowableAssert;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ssammudan.cotree.domain.community.dto.CommunityRequest;
import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.domain.community.type.SearchBoardCategory;
import ssammudan.cotree.domain.community.type.SearchBoardSort;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.domain.community.service
 * FileName    : CommunityServiceImplTest
 * Author      : Baekgwa
 * Date        : 2025-03-31
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-31     Baekgwa               Initial creation
 * 2025-04-08     Baekgwa               커뮤니티 글 작성 시, community category 입력 형식 변경. 기존 : String / 변경 : Long id
 */
@Transactional
class CommunityServiceImplTest extends SpringBootTestSupporter {

	private static final String THUMBNAIL_IMAGE_URL = "https://테스트_가상_이미지_url1.jpg";
	private static final String NOT_THUMBNAIL_IMAGE_URL = "https://테스트_가상_이미지_url2.jpg";

	@DisplayName("커뮤니티 새글 작성. 글 내용은 마크다운 형식의 데이터가 입력된다.")
	@Test
	void createNewBoard1() {
		// given
		List<CommunityCategory> savedCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Member> savedMemberList =
			memberDataFactory.createAndSaveMember(10);

		Member savedMember = savedMemberList.getFirst();
		CommunityCategory savedCommunityCategory = savedCommunityCategoryList.getFirst();
		Long communityCategoryId = savedCommunityCategory.getId();

		String newTitle = "새글 제목";
		String newContent = createNewMarkdownContent(true);
		CommunityRequest.CreateBoard createBoard =
			new CommunityRequest.CreateBoard(communityCategoryId, newTitle, newContent);

		// when
		communityService.createNewBoard(createBoard, savedMember.getId());

		// then
		List<Community> findList = communityRepository.findAll();
		assertThat(findList).hasSize(1);
		assertThat(findList.getFirst())
			.extracting("id", "member", "title", "content", "viewCount")
			.isNotNull();
		assertThat(findList.getFirst().getCommunityCategory().getName()).isEqualTo(savedCommunityCategory.getName());
		assertThat(findList.getFirst().getThumbnailImage()).isEqualTo(THUMBNAIL_IMAGE_URL);
	}

	@DisplayName("커뮤니티 새글 작성. 등록된 커뮤니티 카테고리가 아니라면, 오류를 발생한다.")
	@Test
	void createNewBoard2() {
		// given
		List<Member> savedMemberList =
			memberDataFactory.createAndSaveMember(10);
		String memberId = savedMemberList.getFirst().getId();

		String newTitle = "새글 제목";
		String newContent = createNewMarkdownContent(true);
		CommunityRequest.CreateBoard createBoard =
			new CommunityRequest.CreateBoard(0L, newTitle, newContent);

		// when // then
		assertThatThrownBy(() -> communityService.createNewBoard(createBoard, memberId))
			.isInstanceOf(GlobalException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.COMMUNITY_BOARD_CATEGORY_INVALID);
	}

	@DisplayName("커뮤니티 새글 작성. 로그인 아이디 정보가 이상하다면, 오류를 발생한다.")
	@Test
	void createNewBoard3() {
		// given
		List<CommunityCategory> savedCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		CommunityCategory savedCommunityCategory = savedCommunityCategoryList.getFirst();
		Long communityCategoryId = savedCommunityCategory.getId();

		String newTitle = "새글 제목";
		String newContent = createNewMarkdownContent(true);

		CommunityRequest.CreateBoard createBoard =
			new CommunityRequest.CreateBoard(communityCategoryId, newTitle, newContent);

		// when // then
		assertThatThrownBy(() -> communityService.createNewBoard(createBoard, "Unknown Member Id"))
			.isInstanceOf(GlobalException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.COMMUNITY_MEMBER_NOTFOUND);
	}

	@DisplayName("커뮤니티 새글 작성. 글 내용에 이미지가 없다면, 썸네일 이미지는 null 로 저장된다.")
	@Test
	void createNewBoard4() {
		// given
		List<CommunityCategory> savedCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Member> savedMemberList =
			memberDataFactory.createAndSaveMember(10);

		Member savedMember = savedMemberList.getFirst();
		CommunityCategory savedCommunityCategory = savedCommunityCategoryList.getFirst();
		Long communityCategoryId = savedCommunityCategory.getId();

		String newTitle = "새글 제목";
		String newContent = createNewMarkdownContent(false);
		CommunityRequest.CreateBoard createBoard =
			new CommunityRequest.CreateBoard(communityCategoryId, newTitle, newContent);

		// when
		communityService.createNewBoard(createBoard, savedMember.getId());

		// then
		List<Community> findList = communityRepository.findAll();
		assertThat(findList).hasSize(1);
		assertThat(findList.getFirst())
			.extracting("id", "member", "title", "content", "viewCount")
			.isNotNull();
		assertThat(findList.getFirst().getCommunityCategory().getName()).isEqualTo(savedCommunityCategory.getName());
		assertThat(findList.getFirst().getThumbnailImage()).isNull();
	}

	@DisplayName("커뮤니티 글 조회.")
	@Test
	void getBoardList1() {
		// given
		// 회원 2명
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(2);
		// 커뮤니티 카테고리 2개
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		// 커뮤니티 글 = 회원수 * 카테고리 수 * count = 2 * 2 * 2 = 8
		List<Community> saveCommuintyList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 2);
		// 댓글 수 : member * community * count = 2 * 8 * 2 = 32
		// 대댓글 수 : 댓글 수 * count = 32 * 2 = 64
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommuintyList, 2);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);

		Member saveMember = saveMemberList.getFirst();

		Pageable pageable = PageRequest.of(0, 5);
		SearchBoardSort sort = SearchBoardSort.LATEST;
		SearchBoardCategory category = SearchBoardCategory.TOTAL;

		// when
		PageResponse<CommunityResponse.BoardListDetail> boardList =
			communityService.getBoardList(pageable, sort, category, "", saveMember.getId());

		// then
		assertThat(boardList)
			.satisfies(board -> {
				assertThat(board.getPageNo()).isZero();
				assertThat(board.getPageSize()).isEqualTo(5);
				assertThat(board.getTotalElements()).isEqualTo(8);
				assertThat(board.getTotalPages()).isEqualTo(2);
				assertThat(board.isLast()).isFalse();
				assertThat(board.isFirst()).isTrue();
				assertThat(board.isHasNext()).isTrue();
				assertThat(board.isHasPrevious()).isFalse();
			});

		assertThat(boardList.getContent())
			.allSatisfy(data -> {
				assertThat(data.id()).isNotNull();
				assertThat(data.title()).isNotNull();
				assertThat(data.author()).isNotNull();
				assertThat(data.createdAt()).isNotNull();
				assertThat(data.content()).isNotNull();
				assertThat(data.commentCount()).isEqualTo(12L);
				assertThat(data.likeCount()).isEqualTo(2L);
				assertThat(data.viewCount()).isNotNull();
				assertThat(data.imageUrl()).isNull();
				assertThat(data.isLike()).isTrue();
				assertThat(data.isNew()).isTrue();
				assertThat(data.profileImage()).isNull();
			});
	}

	@DisplayName("커뮤니티 글 조회. 글 내용들은, 마크다운 형식 중 이미지와 불필요한 공백은 삭제 처리된다.")
	@Test
	void getBoardList2() {
		// given
		// 회원 2명
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(2);
		// 커뮤니티 카테고리 2개
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		// 커뮤니티 글 = 회원수 * 카테고리 수 * count = 2 * 2 * 2 = 8
		Community newCommunity = Community.createNewCommunityBoard(
			saveCommunityCategoryList.getFirst(),
			saveMemberList.getFirst(),
			"title",
			createNewMarkdownContent(true),
			THUMBNAIL_IMAGE_URL
		);
		communityRepository.save(newCommunity);

		Member saveMember = saveMemberList.getFirst();

		Pageable pageable = PageRequest.of(0, 5);
		SearchBoardSort sort = SearchBoardSort.LATEST;
		SearchBoardCategory category = SearchBoardCategory.TOTAL;

		// when
		PageResponse<CommunityResponse.BoardListDetail> boardList =
			communityService.getBoardList(pageable, sort, category, "", saveMember.getId());

		// then
		assertThat(boardList.getContent()).isNotEmpty();
		assertThat(boardList.getContent())
			.allSatisfy(data -> assertThat(data.content()).doesNotMatch(".*!\\[[^\\]]*]\\([^)]*\\).*"));
	}

	@DisplayName("커뮤니티 글 상세 조회.")
	@Test
	void getBoardDetail1() {
		// given
		List<Member> saveMemberList = memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList = communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommuintyList = communityDataFactory.createAndSaveCommunity(saveMemberList,
			saveCommunityCategoryList, 1);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);

		Member saveMember = saveMemberList.getFirst();
		Community saveCommunity = saveCommuintyList.getFirst();

		// stubbing
		BDDMockito.willDoNothing().given(viewCountStore).incrementViewCount(any(), any());

		// when
		CommunityResponse.BoardDetail boardDetail =
			communityService.getBoardDetail(saveCommunity.getId(), saveMember.getId());

		// then
		assertThat(boardDetail)
			.extracting("title", "author", "createdAt", "content", "likeCount")
			.isNotNull();
		assertThat(boardDetail.likeCount()).isEqualTo(1L);
		assertThat(boardDetail.isLike()).isTrue();
		assertThat(boardDetail.isOwner()).isTrue();
	}

	@DisplayName("커뮤니티 글 상세 조회. 없는 글을 조회하면 오류메세지를 서빙합니다.")
	@Test
	void getBoardDetail2() {
		// given

		// when // then
		ThrowableAssert.ThrowingCallable callable = () ->
			communityService.getBoardDetail(1L, UUID.randomUUID().toString());

		assertThatThrownBy(callable)
			.isInstanceOf(GlobalException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.COMMUNITY_BOARD_NOTFOUND);

	}

	@DisplayName("커뮤니티 글을 수정합니다.")
	@Test
	void modifyBoard1() {
		// given
		List<Member> saveMemberList = memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList = communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommuintyList = communityDataFactory.createAndSaveCommunity(saveMemberList,
			saveCommunityCategoryList, 1);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);

		Community saveCommunity = saveCommuintyList.getFirst();
		Member saveMember = saveMemberList.getFirst();

		String newTitle = "변경된 title";
		String newContent = "변경된 content";
		CommunityRequest.ModifyBoard modifyBoard = new CommunityRequest.ModifyBoard(newTitle, newContent);

		// when
		communityService.modifyBoard(saveCommunity.getId(), modifyBoard, saveMember.getId());
		em.flush();
		em.clear();

		// then
		Optional<Community> findOpCommunity = communityRepository.findById(saveCommunity.getId());
		assertThat(findOpCommunity).isPresent();
		Community findCommunity = findOpCommunity.get();
		assertThat(findCommunity)
			.extracting("title", "content")
			.containsExactly(newTitle, newContent);
	}

	@DisplayName("커뮤니티 글을 수정합니다. 해당 글의 작성자만 수정할 수 있습니다.")
	@Test
	void modifyBoard2() {
		// given
		List<Member> saveMemberList = memberDataFactory.createAndSaveMember(2);
		List<CommunityCategory> saveCommunityCategoryList = communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommuintyList = communityDataFactory.createAndSaveCommunity(saveMemberList,
			saveCommunityCategoryList, 1);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);

		Community saveCommunity = saveCommuintyList.getFirst();
		Member saveMember = saveMemberList.getLast();

		String newTitle = "변경된 title";
		String newContent = "변경된 content";
		CommunityRequest.ModifyBoard modifyBoard = new CommunityRequest.ModifyBoard(newTitle, newContent);

		// when
		ThrowableAssert.ThrowingCallable callable =
			() -> communityService.modifyBoard(saveCommunity.getId(), modifyBoard, saveMember.getId());

		// then
		assertThatThrownBy(callable)
			.isInstanceOf(GlobalException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.COMMUNITY_BOARD_OPERATION_FAIL_NOT_AUTHOR);
	}

	@DisplayName("커뮤니티 글을 수정합니다. 없는 글을 수정하려고 하면, 오류 메세지를 발생합니다.")
	@Test
	void modifyBoard3() {
		// given
		String newTitle = "변경된 title";
		String newContent = "변경된 content";
		CommunityRequest.ModifyBoard modifyBoard = new CommunityRequest.ModifyBoard(newTitle, newContent);

		// when
		ThrowableAssert.ThrowingCallable callable =
			() -> communityService.modifyBoard(1L, modifyBoard, UUID.randomUUID().toString());

		// then
		assertThatThrownBy(callable)
			.isInstanceOf(GlobalException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.COMMUNITY_BOARD_NOTFOUND);
	}

	@DisplayName("커뮤니티 글을 삭제합니다.")
	@Test
	void deleteBoard1() {
		// given
		List<Member> saveMemberList = memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList = communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommuintyList = communityDataFactory.createAndSaveCommunity(saveMemberList,
			saveCommunityCategoryList, 1);
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommuintyList, 2);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);

		Community saveCommunity = saveCommuintyList.getFirst();
		Member saveMember = saveMemberList.getFirst();

		// when
		communityService.deleteBoard(saveCommunity.getId(), saveMember.getId());

		// then
		boolean result = communityRepository.existsById(saveCommunity.getId());
		assertThat(result).isFalse();
	}

	@DisplayName("커뮤니티 글을 삭제합니다. 없는 글을 수정하려고 하면, 오류 메세지를 발생합니다.")
	@Test
	void deleteBoard2() {
		// given
		List<Member> saveMemberList = memberDataFactory.createAndSaveMember(2);
		List<CommunityCategory> saveCommunityCategoryList = communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommuintyList = communityDataFactory.createAndSaveCommunity(saveMemberList,
			saveCommunityCategoryList, 1);
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommuintyList, 2);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);

		Community saveCommunity = saveCommuintyList.getFirst();
		Member saveMember = saveMemberList.getLast();

		// when
		ThrowableAssert.ThrowingCallable callable =
			() -> communityService.deleteBoard(saveCommunity.getId(), saveMember.getId());

		// then
		assertThatThrownBy(callable)
			.isInstanceOf(GlobalException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.COMMUNITY_BOARD_OPERATION_FAIL_NOT_AUTHOR);
	}

	@DisplayName("커뮤니티 글을 삭제합니다. 해당 글의 작성자만 삭제할 수 있습니다.")
	@Test
	void deleteBoard3() {
		// given

		// when
		ThrowableAssert.ThrowingCallable callable =
			() -> communityService.deleteBoard(1L, UUID.randomUUID().toString());

		// then
		assertThatThrownBy(callable)
			.isInstanceOf(GlobalException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.COMMUNITY_BOARD_NOTFOUND);
	}

	@DisplayName("내가 좋아요한 커뮤니티 글을 조회합니다.")
	@Test
	void getBoardLikeList1() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 10);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommunityList);

		Member saveMember = saveMemberList.getFirst();
		Pageable pageable = PageRequest.of(0, 16);

		// when
		PageResponse<CommunityResponse.BoardLikeListDetail> content =
			communityService.getBoardLikeList(pageable, saveMember.getId());

		// then
		assertThat(content)
			.satisfies(data -> {
				assertThat(data.getPageNo()).isZero();
				assertThat(data.getPageSize()).isEqualTo(16);
				assertThat(data.getTotalElements()).isEqualTo(20);
				assertThat(data.getTotalPages()).isEqualTo(2);
				assertThat(data.isLast()).isFalse();
				assertThat(data.isFirst()).isTrue();
				assertThat(data.isHasNext()).isTrue();
				assertThat(data.isHasPrevious()).isFalse();
			});

		assertThat(content.getContent())
			.hasSize(16);
		assertThat(content.getContent())
			.allSatisfy(data -> {
				assertThat(data.id()).isNotNull();
				assertThat(data.title()).isNotNull();
				assertThat(data.author()).isNotNull();
				assertThat(data.createdAt()).isNotNull();
				assertThat(data.content()).isNotNull();
			});
	}

	private @NotNull String createNewMarkdownContent(final boolean isImageExist) {
		String content = """
			# 새글 제목
			이것은 **마크다운(Markdown)** 형식으로 작성된 글입니다.
			## 주요 내용
			- 첫 번째 리스트 아이템
			- 두 번째 리스트 아이템
			- 세 번째 리스트 아이템
			### 코드 블록
			```java
			public static void main(String[] args) {
			    System.out.println("Hello, Markdown!");
			}
			```
			""";

		if (isImageExist) {
			content =
				content + markDownImageBuilder(THUMBNAIL_IMAGE_URL) + markDownImageBuilder(NOT_THUMBNAIL_IMAGE_URL);
		}
		return content;
	}

	private @NotNull String markDownImageBuilder(String imageUrl) {
		return String.format("%n![이미지](%s)%n", imageUrl);
	}
}
