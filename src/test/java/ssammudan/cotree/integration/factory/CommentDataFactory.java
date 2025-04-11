package ssammudan.cotree.integration.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.model.common.comment.entity.Comment;
import ssammudan.cotree.model.common.comment.repository.CommentRepository;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.integration.factory
 * FileName    : CommentDataFactory
 * Author      : Baekgwa
 * Date        : 2025-04-10
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-10     Baekgwa               Initial creation
 */
@Component
@RequiredArgsConstructor
public class CommentDataFactory {

	private final CommentRepository commentRepository;
	private final EntityManager em;

	/**
	 * 작성되는 댓글 수 : memberList * communityList * count;
	 * 작성되는 대댓글 수 : 댓글수 * count;
	 * 즉, 회원 10명, 글 10개, count 2개 라면
	 * 		댓글 : 10 * 10 * 2 = 200개
	 * 		대댓글 : 200 * 2 = 400개
	 * 		한 커뮤니티 댓글 수 : 20개
	 * 		한 커뮤니티 대댓글 수 : 40개
	 * 		한 커뮤니티 총 댓글 수 : 60개
	 * @param memberList 댓글을 작성할 회원 목록
	 * @param communityList 댓글을 작성할 커뮤니티 글 리스트
	 * @param count 한회원이 하나의 커뮤니티 글에 작성할 댓글/대댓글 개수
	 */
	public void createAndSaveCommunityComment(
		final List<Member> memberList,
		final List<Community> communityList,
		final int count
	) {

		if (count == 0) {
			return;
		}

		List<Comment> parentCommentList = new ArrayList<>();
		List<Comment> childCommentList = new ArrayList<>();

		for (int index = 1; index <= count; index++) {
			for (Member member : memberList) {
				for (Community community : communityList) {
					Comment parentComment =
						Comment.createNewCommunityComment(member, community, null, "댓글");
					parentCommentList.add(parentComment);
					for (int childIndex = 1; childIndex <= count; childIndex++) {
						Comment childComment =
							Comment.createNewCommunityComment(member, community, parentComment, "대댓글");
						childCommentList.add(childComment);
					}
				}
			}
		}

		commentRepository.saveAll(parentCommentList);
		commentRepository.saveAll(childCommentList);

		em.flush();
		em.clear();
	}
}
