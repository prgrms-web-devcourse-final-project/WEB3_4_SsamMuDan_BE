package ssammudan.cotree.domain.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ssammudan.cotree.model.common.like.type.LikeType;

/**
 * PackageName : ssammudan.cotree.domain.common.dto
 * FileName    : LikeRequest
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : 좋아요 요청 DTO
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21               Initial creation
 */
@Schema(description = "Like(좋아요) 요청 DTO")
public class LikeRequest {

	@Schema(description = "Like(좋아요) 생성 요청 DTO")
	public record Create(
		@NotNull
		@Schema(description = "Like 타입: TECH_TUBE, TECH_BOOK, PROJECT, COMMUNITY", example = "COMMUNITY")
		LikeType likeType,
		@NotNull
		@Schema(description = "Like 대상 ID: TechTube ID, TechBook ID, Project ID, Community ID", example = "1")
		Long itemId
	) {
	}

}
