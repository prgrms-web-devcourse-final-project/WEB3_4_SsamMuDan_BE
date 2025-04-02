package ssammudan.cotree.domain.review.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
	public record Detail(
		@Schema(description = "TechEducationReview ID", example = "1")
		long id,                    //TechEducationReview ID
		@Schema(description = "TechEducationType ID: TechBook, TechTube", example = "1")
		long techEducationTypeId,    //TechEducationType ID: TechBook, TechTube
		@Schema(description = "TechEducationItem ID: TechBook ID, TechTube ID", example = "1")
		long itemId,
		@Schema(description = "TechEducationReview 작성자", example = "홍길동")
		String reviewer,
		@Schema(description = "TechEducationReview 평점", example = "5")
		int rating,
		@Schema(description = "TechEducationReview 내용", example = "스프링 부트의 다양한 기능을 활용하는 방법을 배울 수 있었습니다.")
		String content
	) {
		public static Detail from(final TechEducationReview techEducationReview) {
			return new Detail(
				techEducationReview.getId(),
				techEducationReview.getTechEducationType().getId(),
				techEducationReview.getItemId(),
				techEducationReview.getReviewer().getNickname(),
				techEducationReview.getRating(),
				techEducationReview.getContent()
			);
		}
	}

}
