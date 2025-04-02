package ssammudan.cotree.domain.common.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.common.dto.LikeRequest;
import ssammudan.cotree.domain.common.like.LikeService;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.common.controller
 * FileName    : LikeController
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : 좋아요(Like) 컨트롤러
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 */
@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
@Tag(name = "Like Controller", description = "Like API")
public class LikeController {

	private final LikeService likeService;

	@PostMapping
	@Operation(summary = "Like(좋아요) 신규 추가", description = "likeType(좋아요 대상 타입)과 itemId(대상 ID)를 사용해 좋아요 추가")
	@ApiResponse(responseCode = "200", description = "좋아요 추가 성공")
	@SecurityRequirement(name = "bearerAuth")
	public BaseResponse<Void> createLike(
		@RequestBody @Valid LikeRequest.Create requestDto/*, @AuthenticationPrincipal UserDetails userDetails*/
	) {
		//String memberId = ((CustomUser)userDetails).getId();
		String memberId = UUID.randomUUID().toString();    //TODO: 시큐리티 인증 객체 활용 방법 고민 필요
		Long id = likeService.createLike(memberId, requestDto);
		return BaseResponse.success(SuccessCode.LIKE_ADD_SUCCESS);
	}

}
