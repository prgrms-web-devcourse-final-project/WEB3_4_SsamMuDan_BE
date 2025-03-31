package ssammudan.cotree.integration.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.community.category.repository.CommunityCategoryRepository;
import ssammudan.cotree.model.community.community.repository.CommunityRepository;

/**
 * PackageName : ssammudan.cotree.integration.factory
 * FileName    : CommunityDataFactory
 * Author      : Baekgwa
 * Date        : 2025-03-31
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-31     Baekgwa               Initial creation
 */
@Component
@RequiredArgsConstructor
public class CommunityDataFactory {

	private final CommunityCategoryRepository communityCategoryRepository;
	private final CommunityRepository communityRepository;

	/**
	 * count : 생성할 카테고리 수
	 * 카테고리는, [카테고리1, 카테고리2, .... 카테고리 N] 생성 및 저장
	 * 반환된 값은, 저장된 CommunityCategory entity list
	 */
	public List<CommunityCategory> createAndSaveCommunityCategory(final int count) {

		if (count == 0) {
			return List.of();
		}

		List<CommunityCategory> categoryList = new ArrayList<>();

		for (int index = 1; index <= count; index++) {
			categoryList.add(CommunityCategory.createNewCommunityCategory(String.format("카테고리%d", index)));
		}

		return communityCategoryRepository.saveAll(categoryList);
	}
}
