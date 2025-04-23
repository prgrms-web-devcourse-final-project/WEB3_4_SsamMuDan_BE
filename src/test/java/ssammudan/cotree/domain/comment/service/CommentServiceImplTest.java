package ssammudan.cotree.domain.comment.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ssammudan.cotree.domain.comment.dto.CommentRequest;
import ssammudan.cotree.domain.comment.dto.CommentResponse;
import ssammudan.cotree.domain.comment.type.CommentCategory;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.common.comment.entity.Comment;
import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

/**
 * PackageName : ssammudan.cotree.domain.comment.service
 * FileName    : CommentServiceImplTest
 * Author      : Baekgwa
 * Date        : 2025-04-14
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-14     Baekgwa               Initial creation
 */
@Transactional
class CommentServiceImplTest extends SpringBootTestSupporter {

	@DisplayName("커뮤니티에 새로운 댓글 작성 합니다.")
	@Test
	void postNewComment1() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);

		Community saveCommunity = saveCommunityList.getFirst();
		Member saveMember = saveMemberList.getFirst();

		CommentRequest.PostComment postComment = CommentRequest.PostComment
			.builder()
			.content("댓글")
			.category(CommentCategory.COMMUNITY)
			.whereId(saveCommunity.getId())
			.commentId(null)
			.build();

		// when
		commentService.postNewComment(postComment, saveMember.getId());

		// then
		Comment findComment = commentRepository.findAll().getFirst();
		assertThat(findComment.getId()).isNotNull();
		assertThat(findComment.getParentComment()).isNull();
		assertThat(findComment.getAuthor()).isNotNull();
		assertThat(findComment.getCommunity()).isNotNull();
		assertThat(findComment.getResume()).isNull();
		assertThat(findComment.getContent()).isEqualTo("댓글");
	}

	@DisplayName("이력서에 새로운 댓글 작성 합니다.")
	@Test
	void postNewComment2() {
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
		Member saveMember = saveMemberList.getFirst();

		CommentRequest.PostComment postComment = CommentRequest.PostComment
			.builder()
			.content("댓글")
			.category(CommentCategory.RESUME)
			.whereId(saveResume.getId())
			.commentId(null)
			.build();

		// when
		commentService.postNewComment(postComment, saveMember.getId());

		// then
		Comment findComment = commentRepository.findAll().getFirst();
		assertThat(findComment.getId()).isNotNull();
		assertThat(findComment.getParentComment()).isNull();
		assertThat(findComment.getAuthor()).isNotNull();
		assertThat(findComment.getCommunity()).isNull();
		assertThat(findComment.getResume()).isNotNull();
		assertThat(findComment.getContent()).isEqualTo("댓글");
	}

	@DisplayName("커뮤니티에 새로운 대댓글 작성 합니다.")
	@Test
	void postNewComment3() {
		// given
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList =
			communityDataFactory.createAndSaveCommunityCategory();
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		// 커뮤니티 글 2개, 커뮤니티 당 댓글 1개, 대댓글 2개 씩, 총 6개 댓글 생성
		List<Comment> saveCommentList =
			commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommunityList, 1);

		Community saveCommunity = saveCommunityList.getFirst();
		Member saveMember = saveMemberList.getFirst();
		Comment saveComment = saveCommentList.getFirst();

		CommentRequest.PostComment postComment = CommentRequest.PostComment
			.builder()
			.content("신규 대댓글")
			.category(CommentCategory.COMMUNITY)
			.whereId(saveCommunity.getId())
			.commentId(saveComment.getId())
			.build();

		// when
		commentService.postNewComment(postComment, saveMember.getId());

		// then
		List<Comment> findCommentList = commentRepository.findAll();

		assertThat(findCommentList)
			.filteredOn(comment -> "신규 대댓글".equals(comment.getContent()))
			.filteredOn(comment -> comment.getParentComment() != null)
			.filteredOn(comment -> comment.getParentComment().getId().equals(saveComment.getId()))
			.as("신규 대댓글이 정상적으로 저장되었는지 확인")
			.hasSize(1);
	}

	@DisplayName("커뮤니티에 댓글을 작성하는데, 회원 정보가 없다면 오류를 발생합니다.")
	@Test
	void postNewComment4() {
		// given
		CommentRequest.PostComment postComment = CommentRequest.PostComment
			.builder()
			.content("신규 대댓글")
			.category(CommentCategory.COMMUNITY)
			.whereId(1L)
			.commentId(1L)
			.build();

		// when // then
		assertThatThrownBy(() -> commentService.postNewComment(postComment, "not_exist_uuid"))
			.isInstanceOf(GlobalException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
	}

	@DisplayName("커뮤니티의 댓글 목록을 확인합니다. 페이지네이션 (0 page, 5 size")
	@Test
	void getCommentList1() {
		// given
		// 게시글 2개 생성. 각 게시글에 댓글 10개, 댓글당 대댓글 10개
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList = communityDataFactory
			.createAndSaveCommunityCategory();
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);
		commentDataFactory.createAndSaveCommunityComment(saveMemberList, saveCommunityList, 10);

		Community community = saveCommunityList.getFirst();
		Member saveMember = saveMemberList.getFirst();
		Pageable pageable = PageRequest.of(0, 5);

		// when
		PageResponse<CommentResponse.CommentInfo> commentList =
			commentService.getCommentList(pageable, saveMember.getId(), CommentCategory.COMMUNITY, community.getId());

		// then
		assertThat(commentList)
			.isNotNull()
			.satisfies(response -> {
				assertThat(response.getPageNo()).isZero();
				assertThat(response.getPageSize()).isEqualTo(5);
				assertThat(response.getTotalElements()).isEqualTo(10);
				assertThat(response.getTotalPages()).isEqualTo(2);
				assertThat(response.isLast()).isFalse();
				assertThat(response.isFirst()).isTrue();
				assertThat(response.isHasNext()).isTrue();
				assertThat(response.isHasPrevious()).isFalse();
			});

		assertThat(commentList.getContent())
			.hasSize(5)
			.allSatisfy(content -> {
				assertThat(content.getId()).isNotNull();
				assertThat(content.getAuthor()).isNotNull();
				assertThat(content.getCreatedAt()).isNotNull();
				assertThat(content.getIsAuthor()).isTrue();
				assertThat(content.getSubComment()).hasSize(10);
			});
	}

	@DisplayName("커뮤니티의 댓글 목록을 확인합니다. 조회 시, 데이터가 없다면 빈 리스트를 반환")
	@Test
	void getCommentList2() {
		// given
		// 게시글 2개 생성. 각 게시글에 댓글 10개, 댓글당 대댓글 10개
		List<Member> saveMemberList =
			memberDataFactory.createAndSaveMember(1);
		List<CommunityCategory> saveCommunityCategoryList = communityDataFactory
			.createAndSaveCommunityCategory();
		List<Community> saveCommunityList =
			communityDataFactory.createAndSaveCommunity(saveMemberList, saveCommunityCategoryList, 1);

		Community community = saveCommunityList.getFirst();
		Member saveMember = saveMemberList.getFirst();
		Pageable pageable = PageRequest.of(0, 5);

		// when
		PageResponse<CommentResponse.CommentInfo> commentList =
			commentService.getCommentList(pageable, saveMember.getId(), CommentCategory.COMMUNITY, community.getId());

		// then
		assertThat(commentList)
			.isNotNull()
			.satisfies(response -> {
				assertThat(response.getPageNo()).isZero();
				assertThat(response.getPageSize()).isEqualTo(5);
				assertThat(response.getTotalElements()).isZero();
				assertThat(response.getTotalPages()).isZero();
				assertThat(response.isLast()).isTrue();
				assertThat(response.isFirst()).isTrue();
				assertThat(response.isHasNext()).isFalse();
				assertThat(response.isHasPrevious()).isFalse();
			});

		assertThat(commentList.getContent())
			.isEmpty();
	}
}