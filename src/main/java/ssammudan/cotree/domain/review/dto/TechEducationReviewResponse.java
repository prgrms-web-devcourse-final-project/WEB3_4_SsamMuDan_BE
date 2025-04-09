package ssammudan.cotree.domain.review.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ssammudan.cotree.model.education.type.EducationType;
import ssammudan.cotree.model.review.review.entity.TechEducationReview;

/**
 * PackageName : ssammudan.cotree.domain.review.review.dto
 * FileName    : TechEducationReviewResponse
 * Author      : loadingKKamo21
 * Date        : 25. 4. 1.
 * Description : TechEducationReview 응답 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     loadingKKamo21       Initial creation
 */
@Schema(description = "TechEducationReview 응답 DTO")
public class TechEducationReviewResponse {

	@Schema(description = "TechEducationReview 조회 DTO")
	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Detail {
		@Schema(description = "TechEducation 리뷰 ID", example = "1")
		long id;
		@Schema(description = "TechEducation 타입: TECH_TUBE, TECH_BOOK", example = "TECH_TUBE")
		EducationType techEducationType;
		@Schema(description = "TechEducationItem ID: TechBook ID, TechTube ID", example = "1")
		long itemId;
		@Schema(description = "TechEducation 리뷰 작성자", example = "홍길동")
		String reviewer;
		@Schema(description = "TechEducation 리뷰 작성자 프로필 이미지 URL")
		String profileImageUrl;
		@Schema(description = "TechEducation 리뷰 평점", example = "5")
		int rating;
		@Schema(description = "TechEducation 리뷰 내용", example = "스프링 부트의 다양한 기능을 활용하는 방법을 배울 수 있었습니다.")
		String content;
		@Schema(description = "TechEducation 리뷰 작성일자", example = "2025-01-01")
		LocalDateTime createdAt;

		public Detail(
			final long id,
			final long educationTypeId,
			final long itemId,
			final String reviewer,
			final String profileImageUrl,
			final int rating,
			final String content,
			final LocalDateTime createdAt
		) {
			this.id = id;
			this.techEducationType = EducationType.getTechEducationType(educationTypeId);
			this.itemId = itemId;
			this.reviewer = reviewer;
			this.profileImageUrl = profileImageUrl;
			this.rating = rating;
			this.content = content;
			this.createdAt = createdAt;
		}

		public static Detail from(final TechEducationReview techEducationReview) {
			return new Detail(
				techEducationReview.getId(),
				EducationType.getTechEducationType(techEducationReview.getTechEducationType().getId()),
				techEducationReview.getItemId(),
				techEducationReview.getReviewer().getNickname(),
				techEducationReview.getReviewer().getProfileImageUrl(),
				techEducationReview.getRating(),
				techEducationReview.getContent(),
				techEducationReview.getCreatedAt()
			);
		}
	}

}
