package ssammudan.cotree.domain.resume.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.resume.dto.ResumeCreateRequest;
import ssammudan.cotree.domain.resume.dto.ResumeCreateResponse;
import ssammudan.cotree.domain.resume.dto.ResumeDetailResponse;
import ssammudan.cotree.domain.resume.service.ResumeService;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.resume.controller
 * FileName    : ResumeController
 * Author      : kwak
 * Date        : 2025. 3. 28.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 28.     kwak               Initial creation
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "ResumeController", description = "이력서 컨트롤러")
@RequestMapping("/api/v1/recruitment/resume")
public class ResumeController {

	private final ResumeService resumeService;

	@Operation(summary = "이력서 작성", description = "이력서를 작성합니다.")
	@PostMapping
	public BaseResponse<ResumeCreateResponse> register(
		@Valid @RequestBody ResumeCreateRequest request,
		@RequestParam("id") String dummyMemberId
	) {
		// todo 추후 수정
		ResumeCreateResponse response = resumeService.register(request, dummyMemberId);
		return BaseResponse.success(SuccessCode.RESUME_CREATE_SUCCESS, response);
	}

	@Operation(summary = "이력서 상세 조회", description = "이력서 상세를 조회합니다.")
	@GetMapping("/{id}")
	public BaseResponse<ResumeDetailResponse> detail(
		@PathVariable(name = "id") Long id
	) {
		ResumeDetailResponse response = resumeService.detail(id);
		return BaseResponse.success(SuccessCode.RESUME_DETAIL_SUCCESS, response);
	}
}
