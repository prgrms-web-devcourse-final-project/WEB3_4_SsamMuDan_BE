package ssammudan.cotree.domain.project.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.project.dto.ProjectCreateRequest;
import ssammudan.cotree.domain.project.dto.ProjectCreateResponse;
import ssammudan.cotree.domain.project.service.ProjectServiceImpl;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.project.controller
 * FileName    : ProjectController
 * Author      : sangxxjin
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     sangxxjin               Initial creation
 */
@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
@Tag(name = "Proejct Controller", description = "프로젝트 생성 API")
public class ProjectController {
	private final ProjectServiceImpl projectServiceImpl;

	@PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다.")
	@ApiResponse(responseCode = "201", description = "프로젝트 생성 성공")
	public BaseResponse<ProjectCreateResponse> createProject(
		@RequestPart("request") @Valid ProjectCreateRequest request,
		@RequestPart(value = "projectImageUrl", required = false) MultipartFile projectImage) {
		//todo: 현재 회원 정보 하드 코딩 -> 로그인한 member 넘기게 수정 예정
		ProjectCreateResponse response = projectServiceImpl.create(request, projectImage, "1");

		return BaseResponse.success(SuccessCode.PROJECT_CREATE_SUCCESS, response);
	}
}

