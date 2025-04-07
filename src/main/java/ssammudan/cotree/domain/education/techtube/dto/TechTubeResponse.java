package ssammudan.cotree.domain.education.techtube.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import ssammudan.cotree.model.education.category.entity.EducationCategory;
import ssammudan.cotree.model.education.techtube.category.entity.TechTubeEducationCategory;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;

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
 */
@Schema(description = "TechTube 응답\n"
	+ "\t\t//TODO: 구매 상태 정보 필요(비회원: 비구매, 회원: 구매 vs 비구매)")
public class TechTubeResponse {

	@Schema(description = "TechTube 상세 조회 응답 DTO")
	public record Detail(
		//TODO: 구매 상태 정보 필요(비회원: 비구매, 회원: 구매 vs 비구매)
		//TODO: 로그인한 회원의 좋아요 상태
		@Schema(description = "TechTube ID", example = "1")
		long id,                      //TechTube ID
		@Schema(description = "TechTube 저자", example = "홍길동")
		String writer,                //TechTube 저자
		@Schema(description = "TechTube 교육 난이도", example = "입문")
		String educationLevel,        //TechTube 교육 난이도
		@Schema(description = "TechTube 교육 카테고리", example = "백엔드")
		List<String> educationCategories,
		@Schema(description = "TechTube 제목", example = "Spring Boot")
		String title,                 //TechTube 제목
		@Schema(description = "TechTube 설명", example = "스프링 부트를 1000% 활용하는 방법!!")
		String description,           //TechTube 설명
		@Schema(description = "TechTube 소개", example = "스프링 부트에 대한 수 많은 활용법을 담았습니다. ...")
		String introduction,          //TechTube 소개
		@Schema(description = "TechTube 평균 평점", example = "4.5")
		double avgRating,             //TechTube 평균 평점
		@Schema(description = "TechTube 전체 리뷰 수", example = "30")
		int totalReviewCount,         //TechTube 전체 리뷰 수
		@Schema(description = "TechTube 영상 URL", example = "https://cotree.ssammudan.com/techtube/SpringBoot.mp4")
		String techTubeUrl,           //TechTube 영상 URL
		//TODO: 프리뷰(미리보기) 영상 링크 추가 여부 확인 필요
		// @Schema(description = "TechTube 영상 미리보기 URL", example = "https://cotree.ssammudan.com/techtube/SpringBoot_preview.mp4")
		// String techTubePreviewUrl,    //TechTube 영상 미리보기 URL
		@Schema(description = "TechTube 영상 길이(초)", example = "3600")
		long techTubeDurationSeconds,
		@Schema(description = "TechTube 썸네일 URL", example = "https://cotree.ssammudan.com/techtube/SpringBoot_thumbnail.png")
		String techTubeThumbnailUrl,  //TechTube 썸네일 URL
		@Schema(description = "TechTube 가격", example = "10000")
		int price,                    //TechTube 가격
		@Schema(description = "TechTube 조회 수", example = "123")
		int viewCount,                //TechTube 조회 수
		@Schema(description = "TechTube 좋아요 수", example = "123")
		long likeCount,                //TechTube 좋아요 수
		@Schema(description = "로그인된 회원의 TechTube 좋아요 여부", example = "true")
		boolean isLike,                //로그인된 회원의 TechTube 좋아요 여부
		@Schema(description = "TechTube 등록 일자", example = "2025-01-01")
		LocalDate createdAt           //TechTube 등록 일자
	) {
		public static Detail from(final TechTube techTube) {
			return new Detail(
				techTube.getId(),
				techTube.getWriter().getNickname(),
				techTube.getEducationLevel().getName(),
				techTube.getTechTubeEducationCategories().stream()
					.map(TechTubeEducationCategory::getEducationCategory)
					.map(EducationCategory::getName).toList(),    //TODO: 성능 최적화 시 FETCH JOIN 활용 형태 적용 고려
				techTube.getTitle(),
				techTube.getDescription(),
				techTube.getIntroduction(),
				(double)techTube.getTotalRating() / techTube.getTotalReviewCount(),
				techTube.getTotalReviewCount(),
				techTube.getTechTubeUrl(),
				techTube.getTechTubeDuration().getSeconds(),
				techTube.getTechTubeThumbnailUrl(),
				techTube.getPrice(),
				techTube.getViewCount(),
				techTube.getLikes().size(),
				false,
				techTube.getCreatedAt().toLocalDate()
			);
		}

		public static Detail from(final TechTube techTube, final boolean isLike) {
			return new Detail(
				techTube.getId(),
				techTube.getWriter().getNickname(),
				techTube.getEducationLevel().getName(),
				techTube.getTechTubeEducationCategories().stream()
					.map(TechTubeEducationCategory::getEducationCategory)
					.map(EducationCategory::getName).toList(),    //TODO: 성능 최적화 시 FETCH JOIN 활용 형태 적용 고려
				techTube.getTitle(),
				techTube.getDescription(),
				techTube.getIntroduction(),
				(double)techTube.getTotalRating() / techTube.getTotalReviewCount(),
				techTube.getTotalReviewCount(),
				techTube.getTechTubeUrl(),
				techTube.getTechTubeDuration().getSeconds(),
				techTube.getTechTubeThumbnailUrl(),
				techTube.getPrice(),
				techTube.getViewCount(),
				techTube.getLikes().size(),
				isLike,
				techTube.getCreatedAt().toLocalDate()
			);
		}
	}

	@Schema(description = "TechTube 목록 조회 응답 DTO")
	public record ListInfo(
		@Schema(description = "TechTube ID", example = "1")
		Long id,    //TechTube ID

		@Schema(description = "TechTube 저자", example = "홍길동")
		String writer,    //TechTube 저자

		@Schema(description = "TechTube 제목", example = "Spring Boot")
		String title,    //TechTube 제목

		@Schema(description = "TechTube 가격", example = "10000")
		Integer price,    //TechTube 가격

		@Schema(description = "TechTube 썸네일 URL", example = "https://cotree.ssammudan.com/techtube/SpringBoot_thumbnail.png")
		String techTubeThumbnailUrl,    //TechTube 썸네일 URL

		@Schema(description = "TechTube 좋아요 수", example = "123")
		Long likeCount,    //TechTube 좋아요 수

		@Schema(description = "TechTube 등록 일자", example = "2025-01-01")
		LocalDateTime createdAt,    //TechTube 등록 일자

		@Schema(description = "로그인 회원의 좋아요 유무", example = "true")
		Boolean isLike
	) {
	}

}
