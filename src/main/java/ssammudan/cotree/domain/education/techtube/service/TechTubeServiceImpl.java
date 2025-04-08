package ssammudan.cotree.domain.education.techtube.service;

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
 * 25. 4. 7.     Baekgwa       		  techTube 상세 조회 refactor
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TechTubeServiceImpl implements TechTubeService {

	private final TechTubeRepository techTubeRepository;
	private final MemberRepository memberRepository;
	private final EducationLevelRepository educationLevelRepository;
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
			requestDto.techTubeDurationSeconds(),
			requestDto.techTubeThumbnailUrl(),
			requestDto.price()
		);

		return techTubeRepository.save(techTube).getId();
	}

	@Transactional(readOnly = true)
	@Override
	public TechTubeResponse.TechTubeDetail findTechTubeDetail(Long techTubeId, String memberId) {
		// TechTube 유무 검증
		// 단건 조회는, 조회 시 없는 데이터는 조회할 필요가 없음.
		if (!techTubeRepository.existsById(techTubeId)) {
			throw new GlobalException(ErrorCode.TECH_TUBE_NOT_FOUND);
		}

		// TechTube 상세 정보 검색
		TechTubeResponse.TechTubeDetail content = techTubeRepository.findTechTube(techTubeId, memberId);

		// 회원의 구매 상태에 따라, 응답 변경 (url 제거)
		if (Boolean.FALSE.equals(content.isPaymentDone())) {
			content = content.withTechTubeUrl(null);
		}

		// todo: view Count 증가

		return content;
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
