package ssammudan.cotree.domain.education.techbook.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import ssammudan.cotree.model.education.category.entity.EducationCategory;
import ssammudan.cotree.model.education.techbook.category.entity.TechBookEducationCategory;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;

/**
 * PackageName : ssammudan.cotree.domain.education.dto
 * FileName    : TechBookResponse
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBook 응답 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 * 25. 4. 1.	 loadingKKamo21       ListInfo 추가
 */
@Schema(description = "TechBook 응답 DTO")
public class TechBookResponse {

	@Schema(description = "TechBook 상세 조회 응답 DTO")
	public record Detail(
		//TODO: 구매 상태 정보 필요(비회원: 비구매, 회원: 구매 vs 비구매)
		//TODO: 로그인한 회원의 좋아요 상태
		@Schema(description = "TechBook ID", example = "1")
		long id,                      //TechBook ID
		@Schema(description = "TechBook 저자", example = "홍길동")
		String writer,                //TechBook 저자
		@Schema(description = "TechBook 교육 난이도", example = "입문")
		String educationLevel,        //TechBook 교육 난이도
		@Schema(description = "TechBook 교육 카테고리", example = "백엔드")
		List<String> educationCategories,
		@Schema(description = "TechBook 제목", example = "Spring Boot")
		String title,                 //TechBook 제목
		@Schema(description = "TechBook 설명", example = "스프링 부트를 1000% 활용하는 방법!!")
		String description,           //TechBook 설명
		@Schema(description = "TechBook 소개", example = "스프링 부트에 대한 수 많은 활용법을 담았습니다. ...")
		String introduction,          //TechBook 소개
		@Schema(description = "TechBook 평균 평점", example = "4.5")
		double avgRating,             //TechBook 평균 평점
		@Schema(description = "TechBook 전체 리뷰 수", example = "30")
		int totalReviewCount,         //TechBook 전체 리뷰 수
		@Schema(description = "TechBook PDF URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot.pdf")
		String techBookUrl,           //TechBook PDF URL
		@Schema(description = "TechBook PDF 미리보기 URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot_preview.pdf")
		String techBookPreviewUrl,    //TechBook PDF 미리보기 URL
		@Schema(description = "TechBook 썸네일 URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot_thumbnail.png")
		String techBookThumbnailUrl,  //TechBook 썸네일 URL
		@Schema(description = "TechBook 페이지 수", example = "100")
		int techBookPage,             //TechBook 페이지 수
		@Schema(description = "TechBook 가격", example = "10000")
		int price,                    //TechBook 가격
		@Schema(description = "TechBook 조회 수", example = "123")
		int viewCount,                //TechBook 조회 수
		@Schema(description = "TechBook 좋아요 수", example = "123")
		long likeCount,                //TechBook 좋아요 수
		@Schema(description = "로그인된 회원의 TechBook 좋아요 여부", example = "true")
		boolean isLike,                //로그인된 회원의 TechBook 좋아요 여부
		@Schema(description = "TechBook 등록 일자", example = "2025-01-01")
		LocalDate createdAt           //TechBook 등록 일자
	) {
		public static Detail from(final TechBook techBook) {
			return new Detail(
				techBook.getId(),
				techBook.getWriter().getNickname(),
				techBook.getEducationLevel().getName(),
				techBook.getTechBookEducationCategories().stream()
					.map(TechBookEducationCategory::getEducationCategory)
					.map(EducationCategory::getName).toList(),    //TODO: 성능 최적화 시 FETCH JOIN 활용 형태 적용 고려
				techBook.getTitle(),
				techBook.getDescription(),
				techBook.getIntroduction(),
				(double)techBook.getTotalRating() / techBook.getTotalReviewCount(),
				techBook.getTotalReviewCount(),
				techBook.getTechBookUrl(),
				techBook.getTechBookPreviewUrl(),
				techBook.getTechBookThumbnailUrl(),
				techBook.getTechBookPage(),
				techBook.getPrice(),
				techBook.getViewCount(),
				techBook.getLikes().size(),
				false,
				techBook.getCreatedAt().toLocalDate()
			);
		}

		public static Detail from(final TechBook techBook, final boolean isLike) {
			return new Detail(
				techBook.getId(),
				techBook.getWriter().getNickname(),
				techBook.getEducationLevel().getName(),
				techBook.getTechBookEducationCategories().stream()
					.map(TechBookEducationCategory::getEducationCategory)
					.map(EducationCategory::getName).toList(),    //TODO: 성능 최적화 시 FETCH JOIN 활용 형태 적용 고려
				techBook.getTitle(),
				techBook.getDescription(),
				techBook.getIntroduction(),
				(double)techBook.getTotalRating() / techBook.getTotalReviewCount(),
				techBook.getTotalReviewCount(),
				techBook.getTechBookUrl(),
				techBook.getTechBookPreviewUrl(),
				techBook.getTechBookThumbnailUrl(),
				techBook.getTechBookPage(),
				techBook.getPrice(),
				techBook.getViewCount(),
				techBook.getLikes().size(),
				isLike,
				techBook.getCreatedAt().toLocalDate()
			);
		}
	}

	@Schema(description = "TechBook 목록 조회 응답 DTO")
	public record ListInfo(
		//TODO: 로그인한 회원의 좋아요 상태
		@Schema(description = "TechBook ID", example = "1")
		long id,    //TechBook ID
		@Schema(description = "TechBook 저자", example = "홍길동")
		String writer,    //TechBook 저자
		@Schema(description = "TechBook 제목", example = "Spring Boot")
		String title,    //TechBook 제목
		@Schema(description = "TechBook 가격", example = "10000")
		int price,    //TechBook 가격
		@Schema(description = "TechBook 썸네일 URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot_thumbnail.png")
		String techBookThumbnailUrl,    //TechBook 썸네일 URL
		@Schema(description = "TechBook 좋아요 수", example = "123")
		long likeCount,    //TechBook 좋아요 수
		@Schema(description = "TechBook 등록 일자", example = "2025-01-01")
		LocalDate createdAt    //TechBook 등록 일자
	) {
		public static ListInfo from(final TechBook techBook) {
			return new ListInfo(
				techBook.getId(),
				techBook.getWriter().getNickname(),
				techBook.getTitle(),
				techBook.getPrice(),
				techBook.getTechBookThumbnailUrl(),
				techBook.getLikes().size(),
				techBook.getCreatedAt().toLocalDate()
			);
		}
	}

}
