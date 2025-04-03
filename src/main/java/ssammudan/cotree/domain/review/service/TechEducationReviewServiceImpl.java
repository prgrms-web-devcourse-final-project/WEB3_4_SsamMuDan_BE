package ssammudan.cotree.domain.review.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.review.dto.TechEducationReviewRequest;
import ssammudan.cotree.domain.review.dto.TechEducationReviewResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.model.education.techbook.techbook.repository.TechBookRepository;
import ssammudan.cotree.model.education.techtube.techtube.repository.TechTubeRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.review.review.entity.TechEducationReview;
import ssammudan.cotree.model.review.review.repository.TechEducationReviewRepository;
import ssammudan.cotree.model.review.reviewtype.entity.TechEducationType;
import ssammudan.cotree.model.review.reviewtype.repository.TechEducationTypeRepository;
import ssammudan.cotree.model.review.reviewtype.type.TechEducationReviewType;

/**
 * PackageName : ssammudan.cotree.domain.review.review.service
 * FileName    : TechEducationReviewServiceImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 1.
 * Description : TechEducatinoReview 서비스 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     loadingKKamo21       Initial creation
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TechEducationReviewServiceImpl implements TechEducationReviewService {

	private final TechEducationReviewRepository techEducationReviewRepository;
	private final MemberRepository memberRepository;
	private final TechEducationTypeRepository techEducationTypeRepository;
	private final TechTubeRepository techTubeRepository;
	private final TechBookRepository techBookRepository;

	/**
	 * TechEducationReview 신규 생성
	 *
	 * @param memberId   - Member PK
	 * @param requestDto - TechEducationReviewRequest Create DTO
	 * @return PK
	 */
	@Transactional
	@Override
	public Long createTechEducationReview(final String memberId, final TechEducationReviewRequest.Create requestDto) {
		//리뷰 작성자 확인
		Member reviewer = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

		//TODO: 작성자의 구매 이력에 해당 교육 컨텐츠 유효성 확인 필요

		Long reviewTypeId = TechEducationReviewType.getTechEducationTypeId(
			requestDto.techEducationType()
		); //TechTube = 1 or TechBook = 2

		//입력된 리뷰의 카테고리 확인(TechBook or TechTube)
		TechEducationType techEducationType = techEducationTypeRepository.findById(
			reviewTypeId
		).orElseThrow(() -> new GlobalException(ErrorCode.TECH_EDUCATION_TYPE_NOT_FOUND));

		//동일한 회원이 동일한 리뷰 대상 항목(TechBook or TechTube)에 대해 작성한 리뷰가 있는지 확인
		if (techEducationReviewRepository.findByReviewer_IdAndTechEducationType_IdAndItemId(
			memberId, reviewTypeId, requestDto.itemId()
		).isPresent()) {
			//TODO: Circuit Breaker 활용 시 -> 기존 리뷰 수정 로직 호출
			throw new GlobalException(ErrorCode.TECH_EDUCATION_REVIEW_DUPLICATED);
		}

		TechEducationReview techEducationReview = TechEducationReview.create(
			reviewer,
			techEducationType,
			requestDto.rating(),
			requestDto.content(),
			requestDto.itemId()
		);

		//리뷰 대상 컨텐츠 리뷰 누적 점수 추가
		switch (requestDto.techEducationType()) {
			case TECH_TUBE -> techTubeRepository.findById(requestDto.itemId())
				.orElseThrow(() -> new GlobalException(ErrorCode.TECH_TUBE_NOT_FOUND))
				.addReviewRating(requestDto.rating());
			case TECH_BOOK -> techBookRepository.findById(requestDto.itemId())
				.orElseThrow(() -> new GlobalException(ErrorCode.TECH_BOOK_NOT_FOUND))
				.addReviewRating(requestDto.rating());
			default -> throw new GlobalException(ErrorCode.INVALID_INPUT_VALUE);
		}

		return techEducationReviewRepository.save(techEducationReview).getId();
	}

	/**
	 * TechEducationReview 다 건 조회
	 *
	 * @param pageable - 페이징 객체
	 * @return PageResponse TechEducationReviewResponse Detail DTO
	 */
	@Override
	public PageResponse<TechEducationReviewResponse.Detail> findAllTechEducationReviews(
		final TechEducationReviewRequest.Read requestDto, final Pageable pageable
	) {
		return PageResponse.of(techEducationReviewRepository.findAllByTechEducationType_IdAndItemId(
			TechEducationReviewType.getTechEducationTypeId(
				requestDto.techEducationType()), requestDto.itemId(), pageable
		).map(TechEducationReviewResponse.Detail::from));
	}

}
