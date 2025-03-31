package ssammudan.cotree.domain.community.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import ssammudan.cotree.domain.community.dto.CommunityRequest;
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

	@DisplayName("커뮤니티 새글 작성")
	@Test
	void createNewBoard() {
		// given
		List<CommunityCategory> savedCommunityCategoryList = communityFactory.createAndSaveCommunityCategory(10);
		CommunityCategory savedCommunityCategory = savedCommunityCategoryList.getFirst();
		String communityCategoryName = savedCommunityCategory.getName();

		String newTitle = "새글 제목";
		String newContent = "새글 작성";
		CommunityRequest.CreateBoard createBoard =
				new CommunityRequest.CreateBoard(communityCategoryName, newTitle, newContent);

		List<Member> savedMemberList = memberFactory.createAndSaveMember(10);
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
				.extracting("id", "communityCategory", "member", "title", "content", "viewCount")
				.containsExactly(savedMember.getId(), savedCommunityCategory, savedMember, newTitle, newContent, 0);
	}
}