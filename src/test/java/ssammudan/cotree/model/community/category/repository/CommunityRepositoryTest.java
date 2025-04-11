package ssammudan.cotree.model.community.category.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.domain.community.type.SearchBoardCategory;
import ssammudan.cotree.domain.community.type.SearchBoardSort;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.model.community.category.repository
 * FileName    : CommunityRepositoryTest
 * Author      : Baekgwa
 * Date        : 2025-03-30
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-30     Baekgwa               Initial creation
 */
@Transactional
class CommunityRepositoryTest extends SpringBootTestSupporter {

	@DisplayName("로그인 Uid 와 글 Id 로 회원의 글인지 확인합니다.")
	@Test
	void isBoardAuthor1() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommuintyList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);

		Member saveMember = saveMemberList.getFirst();
		Community saveCommunity = saveCommuintyList.getFirst();

		// when
		boolean result = communityRepository.isBoardAuthor(saveMember.getId(), saveCommunity.getId());

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("로그인 Uid 와 글 Id 로 회원의 글인지 확인합니다.")
	@Test
	void isBoardAuthor2() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(2);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommuintyList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);

		Member saveMember = saveMemberList.getFirst();
		Community saveCommunity = saveCommuintyList.getLast();

		// when
		boolean result = communityRepository.isBoardAuthor(saveMember.getId(), saveCommunity.getId());

		// then
		assertThat(result).isFalse();
	}

	@DisplayName("board id 와 로그인 회원 uid 로 글 상세 내용을 조회 합니다. 자신이 작성한 글이라면, 자신의 글이라고 표시된다")
	@Test
	void findBoard1() {
		// given
		List<Member> saveMemberList = memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList = communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommuintyList = communityDataFactory.createAndSaveCommunity(saveMemberList,
			saveCommunityCategoryList, 1);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);

		Member saveMember = saveMemberList.getFirst();
		Community saveCommunity = saveCommuintyList.getFirst();

		// when
		CommunityResponse.BoardDetail content = communityRepository.findBoard(saveCommunity.getId(),
			saveMember.getId());

		// then
		assertThat(content)
			.extracting("title", "author", "createdAt", "content", "likeCount")
			.isNotNull();
		assertThat(content.likeCount()).isEqualTo(1L);
		assertThat(content.isLike()).isTrue();
		assertThat(content.isOwner()).isTrue();
	}

	@DisplayName("board id 와 로그인 회원 uid 로 글 상세 내용을 조회 합니다. 자신이 작성한 글이 아니면 따로 표시된다")
	@Test
	void findBoard2() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(2);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommuintyList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);

		// 첫번째 사용자
		Member saveMember = saveMemberList.getFirst();
		// 두번째 사용자의 글
		Community saveCommunity = saveCommuintyList.getLast();

		// when
		CommunityResponse.BoardDetail content =
			communityRepository.findBoard(saveCommunity.getId(), saveMember.getId());

		// then
		assertThat(content)
			.extracting("title", "author", "createdAt", "content", "likeCount")
			.isNotNull();
		assertThat(content.likeCount()).isEqualTo(2L);
		assertThat(content.isLike()).isTrue();
		assertThat(content.isOwner()).isFalse();
	}

	@DisplayName("글 내용을 상세 조회한다. 회원 id 가 없다면, 좋아요 유무와 글 작성 유무가 false 로 반환된다.")
	@Test
	void findBoard3() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommuintyList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		likeDataFactory.createAndSaveCommunityLike(saveMemberList, saveCommuintyList);
		Community saveCommunity = saveCommuintyList.getFirst();

		// when
		CommunityResponse.BoardDetail content =
			communityRepository.findBoard(saveCommunity.getId(), "NotExistMemberUid");

		// then
		assertThat(content)
			.extracting("title", "author", "createdAt", "content", "likeCount")
			.isNotNull();
		assertThat(content.likeCount()).isEqualTo(1L);
		assertThat(content.isLike()).isFalse();
		assertThat(content.isOwner()).isFalse();
	}

	@DisplayName("글 전체 내용을 페이지네이션 조회 한다. 조건 : [5개씩, 0번페이지, 전체 카테고리, 최신순, 키워드 없음, 로그인 상태]")
	@Test
	void findBoardList1() {
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
		Page<CommunityResponse.BoardListDetail> content =
			communityRepository.findBoardList(pageable, sort, category, "", saveMember.getId());

		// then
		assertThat(content).asInstanceOf(InstanceOfAssertFactories.type(Page.class))
			.satisfies(page -> {
				assertThat(page.getTotalElements()).isEqualTo(8L);
				assertThat(page.getTotalPages()).isEqualTo(2);
				assertThat(page.getNumber()).isZero();
				assertThat(page.getSize()).isEqualTo(5);
				assertThat(page.getNumberOfElements()).isEqualTo(5);
				assertThat(page.isFirst()).isTrue();
				assertThat(page.isLast()).isFalse();
				assertThat(page.hasNext()).isTrue();
				assertThat(page.hasPrevious()).isFalse();
			});

		assertThat(content.getContent())
			.hasSize(5);
		assertThat(content.getContent())
			.allSatisfy(data -> {
				assertThat(data.id()).isNotNull();
				assertThat(data.title()).isNotNull();
				assertThat(data.author()).isNotNull();
				assertThat(data.createdAt()).isNotNull();
				assertThat(data.content()).isNotNull();
				assertThat(data.commentCount()).isEqualTo(12L);
				assertThat(data.likeCount()).isEqualTo(2L);
				assertThat(data.viewCount()).isNotNull();
				assertThat(data.isLike()).isTrue();
				assertThat(data.isNew()).isTrue();
			});
	}

	@DisplayName("글 전체 내용을 페이지네이션 조회 한다. 조건 : [5개씩, 0번페이지, 1번 카테고리, 최신순, 키워드 없음, 로그인 상태]")
	@Test
	void findBoardList2() {
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
		SearchBoardCategory category = SearchBoardCategory.CODE_REVIEW;

		// when
		Page<CommunityResponse.BoardListDetail> content =
			communityRepository.findBoardList(pageable, sort, category, "", saveMember.getId());

		// then
		assertThat(content).asInstanceOf(InstanceOfAssertFactories.type(Page.class))
			.satisfies(page -> {
				assertThat(page.getTotalElements()).isEqualTo(4L);
				assertThat(page.getTotalPages()).isEqualTo(1);
				assertThat(page.getNumber()).isZero();
				assertThat(page.getSize()).isEqualTo(5);
				assertThat(page.getNumberOfElements()).isEqualTo(4);
				assertThat(page.isFirst()).isTrue();
				assertThat(page.isLast()).isTrue();
				assertThat(page.hasNext()).isFalse();
				assertThat(page.hasPrevious()).isFalse();
			});

		assertThat(content.getContent())
			.hasSize(4);
		assertThat(content.getContent())
			.allSatisfy(data -> {
				assertThat(data.id()).isNotNull();
				assertThat(data.title()).isNotNull();
				assertThat(data.author()).isNotNull();
				assertThat(data.createdAt()).isNotNull();
				assertThat(data.content()).isNotNull();
				assertThat(data.commentCount()).isEqualTo(12L);
				assertThat(data.likeCount()).isEqualTo(2L);
				assertThat(data.viewCount()).isNotNull();
				assertThat(data.isLike()).isTrue();
				assertThat(data.isNew()).isTrue();
			});
	}

	@DisplayName("글 전체 내용을 페이지네이션 조회 한다. 조건 : [5개씩, 0번페이지, 1번 카테고리, 최신순, 키워드 없음, 비 로그인 상태]")
	@Test
	void findBoardList3() {
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

		Pageable pageable = PageRequest.of(0, 5);
		SearchBoardSort sort = SearchBoardSort.LATEST;
		SearchBoardCategory category = SearchBoardCategory.CODE_REVIEW;

		// when
		Page<CommunityResponse.BoardListDetail> content =
			communityRepository.findBoardList(pageable, sort, category, "", null);

		// then
		assertThat(content).asInstanceOf(InstanceOfAssertFactories.type(Page.class))
			.satisfies(page -> {
				assertThat(page.getTotalElements()).isEqualTo(4L);
				assertThat(page.getTotalPages()).isEqualTo(1);
				assertThat(page.getNumber()).isZero();
				assertThat(page.getSize()).isEqualTo(5);
				assertThat(page.getNumberOfElements()).isEqualTo(4);
				assertThat(page.isFirst()).isTrue();
				assertThat(page.isLast()).isTrue();
				assertThat(page.hasNext()).isFalse();
				assertThat(page.hasPrevious()).isFalse();
			});

		assertThat(content.getContent())
			.hasSize(4);
		assertThat(content.getContent())
			.allSatisfy(data -> {
				assertThat(data.id()).isNotNull();
				assertThat(data.title()).isNotNull();
				assertThat(data.author()).isNotNull();
				assertThat(data.createdAt()).isNotNull();
				assertThat(data.content()).isNotNull();
				assertThat(data.commentCount()).isEqualTo(12L);
				assertThat(data.likeCount()).isEqualTo(2L);
				assertThat(data.viewCount()).isNotNull();
				assertThat(data.isLike()).isFalse();
				assertThat(data.isNew()).isTrue();
			});
	}

	@DisplayName("글 전체 내용을 페이지네이션 조회 한다. 조건 : [5개씩, 0번페이지, 1번 카테고리, 최신순, 키워드 : TEST, 비 로그인 상태]")
	@Test
	void findBoardList4() {
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

		Pageable pageable = PageRequest.of(0, 5);
		SearchBoardSort sort = SearchBoardSort.LATEST;
		SearchBoardCategory category = SearchBoardCategory.CODE_REVIEW;

		// when
		Page<CommunityResponse.BoardListDetail> content =
			communityRepository.findBoardList(pageable, sort, category, "TEST", null);

		// then
		assertThat(content).asInstanceOf(InstanceOfAssertFactories.type(Page.class))
			.satisfies(page -> {
				assertThat(page.getTotalElements()).isZero();
				assertThat(page.getTotalPages()).isZero();
				assertThat(page.getNumber()).isZero();
				assertThat(page.getSize()).isEqualTo(5);
				assertThat(page.getNumberOfElements()).isZero();
				assertThat(page.isFirst()).isTrue();
				assertThat(page.isLast()).isTrue();
				assertThat(page.hasNext()).isFalse();
				assertThat(page.hasPrevious()).isFalse();
			});

		assertThat(content.getContent())
			.isEmpty();
	}
}
