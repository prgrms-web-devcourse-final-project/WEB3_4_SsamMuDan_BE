package ssammudan.cotree.domain.community.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.community.dto.CommunityRequest;
import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.domain.community.type.SearchBoardCategory;
import ssammudan.cotree.domain.community.type.SearchBoardSort;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.community.category.repository.CommunityCategoryRepository;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.community.community.repository.CommunityRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;

/**
 * PackageName : ssammudan.cotree.domain.community.service
 * FileName    : CommunityServiceImpl
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : Community domain service layer
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28     Baekgwa               Initial creation
 */
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

	private final CommunityCategoryRepository communityCategoryRepository;
	private final CommunityRepository communityRepository;
	private final MemberRepository memberRepository;

	@Transactional
	@Override
	public void createNewBoard(CommunityRequest.CreateBoard createBoard, String userId) {
		// 카테고리 조회 및 유효성 확인
		CommunityCategory findCommunityCategory = communityCategoryRepository.findByName(createBoard.getCategory())
				.orElseThrow(() -> new GlobalException(ErrorCode.COMMUNITY_BOARD_CATEGORY_INVALID));

		// userId 로 회원 정보 검색
		Member findMember = memberRepository.findById(userId)
				.orElseThrow(() -> new GlobalException(ErrorCode.COMMUNITY_MEMBER_NOTFOUND));

		// 새 글 저장
		Community newCommunityBoard =
				Community.createNewCommunityBoard(findCommunityCategory, findMember, createBoard.getTitle(),
						createBoard.getContent());

		communityRepository.save(newCommunityBoard);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<CommunityResponse.BoardListDetail> getBoardList(
			final Pageable pageable,
			final SearchBoardSort sort,
			final SearchBoardCategory category,
			final String keyword,
			final String memberId) {

		Page<CommunityResponse.BoardListDetail> findBoardList =
				communityRepository.findBoardList(pageable, sort, category, keyword, memberId);

		//todo : findBoardList 의 내용 중, Content 들, 글자수 제한 및 이미지 제거 필요.
		return PageResponse.of(findBoardList);
	}
}
