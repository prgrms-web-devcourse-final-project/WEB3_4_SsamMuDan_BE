package ssammudan.cotree.integration.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.model.common.like.entity.Like;
import ssammudan.cotree.model.common.like.repository.LikeRepository;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.integration.factory
 * FileName    : LikeDataFactory
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
public class LikeDataFactory {

	private final LikeRepository likeRepository;
	private final EntityManager em;

	/**
	 * 입력받은 모든 member 가 모든 community 글에 좋아요를 누름
	 * @param memberList target member list
	 * @param communityList target community list
	 */
	public void createAndSaveCommunityLike(
		final List<Member> memberList,
		final List<Community> communityList
	) {

		List<Like> likeList = new ArrayList<>();

		for (Member member : memberList) {
			for (Community community : communityList) {
				Like newLike = Like.create(member, community);
				likeList.add(newLike);
			}
		}

		likeRepository.saveAll(likeList);

		em.flush();
		em.clear();
	}
}
