package ssammudan.cotree.integration.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.community.type.SearchBoardCategory;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.community.category.repository.CommunityCategoryRepository;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.community.community.repository.CommunityRepository;
import ssammudan.cotree.model.member.member.entity.Member;

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
	private final EntityManager em;

	/**
	 * count : 생성할 카테고리 수
	 * 카테고리는, [카테고리1, 카테고리2, .... 카테고리 N] 생성 및 저장
	 * 반환된 값은, 저장된 CommunityCategory entity list
	 */
	public List<CommunityCategory> createAndSaveCommunityCategory() {

		List<CommunityCategory> categoryList = new ArrayList<>();

		categoryList.add(CommunityCategory.createNewCommunityCategory(SearchBoardCategory.BOARD.getData()));
		categoryList.add(CommunityCategory.createNewCommunityCategory(SearchBoardCategory.CODE_REVIEW.getData()));

		List<CommunityCategory> savedCommunityCategory = communityCategoryRepository.saveAll(categoryList);

		em.flush();
		em.clear();

		return savedCommunityCategory;
	}

	/**
	 * count : 생성할 멤버당 수
	 * 총 생성되는 수 : MemberList * CommunityCategory * count
	 */
	public List<Community> createAndSaveCommunity(List<Member> memberList,
		List<CommunityCategory> communityCategoryList, final int count) {

		if (count == 0) {
			return List.of();
		}

		List<Community> communityList = new ArrayList<>();

		for (int index = 1; index <= count; index++) {
			for (Member member : memberList) {
				for (CommunityCategory category : communityCategoryList) {
					communityList.add(
						Community.createNewCommunityBoard(
							category,
							member,
							String.format("[%s 카테고리]제목:%s멤버", category.getName(), member.getId()),
							String.format("내용입니다. %s 카테고리 %s멤버의 글 입니다.", category.getName(), member.getId()),
							null));
				}
			}
		}

		em.flush();
		em.clear();

		return communityRepository.saveAll(communityList);
	}
}
