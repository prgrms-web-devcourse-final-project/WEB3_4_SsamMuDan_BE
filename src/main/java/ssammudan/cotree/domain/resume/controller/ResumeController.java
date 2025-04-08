package ssammudan.cotree.domain.resume.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.resume.dto.ResumeCreateRequest;
import ssammudan.cotree.domain.resume.dto.ResumeCreateResponse;
import ssammudan.cotree.domain.resume.dto.ResumeDetailResponse;
import ssammudan.cotree.domain.resume.dto.ResumeResponse;
import ssammudan.cotree.domain.resume.dto.SearchResumeSort;
import ssammudan.cotree.domain.resume.service.ResumeService;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.PageResponse;
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
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BaseResponse<ResumeCreateResponse> register(
			@RequestPart(value = "request") @Valid ResumeCreateRequest request,
			@RequestPart(value = "resumeImage", required = false) MultipartFile resumeImage,
			@AuthenticationPrincipal CustomUser customUser
	) {
		ResumeCreateResponse response = resumeService.register(request, customUser.getId(), resumeImage);
		return BaseResponse.success(SuccessCode.RESUME_CREATE_SUCCESS, response);
	}

	@Operation(summary = "이력서 상세 조회", description = "이력서 상세를 조회합니다.")
	@GetMapping("/{id}")
	public BaseResponse<ResumeDetailResponse> detail(
			@PathVariable(name = "id") Long id
	) {
		return BaseResponse.success(SuccessCode.RESUME_DETAIL_SUCCESS, resumeService.detail(id));
	}

	@Operation(summary = "채용 지원 조회", description = "채용을 위한 이력서들을 조회합니다.")
	@GetMapping
	public BaseResponse<PageResponse<ResumeResponse>> getResumeList(
		@RequestParam(value = "page", defaultValue = "0", required = false) int page,
		@RequestParam(value = "size", defaultValue = "16", required = false) int size,
		@RequestParam(value = "position", required = false) List<Long> positionIds,
		@RequestParam(value = "skill", required = false) List<Long> skillIds,
		@RequestParam(value = "startY", defaultValue = "0", required = false) Integer startYear,
		@RequestParam(value = "endY", defaultValue = "10", required = false) Integer endYear,
		@RequestParam(value = "sort", defaultValue = "LATEST", required = false) SearchResumeSort sort

	) {
		return BaseResponse.success(SuccessCode.RESUME_LIST_SUCCESS,
			resumeService.getResumeList(page, size, positionIds, skillIds, startYear, endYear, sort));
	}
}
