package ssammudan.cotree.domain.education.techbook.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

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
	public record TechBookDetail(
		@Schema(description = "TechBook ID", example = "1") long id,
		@Schema(description = "TechBook 저자", example = "홍길동") String writer,
		@Schema(description = "TechBook 저자 프로필 이미지 URL") String writerProfileImageUrl,
		@Schema(description = "TechBook 교육 난이도", example = "입문") String educationLevel,
		@Schema(description = "TechBook 교육 카테고리", example = "백엔드") List<String> educationCategoryList,
		@Schema(description = "TechBook 제목", example = "Spring Boot") String title,
		@Schema(description = "TechBook 설명", example = "스프링 부트를 1000% 활용하는 방법!!") String description,
		@Schema(description = "TechBook 소개", example = "스프링 부트에 대한 수 많은 활용법을 담았습니다. ...") String introduction,
		@Schema(description = "TechBook 평균 평점", example = "4.5") double avgRating,
		@Schema(description = "TechBook 전체 리뷰 수", example = "30") int totalReviewCount,
		@Schema(description = "TechBook PDF URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot.pdf") String techBookUrl,
		@Schema(description = "TechBook PDF 미리보기 URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot_preview.pdf") String techBookPreviewUrl,
		@Schema(description = "TechBook 썸네일 URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot_thumbnail.png") String techBookThumbnailUrl,
		@Schema(description = "TechBook 페이지 수", example = "100") int techBookPage,
		@Schema(description = "TechBook 가격", example = "10000") int price,
		@Schema(description = "TechBook 조회 수", example = "123") int viewCount,
		@Schema(description = "TechBook 좋아요 수", example = "123") long likeCount,
		@Schema(description = "로그인된 회원의 TechBook 좋아요 여부", example = "true") boolean isLike,
		@Schema(description = "TechBook 등록 일자", example = "2025-01-01") LocalDateTime createdAt,
		@Schema(description = "TechBook 결제 유무", example = "true") boolean isPaymentDone
	) {
		public TechBookDetail withPurchaseCheck() {
			return new TechBookDetail(
				this.id,
				this.writer,
				this.writerProfileImageUrl,
				this.educationLevel,
				this.educationCategoryList,
				this.title,
				this.description,
				this.introduction,
				this.avgRating,
				this.totalReviewCount,
				this.isPaymentDone ? this.techBookUrl : null,
				this.techBookPreviewUrl,
				this.techBookThumbnailUrl,
				this.techBookPage,
				this.price,
				this.viewCount,
				this.likeCount,
				this.isLike,
				this.createdAt,
				this.isPaymentDone
			);
		}

		public void addEducationCategoryList(final List<String> categoryList) {
			this.educationCategoryList.addAll(categoryList);
		}
	}

	@Schema(description = "TechBook 목록 조회 응답 DTO")
	public record ListInfo(
		@Schema(description = "TechBook ID", example = "1") long id,
		@Schema(description = "TechBook 저자", example = "홍길동") String writer,
		@Schema(description = "TechBook 저자 프로필 이미지 URL") String writerProfileImageUrl,
		@Schema(description = "TechBook 카테고리") String category,
		@Schema(description = "TechBook 제목", example = "Spring Boot") String title,
		@Schema(description = "TechBook 가격", example = "10000") int price,
		@Schema(description = "TechBook 썸네일 URL", example = "https://cotree.ssammudan.com/techbook/SpringBoot_thumbnail.png") String techBookThumbnailUrl,
		@Schema(description = "TechBook 좋아요 수", example = "123") long likeCount,
		@Schema(description = "TechBook 등록 일자", example = "2025-01-01") LocalDateTime createdAt,
		@Schema(description = "로그인된 회원의 좋아요 우무", example = "true") boolean isLike
	) {
	}

}
