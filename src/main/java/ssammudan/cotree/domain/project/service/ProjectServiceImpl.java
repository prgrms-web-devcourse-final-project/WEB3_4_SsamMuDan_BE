package ssammudan.cotree.domain.project.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.project.dto.ProjectCreateRequest;
import ssammudan.cotree.domain.project.dto.ProjectCreateResponse;
import ssammudan.cotree.domain.project.dto.ProjectInfoResponse;
import ssammudan.cotree.domain.project.dto.ProjectListResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.infra.s3.S3Directory;
import ssammudan.cotree.infra.s3.S3Uploader;
import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;
import ssammudan.cotree.model.common.developmentposition.repository.DevelopmentPositionRepository;
import ssammudan.cotree.model.common.like.entity.Like;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.common.techstack.repository.TechStackRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.devposition.repository.ProjectDevPositionRepository;
import ssammudan.cotree.model.project.membership.entity.ProjectMembership;
import ssammudan.cotree.model.project.project.entity.Project;
import ssammudan.cotree.model.project.project.repository.ProjectRepository;
import ssammudan.cotree.model.project.project.repository.ProjectRepositoryImpl;
import ssammudan.cotree.model.project.techstack.entity.ProjectTechStack;
import ssammudan.cotree.model.project.techstack.repository.ProjectTechStackRepository;

/**
 * PackageName : ssammudan.cotree.domain.project.service
 * FileName    : ProjectService
 * Author      : sangxxjin
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     sangxxjin          create project 구현
 * 2025. 4. 2.     sangxxjin          get project 구현
 * 2025. 4. 3.     sangxxjin          get hot project 구현, 상세 조회 시 조회수 증가 구현
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
	private final TechStackRepository techStackRepository;
	private final DevelopmentPositionRepository developmentPositionRepository;
	private final MemberRepository memberRepository;
	private final ProjectTechStackRepository projectTechStackRepository;
	private final ProjectRepository projectRepository;
	private final ProjectDevPositionRepository projectDevPositionRepository;
	private final S3Uploader s3Uploader;
	private final ProjectRepositoryImpl projectRepositoryImpl;
	private final ProjectViewService projectViewService;

	@Override
	@Transactional
	public ProjectCreateResponse create(@Valid ProjectCreateRequest request, MultipartFile projectImage,
		String memberId) {
		Member member = getMemberOrThrow(memberId);
		String savedImageUrl = uploadImageIfPresent(projectImage, memberId);
		List<TechStack> techStacks = getTechStackNames(request);
		List<DevelopmentPosition> devPositions = getDevelopmentPositions(request);

		Project project = createProjectEntity(member, request, savedImageUrl);
		List<ProjectTechStack> projectTechStacks = createProjectTechStacks(project, techStacks);
		List<ProjectDevPosition> projectDevPositions = createProjectDevPositions(project, devPositions, request);

		saveProjectWithRelations(project, projectTechStacks, projectDevPositions);

		return ProjectCreateResponse.from(project);
	}

	@Transactional(readOnly = true)
	public ProjectInfoResponse getProjectInfo(Long projectId, String memberId) {
		Project project = getProjectByIdAndOptionalMemberId(projectId, memberId);

		projectViewService.incrementViewCount(projectId);

		Member creator = project.getMember();

		return ProjectInfoResponse.of(
			project,
			creator,
			project.getLikes().size(),
			convertDevPositions(project.getProjectDevPositions()),
			convertTechStacks(project.getProjectTechStacks()),
			isLikedByMember(project.getLikes(), memberId),
			isMemberParticipant(project.getProjectMemberships(), memberId),
			isProjectOwner(project, memberId)
		);
	}

	@Transactional(readOnly = true)
	public PageResponse<ProjectListResponse> getHotProjectsForMain(Pageable pageable) {
		Page<ProjectListResponse> projects = projectRepository.findHotProjectsForMain(pageable);
		return PageResponse.of(projects);
	}

	@Transactional(readOnly = true)
	public List<ProjectListResponse> getHotProjectsForProject() {
		//todo: 캐싱 작업
		return projectRepository.findHotProjectsForProject(2);
	}

	@Override
	@Transactional
	public void updateRecruitmentStatus(Long projectId, String memberId) {
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new GlobalException(ErrorCode.PROJECT_NOT_FOUND));

		if (!isProjectOwner(project, memberId)) {
			throw new GlobalException(ErrorCode.PROJECT_FORBIDDEN);
		}
		project.toggleIsOpen();
	}

	@Transactional(readOnly = true)
	public PageResponse<ProjectListResponse> getProjects(Pageable pageable, List<Long> techStackIds,
		List<Long> devPositionIds, String sort) {
		Page<ProjectListResponse> projects = projectRepositoryImpl.findByFilters(pageable, techStackIds,
			devPositionIds, sort);
		return PageResponse.of(projects);
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

	private List<Map<String, Integer>> convertDevPositions(Set<ProjectDevPosition> devPositions) {
		return devPositions.stream()
			.map(devPos -> Map.of(devPos.getDevelopmentPosition().getName(), devPos.getAmount()))
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

	private boolean isProjectOwner(Project project, String memberId) {
		return project.getMember().getId().equals(memberId);
	}

	private Member getMemberOrThrow(String memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private String uploadImageIfPresent(MultipartFile image, String memberId) {
		return Optional.ofNullable(image)
			.map(img -> s3Uploader.upload(memberId, img, S3Directory.PROJECT).getSaveUrl())
			.orElse(null);
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
