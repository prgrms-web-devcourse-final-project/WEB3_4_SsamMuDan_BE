package ssammudan.cotree.domain.review.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import ssammudan.cotree.model.review.review.entity.TechEducationReview;
import ssammudan.cotree.model.review.reviewtype.type.TechEducationReviewType;

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
	public record Detail(
		@Schema(description = "TechEducation 리뷰 ID", example = "1")
		long id,                    //TechEducationReview ID
		@Schema(description = "TechEducation 타입: TECH_TUBE, TECH_BOOK", example = "TECH_TUBE")
		TechEducationReviewType techEducationType,    //TechEducationType: TECH_BOOK, TECH_TUBE
		@Schema(description = "TechEducationItem ID: TechBook ID, TechTube ID", example = "1")
		long itemId,    //Item ID: TechBook ID, TechTube ID
		@Schema(description = "TechEducation 리뷰 작성자", example = "홍길동")
		String reviewer,    //리뷰 작성자 닉네임
		@Schema(description = "TechEducation 리뷰 평점", example = "5")
		int rating,    //리뷰 평점
		@Schema(description = "TechEducation 리뷰 내용", example = "스프링 부트의 다양한 기능을 활용하는 방법을 배울 수 있었습니다.")
		String content,    //리뷰 내용,
		@Schema(description = "TechEducation 리뷰 작성일자", example = "2025-01-01")
		LocalDate createdAt    //리뷰 작성일자
	) {
		public static Detail from(final TechEducationReview techEducationReview) {
			return new Detail(
				techEducationReview.getId(),
				TechEducationReviewType.getTechEducationType(techEducationReview.getTechEducationType().getId()),
				techEducationReview.getItemId(),
				techEducationReview.getReviewer().getNickname(),
				techEducationReview.getRating(),
				techEducationReview.getContent(),
				techEducationReview.getCreatedAt().toLocalDate()
			);
		}
	}

}
