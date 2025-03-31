package ssammudan.cotree.domain.education.techbook.dto;

import java.time.LocalDate;

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
 */
public class TechBookResponse {

	public record Detail(
		//TODO: 구매 상태 정보 필요(비회원: 비구매, 회원: 구매 vs 비구매)
		//TODO: 좋아요 숫자 포함
		long id,                      //TechBook PK
		//String writer,                //TechBook 저자
		String educationLevel,        //TechBook 교육 난이도
		String title,                 //TechBook 제목
		String description,           //TechBook 설명
		String introduction,          //TechBook 소개
		int totalRating,              //TechBook 전체 누적 평점
		int totalReviewCount,         //TechBook 전체 리뷰 수
		String techBookUrl,           //TechBook PDF URL
		String techBookPreviewUrl,    //TechBook PDF 미리보기 URL
		String techBookThumbnailUrl,  //TechBook 썸네일 URL
		int techBookPage,             //TechBook 페이지 수
		int price,                    //TechBook 가격
		int viewCount,                //TechBook 조회 수
		LocalDate createdAt           //TechBook 등록 일자
	) {
		public static Detail from(final TechBook techBook) {
			return new Detail(
				techBook.getId(),
				techBook.getEducationLevel().getName(),
				techBook.getTitle(),
				techBook.getDescription(),
				techBook.getIntroduction(),
				techBook.getTotalRating(),
				techBook.getTotalReviewCount(),
				techBook.getTechBookUrl(),
				techBook.getTechBookPreviewUrl(),
				techBook.getTechBookThumbnailUrl(),
				techBook.getTechBookPage(),
				techBook.getPrice(),
				techBook.getViewCount(),
				techBook.getCreatedAt().toLocalDate()
			);
		}
	}

}
