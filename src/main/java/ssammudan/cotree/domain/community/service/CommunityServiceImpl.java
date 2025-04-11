package ssammudan.cotree.domain.community.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.community.dto.CommunityRequest;
import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.domain.community.type.SearchBoardCategory;
import ssammudan.cotree.domain.community.type.SearchBoardSort;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.infra.viewcount.persistence.ViewCountStore;
import ssammudan.cotree.infra.viewcount.type.ViewCountType;
import ssammudan.cotree.model.common.comment.repository.CommentRepository;
import ssammudan.cotree.model.common.like.repository.LikeRepository;
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
 * 2025-04-04     Baekgwa               글 수정/삭제 기능 추가
 * 2025-04-07     Baekgwa               Thumbnail 이미지 출력 정상화
 * 2025-04-08     Baekgwa               커뮤니티 글 작성 시, community category 입력 형식 변경. 기존 : String / 변경 : Long id
 * 2025-04-11     Baekgwa               내가 좋아요 (관심)한, Community 목록 조회 기능 추가
 */
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

	private final CommunityCategoryRepository communityCategoryRepository;
	private final CommunityRepository communityRepository;
	private final MemberRepository memberRepository;
	private final ViewCountStore viewCountStore;
	private final LikeRepository likeRepository;
	private final CommentRepository commentRepository;

	@Transactional
	@Override
	public CommunityResponse.BoardCreate createNewBoard(
		@NonNull final CommunityRequest.CreateBoard createBoard,
		@NonNull final String memberId
	) {
		// 카테고리 조회 및 유효성 확인
		CommunityCategory findCommunityCategory = communityCategoryRepository.findById(
				createBoard.getCommunityCategoryId())
			.orElseThrow(() -> new GlobalException(ErrorCode.COMMUNITY_BOARD_CATEGORY_INVALID));

		// userId 로 회원 정보 검색
		Member findMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.COMMUNITY_MEMBER_NOTFOUND));

		// content 로부터 thumbnail image 추출
		String thumbnailImage = extractThumbnailByContent(createBoard.getContent());

		// 새 글 저장
		Community newCommunityBoard =
			Community.createNewCommunityBoard(
				findCommunityCategory,
				findMember,
				createBoard.getTitle(),
				createBoard.getContent(),
				thumbnailImage);

		//저장 후 응답
		Community savedCommunity = communityRepository.save(newCommunityBoard);
		return CommunityResponse.BoardCreate.of(savedCommunity.getId());
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<CommunityResponse.BoardListDetail> getBoardList(
		@NonNull final Pageable pageable,
		@NonNull final SearchBoardSort sort,
		@NonNull final SearchBoardCategory category,
		@NonNull final String keyword,
		@Nullable final String memberId
	) {
		// Board 데이터 조회
		Page<CommunityResponse.BoardListDetail> findBoardList =
			communityRepository.findBoardList(pageable, sort, category, keyword, memberId);

		// Content 내용 후처리
		// 마크다운 이미지 형식 + 불필요한 띄워쓰기 공백 삭제 처리.
		List<CommunityResponse.BoardListDetail> processedList = findBoardList.getContent().stream()
			.map(board -> board.withContent(processMarkdownContent(board.content())))
			.toList();

		// 새로운 페이지 Response 객체 생성
		Page<CommunityResponse.BoardListDetail> processedPage = new PageImpl<>(
			processedList,
			findBoardList.getPageable(),
			findBoardList.getTotalElements()
		);

		return PageResponse.of(processedPage);
	}

	@Transactional(readOnly = true)
	@Override
	public CommunityResponse.BoardDetail getBoardDetail(
		@NotNull final Long boardId,
		@NonNull final String memberId
	) {
		// 게시글 유무 확인
		if (!communityRepository.existsById(boardId)) {
			throw new GlobalException(ErrorCode.COMMUNITY_BOARD_NOTFOUND);
		}

		// 게시글 정보 조회
		CommunityResponse.BoardDetail findData = communityRepository.findBoard(boardId, memberId);

		// 게시글 조회수 count 업데이트
		viewCountStore.incrementViewCount(ViewCountType.COMMUNITY, boardId);

		return findData;
	}

	@Transactional
	@Override
	public void modifyBoard(
		@NonNull final Long boardId,
		@NonNull final CommunityRequest.ModifyBoard modifyBoard,
		@NonNull final String memberId
	) {
		// 글 수정 가능 검증
		checkAuthorityBeforeOperation(memberId, boardId);

		// 글 수정.
		// JPA duty checking 사용
		Community findCommunity = communityRepository.findById(boardId)
			.orElseThrow(() -> new GlobalException(ErrorCode.COMMUNITY_BOARD_NOTFOUND));
		findCommunity.modifyCommunity(modifyBoard.getTitle(), modifyBoard.getContent());
	}

	@Transactional
	@Override
	public void deleteBoard(
		@NonNull final Long boardId,
		@NonNull final String memberId
	) {
		// 글 삭제 가능 검증
		checkAuthorityBeforeOperation(memberId, boardId);

		// 글 관련 연관된 데이터 우선 삭제 처리
		deleteCommunityDependencies(boardId);

		// 현재 글삭제는 하드 delete
		communityRepository.deleteById(boardId);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<CommunityResponse.BoardLikeListDetail> getBoardLikeList(
		Pageable pageable, String memberId
	) {
		return PageResponse.of(likeRepository.findBoardLikeList(pageable, memberId));
	}

	/**
	 * 커뮤니티 board 를 삭제 전, 연관된 데이터를 삭제한다.
	 * 연관된 Entity 는 [Like, Comment] 이다.
	 * @param boardId 삭제할 연관 데이터의 FK
	 */
	private void deleteCommunityDependencies(@NotNull final Long boardId) {
		// 연관 댓글 삭제 (대댓글 먼저 삭제)
		commentRepository.deleteChildComments(boardId);

		// 연관 댓글 삭제 (댓글 먼저 삭제)
		commentRepository.deleteParentComments(boardId);

		// 연관 좋아요 삭제
		likeRepository.deleteAllByCommunityId(boardId);
	}

	/**
	 * 글 (삭제, 수정 등) 조작 전, 유효성 검증 공통 메서드
	 * 1. 글 존재 유무 확인
	 * 2. 글 조작 가능 권한 확인
	 * @param memberId 로그인 회원 uid
	 * @param boardId 대상 게시글 id
	 */
	private void checkAuthorityBeforeOperation(final String memberId, final Long boardId) {
		//글 존재 유무 확인
		if (!communityRepository.existsById(boardId)) {
			throw new GlobalException(ErrorCode.COMMUNITY_BOARD_NOTFOUND);
		}

		//로그인 회원, 작성자인지 검증
		if (!communityRepository.isBoardAuthor(memberId, boardId)) {
			throw new GlobalException(ErrorCode.COMMUNITY_BOARD_OPERATION_FAIL_NOT_AUTHOR);
		}
	}

	/**
	 * 마크다운 콘텐츠에서 이미지 마크업을 제거
	 */
	private String processMarkdownContent(final String content) {
		if (content == null || content.isEmpty()) {
			return "";
		}

		// 마크다운 이미지 패턴 (![대체텍스트](이미지URL) 형식)
		String imagePattern = "!\\[[^\\]]*+\\]\\([^\\)]*+\\)";

		// 마크다운 이미지 제거
		String withoutImages = content.replaceAll(imagePattern, "");

		// 불필요한 공백 및 연속된 줄바꿈 정리
		return withoutImages.replaceAll("\\s+", " ").trim();
	}

	/**
	 * 글 내용 중, 썸네일로 사용될 이미지 추출
	 * @param content 글 내용
	 * @return 추출된 이미지 url
	 */
	private String extractThumbnailByContent(String content) {
		if (content == null || content.isBlank()) {
			return null;
		}

		// 마크다운 이미지 파싱: ![alt](url)
		Pattern markdownImgPattern = Pattern.compile("!\\[[^\\]]*\\]\\(([^\\)]+)\\)");
		Matcher markdownMatcher = markdownImgPattern.matcher(content);
		if (markdownMatcher.find()) {
			return markdownMatcher.group(1); // 이미지 URL
		}

		// 이미지가 없을 경우
		return null;
	}
}
