package ssammudan.cotree.model.community.category.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import ssammudan.cotree.integration.SpringBootTestSupporter;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;

/**
 * PackageName : ssammudan.cotree.model.community.category.repository
 * FileName    : CommunityCategoryRepositoryTest
 * Author      : Baekgwa
 * Date        : 2025-03-30
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-30     Baekgwa               Initial creation
 */
@Transactional
class CommunityCategoryRepositoryTest extends SpringBootTestSupporter {

	@DisplayName("카테고리 이름으로 Entity 찾기")
	@Test
	void findByName() {
		// given
		List<CommunityCategory> savedCommunityCategory = communityFactory.createAndSaveCommunityCategory(10);
		String categoryName = savedCommunityCategory.getFirst().getName();
		em.flush();
		em.clear();

		// when
		Optional<CommunityCategory> findOptionalData = communityCategoryRepository.findByName(categoryName);

		// then
		assertThat(findOptionalData).isPresent();
		assertThat(findOptionalData.get().getName()).isEqualTo(categoryName);
	}
}
