package ssammudan.cotree.domain.project.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.project.common.helper.ProjectHelper;
import ssammudan.cotree.domain.project.project.dto.ProjectCreateRequest;
import ssammudan.cotree.domain.project.project.dto.ProjectCreateResponse;
import ssammudan.cotree.domain.project.project.dto.ProjectDevPositionResponse;
import ssammudan.cotree.domain.project.project.dto.ProjectInfoResponse;
import ssammudan.cotree.domain.project.project.dto.ProjectListResponse;
import ssammudan.cotree.domain.project.project.dto.UpdateProjectPositionRequest;
import ssammudan.cotree.domain.project.project.service.ProjectService;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.infra.s3.S3Directory;
import ssammudan.cotree.infra.s3.S3Uploader;
import ssammudan.cotree.infra.viewcount.persistence.ViewCountStore;
import ssammudan.cotree.infra.viewcount.type.ViewCountType;
import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;
import ssammudan.cotree.model.common.developmentposition.repository.DevelopmentPositionRepository;
import ssammudan.cotree.model.common.like.entity.Like;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.common.techstack.repository.TechStackRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.devposition.repository.ProjectDevPositionRepository;
import ssammudan.cotree.model.project.membership.entity.ProjectMembership;
import ssammudan.cotree.model.project.project.entity.Project;
import ssammudan.cotree.model.project.project.repository.ProjectRepository;
import ssammudan.cotree.model.project.techstack.entity.ProjectTechStack;
import ssammudan.cotree.model.project.techstack.repository.ProjectTechStackRepository;

