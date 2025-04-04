package ssammudan.cotree.domain.project.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.project.dto.ProjectCreateRequest;
import ssammudan.cotree.domain.project.dto.ProjectCreateResponse;
import ssammudan.cotree.domain.project.dto.ProjectInfoResponse;
import ssammudan.cotree.domain.project.dto.ProjectListResponse;
import ssammudan.cotree.domain.project.service.ProjectServiceImpl;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.global.response.BaseResponse;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.global.response.SuccessCode;

/**
 * PackageName : ssammudan.cotree.domain.project.controller
 * FileName    : ProjectController
 * Author      : sangxxjin
 * Date        : 2025. 4. 2.
 * Description : 프로젝트 컨트롤러
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     sangxxjin               Initial creation
 */
@RestController
@RequestMapping("/api/v1/project/team")
@RequiredArgsConstructor
@Tag(name = "Project Controller", description = "프로젝트 생성 API")
public class ProjectController {
	private final ProjectServiceImpl projectServiceImpl;

	@PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다.")
	@ApiResponse(responseCode = "201", description = "프로젝트 생성 성공")
	public BaseResponse<ProjectCreateResponse> createProject(
		@RequestPart("request") @Valid ProjectCreateRequest request,
		@RequestPart(value = "projectImage", required = false) MultipartFile projectImage,
		@AuthenticationPrincipal CustomUser customUser
	) {
		String memberId = customUser.getId();
		ProjectCreateResponse response = projectServiceImpl.create(request, projectImage, memberId);

		return BaseResponse.success(SuccessCode.PROJECT_CREATE_SUCCESS, response);
	}

	@GetMapping("/{projectId}")
	@Operation(summary = "프로젝트 상세 조회", description = "특정 프로젝트의 상세 정보를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	public BaseResponse<ProjectInfoResponse> getProjectInfo(
		@PathVariable Long projectId,
		@AuthenticationPrincipal CustomUser customUser
	) {
		String memberId = (customUser != null) ? customUser.getId() : null;
		ProjectInfoResponse response = projectServiceImpl.getProjectInfo(projectId, memberId);

		return BaseResponse.success(SuccessCode.PROJECT_FETCH_SUCCESS, response);
	}

	@GetMapping("/hot/main")
	@Operation(summary = "메인페이지 HOT 프로젝트 조회", description = "메인 페이지에서 인기 있는 HOT 프로젝트 목록을 조회합니다.(조회수, 작성일 기준)")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	public BaseResponse<PageResponse<ProjectListResponse>> getHotProjectsForMain(
		@ParameterObject @PageableDefault(page = 0, size = 4, sort = {"viewCount",
			"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable
	) {
		return BaseResponse.success(SuccessCode.PROJECT_HOT_LIST_SEARCH_SUCCESS,
			projectServiceImpl.getHotProjectsForMain(pageable));
	}

	@GetMapping("/hot")
	@Operation(summary = "프로젝트 페이지 HOT 프로젝트 조회", description = "프로젝트 페이지에서 인기 있는 HOT 프로젝트 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	public BaseResponse<List<ProjectListResponse>> getHotProjectsForProject() {
		return BaseResponse.success(SuccessCode.PROJECT_HOT_LIST_SEARCH_SUCCESS,
			projectServiceImpl.getHotProjectsForProject());
	}

	@GetMapping
	@Operation(summary = "프로젝트 목록 조회", description = "프로젝트 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	public BaseResponse<PageResponse<ProjectListResponse>> getProjects(
		@RequestParam(value = "page", defaultValue = "0", required = false) int page,
		@RequestParam(value = "size", defaultValue = "12", required = false) int size,
		@RequestParam(value = "sort", defaultValue = "createdAt", required = false) String sort,
		@RequestParam(value = "techStack", required = false) List<Long> techStackIds,
		@RequestParam(value = "jobPosition", required = false) List<Long> devPositionIds
	) {
		Pageable pageable = PageRequest.of(page, size);
		return BaseResponse.success(SuccessCode.PROJECT_LIST_SEARCH_SUCCESS,
			projectServiceImpl.getProjects(pageable, techStackIds, devPositionIds, sort));
	}

}

