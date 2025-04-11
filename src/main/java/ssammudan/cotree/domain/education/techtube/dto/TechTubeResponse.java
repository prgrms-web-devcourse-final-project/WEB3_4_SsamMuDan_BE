package ssammudan.cotree.domain.education.techtube.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.With;

/**
 * PackageName : ssammudan.cotree.domain.education.techtube.dto
 * FileName    : TechTubeResponse
 * Author      : loadingKKamo21
 * Date        : 25. 4. 4.
 * Description : TechTube 응답 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 4.     loadingKKamo21       Initial creation
 * 25. 4. 7.     Baekgwa       		  TechTube 목록 조회, 회원의 좋아요 상태 추가
 * 25. 4. 7.     Baekgwa       		  techTube 상세 조회 refactor
 * 2025-04-11     Baekgwa               내가 좋아요 (관심)한, TechTube 목록 조회 기능 추가
 */
@Schema(description = "TechTube 응답")
public class TechTubeResponse {

	private TechTubeResponse() {
	}

	@With
	@Builder(access = AccessLevel.PRIVATE)
	@Schema(description = "TechTube 상세 조회 응답 DTO")
	public record TechTubeDetail(
		@Schema(description = "TechTube 저자", example = "홍길동") String writer,
		@Schema(description = "TechTube 교육 난이도", example = "입문") String educationLevel,
		@Schema(description = "TechTube 교육 카테고리", example = "백엔드") List<String> educationCategoryList,
		@Schema(description = "TechTube 제목", example = "Spring Boot") String title,
		@Schema(description = "TechTube 설명", example = "스프링 부트를 1000% 활용하는 방법!!") String description,
		@Schema(description = "TechTube 소개", example = "스프링 부트에 대한 수 많은 활용법을 담았습니다. ...") String introduction,
		@Schema(description = "TechTube 평균 평점", example = "4.5") Double avgRating,
		@Schema(description = "TechTube 전체 리뷰 수", example = "30") Integer totalReviewCount,
		@Schema(description = "TechTube 영상 URL", example = "https://cotree.ssammudan.com/techtube/SpringBoot.mp4") String techTubeUrl,
		@Schema(description = "TechTube 영상 길이", example = "3600") Long durationSeconds,
		@Schema(description = "TechTube 썸네일 URL", example = "https://cotree.ssammudan.com/techtube/SpringBoot_thumbnail.png") String thumbnailUrl,
		@Schema(description = "TechTube 가격", example = "10000") Integer price,
		@Schema(description = "TechTube 조회 수", example = "123") Integer viewCount,
		@Schema(description = "TechTube 좋아요 수", example = "123") Long likeCount,
		@Schema(description = "로그인된 회원의 TechTube 좋아요 여부", example = "true") Boolean isLike,
		@Schema(description = "TechTube 등록 일자", example = "2025-01-01") LocalDateTime createdAt,
		@Schema(description = "TechTube 결제 유무", example = "true") Boolean isPaymentDone) {

		public void addEducationCategoryList(List<String> findCategoryList) {
			this.educationCategoryList.addAll(findCategoryList);
		}
	}

	@Schema(description = "TechTube 목록 조회 응답 DTO")
	public record ListInfo(
		@Schema(description = "TechTube ID", example = "1") Long id,
		@Schema(description = "TechTube 저자", example = "홍길동") String writer,
		@Schema(description = "TechTube 제목", example = "Spring Boot") String title,
		@Schema(description = "TechTube 가격", example = "10000") Integer price,
		@Schema(description = "TechTube 썸네일 URL",
			example = "https://cotree.ssammudan.com/techtube/SpringBoot_thumbnail.png") String techTubeThumbnailUrl,
		@Schema(description = "TechTube 좋아요 수", example = "123") Long likeCount,
		@Schema(description = "TechTube 등록 일자", example = "2025-01-01") LocalDateTime createdAt,
		@Schema(description = "로그인 회원의 좋아요 유무", example = "true") Boolean isLike
	) {
	}

	public record TechTubeLikeListDetail(
		Long id,
		String author,
		String title,
		Integer price,
		String techTubeThumbnailUrl,
		LocalDateTime createdAt,
		String description
	) {
	}
}
