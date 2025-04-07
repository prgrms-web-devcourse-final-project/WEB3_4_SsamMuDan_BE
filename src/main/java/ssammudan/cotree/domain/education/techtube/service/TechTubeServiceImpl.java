package ssammudan.cotree.domain.education.techtube.service;

import java.time.Duration;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeRequest;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeResponse;
import ssammudan.cotree.domain.education.type.SearchEducationSort;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.model.common.like.repository.LikeRepository;
import ssammudan.cotree.model.education.category.repository.EducationCategoryRepository;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.level.repository.EducationLevelRepository;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;
import ssammudan.cotree.model.education.techtube.techtube.repository.TechTubeRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;

/**
 * PackageName : ssammudan.cotree.domain.education.techtube.service
 * FileName    : TechTubeServiceImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : TechTube 서비스 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TechTubeServiceImpl implements TechTubeService {

	private final TechTubeRepository techTubeRepository;
	private final MemberRepository memberRepository;
	private final EducationLevelRepository educationLevelRepository;
	private final LikeRepository likeRepository;
	private final EducationCategoryRepository educationCategoryRepository;

	/**
	 * TechTube 신규 생성
	 *
	 * @param memberId   - Member PK
	 * @param requestDto - TechTubeRequest Create DTO
	 * @return PK
	 */
	@Transactional
	@Override
	public Long createTechTube(final String memberId, final TechTubeRequest.Create requestDto) {
		//TechTube 작성자 확인
		Member writer = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

		//입력된 학습 난이도 확인
		EducationLevel educationLevel = educationLevelRepository.findById(requestDto.educationLevel().getId())
			.orElseThrow(() -> new GlobalException(ErrorCode.EDUCATION_LEVEL_NOT_FOUND));

		//TODO: 썸네일 생성 및 동영상 파일 저장 등 로직 확인 필요
		TechTube techTube = TechTube.create(
			writer,
			educationLevel,
			requestDto.title(),
			requestDto.description(),
			requestDto.introduction(),
			requestDto.techTubeUrl(),
			Duration.ofSeconds(requestDto.techTubeDurationSeconds()),
			requestDto.techTubeThumbnailUrl(),
			requestDto.price()
		);

		return techTubeRepository.save(techTube).getId();
	}

	/**
	 * TechTube 단 건 조회
	 *
	 * @param id       - PK
	 * @param memberId - 회원 ID
	 * @return TechTubeResponse Detail DTO
	 */
	@Override
	public TechTubeResponse.Detail findTechTubeById(final Long id, final String memberId) {
		//Member 존재 여부 확인
		if (!memberRepository.existsById(memberId)) {
			throw new GlobalException(ErrorCode.MEMBER_NOT_FOUND);
		}

		TechTube techTube = techTubeRepository.findById(id)
			.orElseThrow(() -> new GlobalException(ErrorCode.TECH_TUBE_NOT_FOUND));
		//TODO: Member-Like 연관관계에 따라 수정 가능
		boolean isLike = likeRepository.existsByMember_IdAndTechTube_Id(memberId, id);
		techTube.increaseViewCount();
		return TechTubeResponse.Detail.from(techTube, isLike);
	}

	/**
	 * TechTube 단 건 조회
	 *
	 * @param id - PK
	 * @return TechTubeResponse Detail DTO
	 */
	@Override
	public TechTubeResponse.Detail findTechTubeById(final Long id) {
		TechTube techTube = techTubeRepository.findById(id)
			.orElseThrow(() -> new GlobalException(ErrorCode.TECH_TUBE_NOT_FOUND));
		techTube.increaseViewCount();
		return TechTubeResponse.Detail.from(techTube);
	}

	/**
	 * TechTube 다 건 조회
	 *
	 * @param keyword  - 검색어
	 * @param pageable - 페이징 객체
	 * @return PageResponse TechTubeResponse ListInfo DTO
	 */
	@Override
	public PageResponse<TechTubeResponse.ListInfo> findAllTechTubes(
		final String keyword,
		final SearchEducationSort sort,
		final Pageable pageable,
		final String memberId,
		final Long educationId
	) {
		//education Id 유효성 검증
		if (educationId != null && !educationCategoryRepository.existsById(educationId)) {
			throw new GlobalException(ErrorCode.INVALID_EDUCATION_CATEGORY_ID);
		}

		return PageResponse.of(
			techTubeRepository.findTechTubeList(keyword, sort, pageable, memberId, educationId));
	}
}
