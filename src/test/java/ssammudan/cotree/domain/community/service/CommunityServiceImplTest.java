package ssammudan.cotree.domain.community.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import ssammudan.cotree.domain.community.dto.CommunityRequest;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
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
 */
@Transactional
class CommunityServiceImplTest extends SpringBootTestSupporter {

	@DisplayName("커뮤니티 새글 작성. 글 내용은 마크다운 형식의 데이터가 입력된다.")
	@Test
	void createNewBoard1() {
		// given
		List<CommunityCategory> savedCommunityCategoryList = communityFactory.createAndSaveCommunityCategory(10);
		CommunityCategory savedCommunityCategory = savedCommunityCategoryList.getFirst();
		String communityCategoryName = savedCommunityCategory.getName();

		String newTitle = "새글 제목";
		String newContent = """
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

		CommunityRequest.CreateBoard createBoard =
				new CommunityRequest.CreateBoard(communityCategoryName, newTitle, newContent);

		List<Member> savedMemberList = memberDataFactory.createAndSaveMember(10);
		Member savedMember = savedMemberList.getFirst();
		em.flush();
		em.clear();

		// when
		communityService.createNewBoard(createBoard, savedMember.getId());

		// then
		List<Community> findList = communityRepository.findAll();
		assertThat(findList).hasSize(1);
		Community findData = findList.getFirst();
		assertThat(findData)
				.extracting("title", "content", "viewCount")
				.containsExactly(newTitle, newContent, 0);
	}

	@DisplayName("커뮤니티 새글 작성. 등록된 커뮤니티 카테고리가 아니라면, 오류를 발생한다.")
	@Test
	void createNewBoard2() {
		// given
		String newTitle = "새글 제목";
		String newContent = """
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

		CommunityRequest.CreateBoard createBoard =
				new CommunityRequest.CreateBoard("Unknown Category", newTitle, newContent);

		List<Member> savedMemberList = memberDataFactory.createAndSaveMember(10);
		Member savedMember = savedMemberList.getFirst();
		em.flush();
		em.clear();

		// when // then
		assertThatThrownBy(() -> communityService.createNewBoard(createBoard, savedMember.getId()))
				.isInstanceOf(GlobalException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMUNITY_BOARD_CATEGORY_INVALID);
	}

	@DisplayName("커뮤니티 새글 작성. 로그인 아이디 정보가 이상하다면, 오류를 발생한다.")
	@Test
	void createNewBoard3() {
		// given
		List<CommunityCategory> savedCommunityCategoryList = communityFactory.createAndSaveCommunityCategory(10);
		CommunityCategory savedCommunityCategory = savedCommunityCategoryList.getFirst();
		String communityCategoryName = savedCommunityCategory.getName();

		String newTitle = "새글 제목";
		String newContent = """
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

		CommunityRequest.CreateBoard createBoard =
				new CommunityRequest.CreateBoard(communityCategoryName, newTitle, newContent);

		em.flush();
		em.clear();

		// when // then
		assertThatThrownBy(() -> communityService.createNewBoard(createBoard, "Unknown Member Id"))
				.isInstanceOf(GlobalException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMUNITY_MEMBER_NOTFOUND);
	}
}
