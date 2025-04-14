package ssammudan.cotree.domain.comment.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.comment.dto.CommentRequest;
import ssammudan.cotree.domain.comment.dto.CommentResponse;
import ssammudan.cotree.domain.comment.type.CommentCategory;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.model.common.comment.entity.Comment;
import ssammudan.cotree.model.common.comment.repository.CommentInfoProjection;
import ssammudan.cotree.model.common.comment.repository.CommentRepository;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.community.community.repository.CommunityRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;
import ssammudan.cotree.model.recruitment.resume.resume.repository.ResumeRepository;

/**
 * PackageName : ssammudan.cotree.domain.comment.service
 * FileName    : CommentServiceImpl
 * Author      : Baekgwa
 * Date        : 2025-04-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-02     Baekgwa               Initial creation
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final CommunityRepository communityRepository;
	private final ResumeRepository resumeRepository;

	@Transactional
	@Override
	public void postNewComment(final CommentRequest.PostComment postComment, final String memberId) {
		// 댓글 작성자 조회
		Member commentAuthor = findCommentAuthor(memberId);

		// 부모 댓글 조회
		Comment parentComment = findParentComment(postComment.getCommentId());

		// 카테고리별 댓글 생성 및 저장
		Comment newComment = createCommentByCategory(postComment, commentAuthor, parentComment);

		commentRepository.save(newComment);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<CommentResponse.CommentInfo> getCommentList(
		final Pageable pageable,
		final String memberId,
		final CommentCategory category,
		final Long itemId) {

		// 댓글 데이터 조회
		List<CommentInfoProjection> commentProjections =
			commentRepository.findCommentListByPaging(pageable, memberId, category, itemId);

		// 데이터가 없는 경우 빈 응답 반환
		if (commentProjections.isEmpty()) {
			return PageResponse.of(new PageImpl<>(List.of(), pageable, 0));
		}

		// 총 부모 댓글 수 조회
		Long totalCount = commentRepository.findCommentListCounts(category, itemId);

		// 비즈니스 로직: 댓글 구조화 작업 (여기서 DTO 변환)
		List<CommentResponse.CommentInfo> structuredComments = buildCommentStructure(commentProjections);

		// 페이지 응답 생성 및 반환
		Page<CommentResponse.CommentInfo> commentPage =
			new PageImpl<>(structuredComments, pageable, totalCount != null ? totalCount : 0);
		return PageResponse.of(commentPage);
	}

	private Member findCommentAuthor(String memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private Comment findParentComment(Long parentCommentId) {
		if (parentCommentId == null) {
			return null;
		}

		return commentRepository.findById(parentCommentId)
			.orElseThrow(() -> new GlobalException(ErrorCode.POST_COMMENT_FAIL_PARENT_COMMENT_NOTFOUND));
	}

	private Comment createCommentByCategory(CommentRequest.PostComment postComment, Member commentAuthor,
		Comment parentComment) {
		return switch (postComment.getCategory()) {
			case COMMUNITY -> createCommunityComment(postComment, commentAuthor, parentComment);
			case RESUME -> createResumeComment(postComment, commentAuthor, parentComment);
			default -> throw new GlobalException(ErrorCode.POST_COMMENT_FAIL_INVALID_CATEGORY);
		};
	}

	private Comment createCommunityComment(CommentRequest.PostComment postComment, Member commentAuthor,
		Comment parentComment) {
		Community targetCommunity = communityRepository.findById(postComment.getWhereId())
			.orElseThrow(() -> new GlobalException(ErrorCode.POST_COMMENT_FAIL_COMMUNITY_NOTFOUND));

		return Comment.createNewCommunityComment(commentAuthor, targetCommunity, parentComment,
			postComment.getContent());
	}

	private Comment createResumeComment(CommentRequest.PostComment postComment, Member commentAuthor,
		Comment parentComment) {
		Resume targetResume = resumeRepository.findById(postComment.getWhereId())
			.orElseThrow(() -> new GlobalException(ErrorCode.POST_COMMENT_FAIL_RESUME_NOTFOUND));

		return Comment.createNewResumeComment(commentAuthor, targetResume, parentComment, postComment.getContent());
	}

	/**
	 * 댓글과 대댓글 구조 구성
	 */
	private List<CommentResponse.CommentInfo> buildCommentStructure(List<CommentInfoProjection> allCommentList) {
		// 대댓글을 부모 ID 기준으로 그룹화
		Map<Long, List<CommentResponse.ChildCommentInfo>> subCommentMap = allCommentList.stream()
			.filter(c -> c.getParentId() != null)
			.collect(Collectors.groupingBy(
				CommentInfoProjection::getParentId,
				Collectors.mapping(CommentResponse.ChildCommentInfo::of, Collectors.toList())
			));

		// 부모 댓글 리스트 생성
		List<CommentInfoProjection> parentCommentList = allCommentList.stream()
			.filter(c -> c.getParentId() == null)
			.toList();

		// 대댓글 최신순 정렬
		subCommentMap.forEach((parentId, subComments) ->
			subComments.sort(Comparator.comparing(CommentResponse.ChildCommentInfo::getCreatedAt).reversed())
		);

		// 최종 결과 빌드 & 리턴
		return parentCommentList.stream()
			.map(parent -> CommentResponse.CommentInfo.from(parent,
				subCommentMap.getOrDefault(parent.getId(), List.of())))
			.toList();
	}
}
