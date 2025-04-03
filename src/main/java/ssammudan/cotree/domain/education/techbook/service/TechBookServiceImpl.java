package ssammudan.cotree.domain.education.techbook.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.education.techbook.dto.TechBookRequest;
import ssammudan.cotree.domain.education.techbook.dto.TechBookResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.model.common.like.repository.LikeRepository;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.level.repository.EducationLevelRepository;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;
import ssammudan.cotree.model.education.techbook.techbook.repository.TechBookRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;

/**
 * PackageName : ssammudan.cotree.domain.education.service
 * FileName    : TechBookServiceImpl
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBook 서비스 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 * 25. 4. 1.     loadingKKamo21       findAllTechBooks() 구현
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TechBookServiceImpl implements TechBookService {

	private final TechBookRepository techBookRepository;
	private final MemberRepository memberRepository;
	private final EducationLevelRepository educationLevelRepository;
	private final LikeRepository likeRepository;

	/**
	 * TechBook 신규 생성
	 *
	 * @param memberId   - Member PK
	 * @param requestDto - TechBookRequest Create DTO
	 * @return PK
	 */
	@Transactional
	@Override
	public Long createTechBook(final String memberId, final TechBookRequest.Create requestDto) {
		//TechBook 작성자 확인
		Member writer = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

		//입력된 학습 난이도 확인
		EducationLevel educationLevel = educationLevelRepository.findById(requestDto.educationLevel().getId())
			.orElseThrow(() -> new GlobalException(ErrorCode.EDUCATION_LEVEL_NOT_FOUND));

		//TODO: 썸네일 생성 및 PDF 파일 저장 등 로직 확인 필요
		TechBook techBook = TechBook.create(
			writer,
			educationLevel,
			requestDto.title(),
			requestDto.description(),
			requestDto.introduction(),
			requestDto.techBookUrl(),
			requestDto.techBookPreviewUrl(),
			requestDto.techBookThumbnailUrl(),
			requestDto.techBookPage(),
			requestDto.price()
		);

		return techBookRepository.save(techBook).getId();
	}

	/**
	 * TechBook 단 건 조회
	 *
	 * @param id - PK
	 * @return TechBookResponse Detail DTO
	 */
	@Override
	public TechBookResponse.Detail findTechBookById(final Long id) {
		TechBook techBook = techBookRepository.findById(id)
			.orElseThrow(() -> new GlobalException(ErrorCode.TECH_BOOK_NOT_FOUND));
		techBook.increseViewCount();
		return TechBookResponse.Detail.from(techBook);
	}

	/**
	 * TechBook 다 건 조회
	 *
	 * @param keyword  - 검색어
	 * @param pageable - 페이징 객체
	 * @return PageResponse TechBookResponse ListInfo DTO
	 */
	@Override
	public PageResponse<TechBookResponse.ListInfo> findAllTechBooks(final String keyword, final Pageable pageable) {
		return PageResponse.of(
			techBookRepository.findAllTechBooksByKeyword(keyword, pageable).map(TechBookResponse.ListInfo::from)
		);
	}

}
