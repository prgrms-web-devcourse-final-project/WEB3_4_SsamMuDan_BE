package ssammudan.cotree.model.common.comment.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ssammudan.cotree.domain.comment.type.CommentCategory;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.common.comment.entity.Comment;
import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

/**
 * PackageName : ssammudan.cotree.model.common.comment.repository
 * FileName    : CommentRepositoryTest
 * Author      : Baekgwa
 * Date        : 2025-04-13
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-13     Baekgwa               Initial creation
 */
@Transactional
class CommentRepositoryTest extends SpringBootTestSupporter {

	@DisplayName("커뮤니티 글의 대댓글을 삭제합니다.")
	@Test
	void deleteCommunityChildComments() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		// 커뮤니티 글 = 2개
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		// 댓글은 총 4개, 대댓글 8개
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommunityList, 2);
		Community saveCommunity = saveCommunityList.getFirst();

		// when
		// 대댓글 4개 삭제되어, 총 12개 중, 8개 남음
		commentRepository.deleteCommunityChildComments(saveCommunity.getId());

		// then
		List<Comment> findCommentList = commentRepository.findAll();
		assertThat(findCommentList).hasSize(8);
	}

	@DisplayName("커뮤니티 글의 댓글을 삭제합니다.")
	@Test
	void deleteCommunityParentComments1() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		// 커뮤니티 글 = 2개
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		// 댓글은 총 4개, 대댓글 8개
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommunityList, 2);
		Community saveCommunity = saveCommunityList.getFirst();
		commentRepository.deleteCommunityChildComments(saveCommunity.getId());

		// when
		// 대댓글 4개, 댓글 2개 삭제되어, 총 12개 중, 6개 남음
		commentRepository.deleteCommunityParentComments(saveCommunity.getId());

		// then
		List<Comment> findCommentList = commentRepository.findAll();
		assertThat(findCommentList).hasSize(6);
	}

	@DisplayName("커뮤니티 글의 댓글을 삭제합니다. 대댓글이 있는 댓글은, 먼저 대댓글을 삭제해야 합니다.")
	@Test
	void deleteCommunityParentComments2() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		// 커뮤니티 글 = 2개
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		// 댓글은 총 4개, 대댓글 8개
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommunityList, 2);
		Community saveCommunity = saveCommunityList.getFirst();

		// when // then
		ThrowableAssert.ThrowingCallable callable =
			() -> commentRepository.deleteCommunityParentComments(saveCommunity.getId());
		assertThatThrownBy(callable)
			.isInstanceOf(DataIntegrityViolationException.class);
	}

	@DisplayName("특정 글(community, resume)에 대한 댓글을 페이지네이션 조회 합니다. [0 page, 5size, community category")
	@Test
	void findCommentListByPaging1() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		// 커뮤니티 글 = 2개
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		// 하나의 게시글에 댓글 10개, 대댓글 100개
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommunityList, 10);

		Community saveCommunity = saveCommunityList.getFirst();
		Member saveMember = saveMemberList.getFirst();
		Pageable pageable = PageRequest.of(0, 5);

		// when
		List<CommentInfoProjection> content =
			commentRepository.findCommentListByPaging(
				pageable, saveMember.getId(),
				CommentCategory.COMMUNITY,
				saveCommunity.getId());

		// then
		assertThat(content)
			.hasSize(55)
			.allSatisfy(data -> {
				assertThat(data.getId()).isNotNull();
				assertThat(data.getAuthor()).isNotNull();
				assertThat(data.getCreatedAt()).isNotNull();
				assertThat(data.getContent()).isNotNull();
				assertThat(data.getIsAuthor()).isTrue();
			});
	}

	@DisplayName("특정 글(community, resume)에 대한 댓글을 페이지네이션 조회 합니다. [0 page, 5size, RESUME category")
	@Test
	void findCommentListByPaging2() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<TechStack> saveTechStackList =
			techStackDataFactory.createAndSaveTechStack();
		List<DevelopmentPosition> saveDevPosList =
			developmentPositionDataFactory.createAndSaveDevelopmentPosition();
		List<Resume> saveResumeList =
			resumeProjectTestDataFactory.createAndSaveResume(saveMemberList, saveTechStackList, saveDevPosList);
		// 하나의 게시글에 댓글 10개, 대댓글 100개
		commentDataFactory.createAndSaveResumeComment(saveMemberList, saveResumeList, 10);

		Resume saveResume = saveResumeList.getFirst();
		Member saveMember = saveMemberList.getFirst();
		Pageable pageable = PageRequest.of(0, 5);

		// when
		List<CommentInfoProjection> content =
			commentRepository.findCommentListByPaging(
				pageable, saveMember.getId(),
				CommentCategory.RESUME,
				saveResume.getId());

		// then
		assertThat(content)
			.hasSize(55)
			.allSatisfy(data -> {
				assertThat(data.getId()).isNotNull();
				assertThat(data.getAuthor()).isNotNull();
				assertThat(data.getCreatedAt()).isNotNull();
				assertThat(data.getContent()).isNotNull();
				assertThat(data.getIsAuthor()).isTrue();
			});
	}

	@DisplayName("특정 글(community, resume)에 대한 댓글을 페이지네이션 조회 합니다. [0 page, 5size, RESUME category], 회원 정보가 없으면, isAuthor 는 false 가 반환 됩니다.")
	@Test
	void findCommentListByPaging3() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<TechStack> saveTechStackList =
			techStackDataFactory.createAndSaveTechStack();
		List<DevelopmentPosition> saveDevPosList =
			developmentPositionDataFactory.createAndSaveDevelopmentPosition();
		List<Resume> saveResumeList =
			resumeProjectTestDataFactory.createAndSaveResume(saveMemberList, saveTechStackList, saveDevPosList);
		// 하나의 게시글에 댓글 10개, 대댓글 100개
		commentDataFactory.createAndSaveResumeComment(saveMemberList, saveResumeList, 10);

		Resume saveResume = saveResumeList.getFirst();
		Pageable pageable = PageRequest.of(0, 5);

		// when
		List<CommentInfoProjection> content =
			commentRepository.findCommentListByPaging(
				pageable,
				null,
				CommentCategory.RESUME,
				saveResume.getId());

		// then
		assertThat(content)
			.hasSize(55)
			.allSatisfy(data -> {
				assertThat(data.getId()).isNotNull();
				assertThat(data.getAuthor()).isNotNull();
				assertThat(data.getCreatedAt()).isNotNull();
				assertThat(data.getContent()).isNotNull();
				assertThat(data.getIsAuthor()).isFalse();
			});
	}

	@DisplayName("특정 글(community, resume)에 대한 댓글을 페이지네이션 조회 합니다. [0 page, 5size, RESUME category], 댓글이 없다면 빈 리스트가 반환됩니다.")
	@Test
	void findCommentListByPaging4() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<TechStack> saveTechStackList =
			techStackDataFactory.createAndSaveTechStack();
		List<DevelopmentPosition> saveDevPosList =
			developmentPositionDataFactory.createAndSaveDevelopmentPosition();
		List<Resume> saveResumeList =
			resumeProjectTestDataFactory.createAndSaveResume(saveMemberList, saveTechStackList, saveDevPosList);

		Resume saveResume = saveResumeList.getFirst();
		Pageable pageable = PageRequest.of(0, 5);

		// when
		List<CommentInfoProjection> content =
			commentRepository.findCommentListByPaging(
				pageable,
				null,
				CommentCategory.RESUME,
				saveResume.getId());

		// then
		assertThat(content)
			.isEmpty();
	}

	@DisplayName("해당 커뮤니티의 댓글의 총 개수를 조회한다.")
	@Test
	void findCommentListCounts1() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		// 커뮤니티 글 = 2개
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		// 하나의 게시글에 댓글 10개, 대댓글 100개
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommunityList, 10);

		Community saveCommunity = saveCommunityList.getFirst();

		// when
		Long count = commentRepository.findCommentListCounts(CommentCategory.COMMUNITY,
			saveCommunity.getId());

		// then
		assertThat(count).isEqualTo(10);
	}

	@DisplayName("해당 이력서의 댓글의 총 개수를 조회한다.")
	@Test
	void findCommentListCounts2() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<TechStack> saveTechStackList =
			techStackDataFactory.createAndSaveTechStack();
		List<DevelopmentPosition> saveDevPosList =
			developmentPositionDataFactory.createAndSaveDevelopmentPosition();
		List<Resume> saveResumeList =
			resumeProjectTestDataFactory.createAndSaveResume(saveMemberList, saveTechStackList, saveDevPosList);
		// 하나의 게시글에 댓글 10개, 대댓글 100개
		commentDataFactory.createAndSaveResumeComment(saveMemberList, saveResumeList, 10);

		Resume saveResume = saveResumeList.getFirst();

		// when
		Long count = commentRepository.findCommentListCounts(CommentCategory.RESUME, saveResume.getId());

		// then
		assertThat(count).isEqualTo(10);
	}
}