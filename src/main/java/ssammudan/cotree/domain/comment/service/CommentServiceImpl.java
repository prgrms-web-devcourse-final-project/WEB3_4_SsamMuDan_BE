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

	@Transactional
	@Override
	public void postNewComment(final CommentRequest.PostComment postComment, final String memberId) {

		Member commentAuthor = memberRepository.findById(memberId)
				.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));

		switch (postComment.getCategory()) {
			case COMMUNITY -> {
				//커뮤니티 글 조회
				Community targetCommunity = communityRepository.findById(postComment.getWhereId())
						.orElseThrow(() -> new GlobalException(ErrorCode.POST_COMMENT_FAIL_COMMUNITY_NOTFOUND));

				//대댓글 이라면, 댓글 정보 조회
				Comment parrentCommunity = null;
				if (postComment.getCommentId() != null) {
					parrentCommunity = commentRepository.findById(postComment.getCommentId())
							.orElseThrow(
									() -> new GlobalException(ErrorCode.POST_COMMENT_FAIL_PARENT_COMMENT_NOTFOUND));
				}

				//신규 댓글 저장
				commentRepository.save(
						Comment.createNewCommunityComment(
								commentAuthor,
								targetCommunity,
								parrentCommunity,
								postComment.getContent()));
			}
			// case RESUME -> {
			//
			// }
		}
	}
}
