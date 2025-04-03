package ssammudan.cotree.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.comment.dto.CommentRequest;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.common.comment.entity.Comment;
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
}
