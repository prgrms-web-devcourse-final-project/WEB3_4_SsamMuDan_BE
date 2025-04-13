package ssammudan.cotree.model.common.like.repository;

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
import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.model.common.like.repository
 * FileName    : LikeRepositoryImplTest
 * Author      : Baekgwa
 * Date        : 2025-04-13
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-13     Baekgwa               Initial creation
 */
@Transactional
class LikeRepositoryImplTest extends SpringBootTestSupporter {

	@DisplayName("내가 좋아요 누른 커뮤니티 글을 조회 합니다.")
	@Test
	void findBoardLikeList1() {
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
		Page<CommunityResponse.BoardLikeListDetail> content =
			likeRepository.findBoardLikeList(pageable, saveMember.getId());

		// then
		assertThat(content).asInstanceOf(InstanceOfAssertFactories.type(Page.class))
			.satisfies(page -> {
				assertThat(page.getTotalElements()).isEqualTo(20L);
				assertThat(page.getTotalPages()).isEqualTo(2);
				assertThat(page.getNumber()).isZero();
				assertThat(page.getSize()).isEqualTo(16);
				assertThat(page.getNumberOfElements()).isEqualTo(16);
				assertThat(page.isFirst()).isTrue();
				assertThat(page.isLast()).isFalse();
				assertThat(page.hasNext()).isTrue();
				assertThat(page.hasPrevious()).isFalse();
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
}