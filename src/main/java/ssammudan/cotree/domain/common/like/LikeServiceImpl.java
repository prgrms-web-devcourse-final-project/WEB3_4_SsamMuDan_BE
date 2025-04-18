package ssammudan.cotree.domain.common.like;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.common.dto.LikeRequest;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.common.like.entity.Like;
import ssammudan.cotree.model.common.like.repository.LikeRepository;
import ssammudan.cotree.model.community.community.repository.CommunityRepository;
import ssammudan.cotree.model.education.techbook.techbook.repository.TechBookRepository;
import ssammudan.cotree.model.education.techtube.techtube.repository.TechTubeRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.project.project.repository.ProjectRepository;

/**
 * PackageName : ssammudan.cotree.domain.common.like
 * FileName    : LikeServiceImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : Like 서비스 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 */
@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

	private final LikeRepository likeRepository;
	private final MemberRepository memberRepository;
	private final TechTubeRepository techTubeRepository;
	private final TechBookRepository techBookRepository;
	private final ProjectRepository projectRepository;
	private final CommunityRepository communityRepository;

	/**
	 * Like 신규 생성
	 *
	 * @param memberId   - 회원 ID
	 * @param requestDto - LikeRequest Create DTO
	 * @return PK
	 */
	@Override
	public Long createLike(final String memberId, final LikeRequest requestDto) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

		//좋아요 타입에 따른 분기, Like 엔티티 생성/저장
		return likeRepository.save(createDesignedLike(member, requestDto)).getId();
	}

	/**
	 * Like 삭제
	 *
	 * @param memberId   - 회원 ID
	 * @param requestDto - LikeRequest DTO
	 */
	@Override
	public void deleteLike(final String memberId, final LikeRequest requestDto) {
		Optional<Like> opLike = getLike(memberId, requestDto);
		if (opLike.isPresent()) {
			likeRepository.delete(opLike.get());
		} else {
			throw new GlobalException(ErrorCode.LIKE_NOT_FOUND);
		}
	}

	/**
	 * Optional Like 엔티티 조회
	 *
	 * @param memberId   - Member ID
	 * @param requestDto - LikeRequest DTO
	 * @return Optional<Like>
	 */
	private Optional<Like> getLike(final String memberId, final LikeRequest requestDto) {
		return switch (requestDto.likeType()) {
			case TECH_TUBE -> likeRepository.findByMember_IdAndTechTube_Id(memberId, requestDto.itemId());
			case TECH_BOOK -> likeRepository.findByMember_IdAndTechBook_Id(memberId, requestDto.itemId());
			case PROJECT -> likeRepository.findByMember_IdAndProject_Id(memberId, requestDto.itemId());
			case COMMUNITY -> likeRepository.findByMember_IdAndCommunity_Id(memberId, requestDto.itemId());
		};
	}

	/**
	 * 파라미터를 활용하여 분기 후 Like 엔티티 생성
	 *
	 * @param member     - Member
	 * @param requestDto - LikeRequest DTO
	 * @return Like
	 */
	private Like createDesignedLike(final Member member, final LikeRequest requestDto) {
		Optional<Like> opLike = getLike(member.getId(), requestDto);

		//이미 추가된 좋아요에 대한 중복 시도의 경우
		if (opLike.isPresent()) {
			throw new GlobalException(ErrorCode.LIKE_DUPLICATED);    //TODO: 예외 처리 vs 단순 응답 처리 vs Circuit Breaker 등
		}

		return switch (requestDto.likeType()) {
			case TECH_TUBE -> Like.create(
				member, techTubeRepository.findById(requestDto.itemId())
					.orElseThrow(() -> new GlobalException(ErrorCode.TECH_TUBE_NOT_FOUND))
			);
			case TECH_BOOK -> Like.create(
				member, techBookRepository.findById(requestDto.itemId())
					.orElseThrow(() -> new GlobalException(ErrorCode.TECH_BOOK_NOT_FOUND))
			);
			case PROJECT -> Like.create(
				member, projectRepository.findById(requestDto.itemId())
					.orElseThrow(() -> new GlobalException(ErrorCode.PROJECT_NOT_FOUND))
			);
			case COMMUNITY -> Like.create(
				member, communityRepository.findById(requestDto.itemId())
					.orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND))
				//TODO: Community 관련 에러 코드 확인 필요
			);
		};
	}

}