/**
 * PackageName : ssammudan.cotree.domain.project.service
 * FileName    : ProjectService
 * Author      : sangxxjin
 * Date        : 2025. 4. 2.
 * Description : ProjectService
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     sangxxjin          create project 구현
 * 2025. 4. 2.     sangxxjin          get project 구현
 * 2025. 4. 3.     sangxxjin          get hot project 구현, 상세 조회 시 조회수 증가 구현
 * 2025. 4. 9.     Baekgwa            프로젝트 상세 조회 시, 조회수 증가 로직 변경.
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
	private final TechStackRepository techStackRepository;
	private final DevelopmentPositionRepository developmentPositionRepository;
	private final ProjectTechStackRepository projectTechStackRepository;
	private final ProjectRepository projectRepository;
	private final ProjectDevPositionRepository projectDevPositionRepository;
	private final S3Uploader s3Uploader;
	private final ProjectHelper projectHelper;
	private final ViewCountStore viewCountStore;

	private static final int HOT_PROJECT_LIMIT = 2;

	@Override
	@Transactional
	public ProjectCreateResponse create(@Valid ProjectCreateRequest request, MultipartFile projectImage,
		String memberId) {
		Member member = projectHelper.getMemberOrThrow(memberId);
		String savedImageUrl = uploadImage(projectImage, memberId);
		List<TechStack> techStacks = getTechStackNames(request);
		List<DevelopmentPosition> devPositions = getDevelopmentPositions(request);

		Project project = createProjectEntity(member, request, savedImageUrl);
		List<ProjectTechStack> projectTechStacks = createProjectTechStacks(project, techStacks);
		List<ProjectDevPosition> projectDevPositions = createProjectDevPositions(project, devPositions, request);

		saveProjectWithRelations(project, projectTechStacks, projectDevPositions);

		return ProjectCreateResponse.from(project);
	}

	@Override
	@Transactional(readOnly = true)
	public ProjectInfoResponse getProjectInfo(Long projectId, String memberId) {
		Project project = getProjectByIdAndOptionalMemberId(projectId, memberId);

		viewCountStore.incrementViewCount(ViewCountType.PROJECT, projectId);

		Member creator = project.getMember();

		return ProjectInfoResponse.of(
			project,
			creator,
			project.getLikes().size(),
			convertDevPositions(project.getProjectDevPositions()),
			convertTechStacks(project.getProjectTechStacks()),
			isLikedByMember(project.getLikes(), memberId),
			isMemberParticipant(project.getProjectMemberships(), memberId),
			ProjectHelper.isProjectOwner(project, memberId)
		);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<ProjectListResponse> getHotProjectsForMain(Pageable pageable) {
		Page<ProjectListResponse> projects = projectRepository.findHotProjectsForMain(pageable);
		return PageResponse.of(projects);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProjectListResponse> getHotProjectsForProject() {
		//todo: 캐싱 작업
		return projectRepository.findHotProjectsForProject(HOT_PROJECT_LIMIT);
	}

	@Override
	@Transactional
	public void updateRecruitmentStatus(Long projectId, String memberId) {
		Project project = projectHelper.getProjectOrThrow(projectId);

		if (!ProjectHelper.isProjectOwner(project, memberId)) {
			throw new GlobalException(ErrorCode.PROJECT_OWNER_ONLY_CAN_UPDATE);
		}
		project.toggleIsOpen();
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<ProjectListResponse> getProjects(Pageable pageable, List<Long> techStackIds,
		List<Long> devPositionIds, String sort) {
		Page<ProjectListResponse> projects = projectRepository.findByFilters(pageable, techStackIds,
			devPositionIds, sort);
		return PageResponse.of(projects);
	}

	@Override
	@Transactional
	public void updateProjectPositionAmounts(Long projectId, String memberId,
		List<UpdateProjectPositionRequest> requests) {
		Project project = projectHelper.getProjectOrThrow(projectId);
		if (Boolean.FALSE.equals(project.getIsOpen()))
			throw new GlobalException(ErrorCode.PROJECT_NOT_OPEN);
		if (!ProjectHelper.isProjectOwner(project, memberId))
			throw new GlobalException(ErrorCode.PROJECT_OWNER_ONLY_CAN_UPDATE);

		List<ProjectDevPosition> currentPositions = projectRepository.findAllByProjectId(projectId);

		Map<Long, ProjectDevPosition> currentMap = currentPositions.stream()
			.collect(Collectors.toMap(ProjectDevPosition::getId, p -> p));

		Set<Long> requestIds = requests.stream()
			.map(UpdateProjectPositionRequest::projectDevPositionId)
			.collect(Collectors.toSet());

		for (ProjectDevPosition existing : currentPositions) {
			if (!requestIds.contains(existing.getId())) {
				projectDevPositionRepository.delete(existing);
			}
		}

		for (UpdateProjectPositionRequest req : requests) {
			ProjectDevPosition position = currentMap.get(req.projectDevPositionId());
			position.updateAmount(req.amount());
		}
	}

	private Project getProjectByIdAndOptionalMemberId(Long projectId, String memberId) {
		if (memberId != null) {
			return projectRepository.fetchProjectDetailById(projectId, memberId)
				.orElseThrow(() -> new GlobalException(ErrorCode.PROJECT_NOT_FOUND));
		}
		return projectRepository.fetchProjectDetailById(projectId)
			.orElseThrow(() -> new GlobalException(ErrorCode.PROJECT_NOT_FOUND));
	}

	private List<TechStack> getTechStackNames(ProjectCreateRequest request) {
		return techStackRepository.findByIds(request.techStackIds());
	}

	private List<DevelopmentPosition> getDevelopmentPositions(ProjectCreateRequest request) {
		return developmentPositionRepository.findByIds(request.recruitmentPositions().keySet());
	}

	private List<ProjectDevPositionResponse> convertDevPositions(Set<ProjectDevPosition> devPositions) {
		return devPositions.stream()
			.map(ProjectDevPositionResponse::from)
			.toList();
	}

	private List<String> convertTechStacks(Set<ProjectTechStack> techStacks) {
		return techStacks.stream()
			.map(ts -> ts.getTechStack().getName())
			.toList();
	}

	private boolean isLikedByMember(Set<Like> likes, String memberId) {
		return memberId != null &&
			likes.stream().anyMatch(like -> like.getMember().getId().equals(memberId));
	}

	private boolean isMemberParticipant(Set<ProjectMembership> memberships, String memberId) {
		return memberId != null &&
			memberships.stream().anyMatch(m -> m.getMember().getId().equals(memberId));
	}

	private String uploadImage(MultipartFile image, String memberId) {
		if (image == null)
			return null;
		return s3Uploader.upload(memberId, image, S3Directory.PROJECT).getSaveUrl();
	}

	private Project createProjectEntity(Member member, ProjectCreateRequest request, String imageUrl) {
		return Project.create(member, request, imageUrl);
	}

	private List<ProjectTechStack> createProjectTechStacks(Project project, List<TechStack> techStacks) {
		return techStacks.stream()
			.map(techStack -> ProjectTechStack.create(project, techStack))
			.toList();
	}

	private List<ProjectDevPosition> createProjectDevPositions(Project project, List<DevelopmentPosition> devPositions,
		ProjectCreateRequest request) {
		return devPositions.stream()
			.map(devPosition -> ProjectDevPosition.create(
				project, devPosition, request.recruitmentPositions().get(devPosition.getId())))
			.toList();
	}

	private void saveProjectWithRelations(Project project, List<ProjectTechStack> stacks,
		List<ProjectDevPosition> devPositions) {
		projectRepository.save(project);
		projectTechStackRepository.saveAll(stacks);
		projectDevPositionRepository.saveAll(devPositions);
	}

}
