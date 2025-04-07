package ssammudan.cotree.domain.project.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import ssammudan.cotree.model.common.like.repository.LikeRepository;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.common.techstack.repository.TechStackRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.devposition.repository.ProjectDevPositionRepository;
import ssammudan.cotree.model.project.membership.repository.ProjectMembershipRepository;
import ssammudan.cotree.model.project.membership.type.ProjectMembershipStatus;
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
	private final LikeRepository likeRepository;
	private final ProjectMembershipRepository projectMembershipRepository;
	private final ProjectRepositoryImpl projectRepositoryImpl;

	@Override
	@Transactional
	public ProjectCreateResponse create(@Valid ProjectCreateRequest request, MultipartFile projectImage,
		String memberId) {

		String savedImageUrl = Optional.ofNullable(projectImage)
			.map(img -> s3Uploader.upload(memberId, img, S3Directory.PROJECT).getSaveUrl())
			.orElse(null);

		List<TechStack> techStacks = getTechStackNames(request);
		List<DevelopmentPosition> devPositions = getDevelopmentPositions(request);

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

		Project project = Project.create(member, request, savedImageUrl);

		List<ProjectTechStack> projectTechStacks = techStacks.stream()
			.map(techStack -> ProjectTechStack.create(project, techStack))
			.toList();

		List<ProjectDevPosition> projectDevPositions = devPositions.stream()
			.map(devPosition -> ProjectDevPosition.create(project, devPosition,
				request.recruitmentPositions().get(devPosition.getId())))
			.toList();

		projectRepository.save(project);
		projectTechStackRepository.saveAll(projectTechStacks);
		projectDevPositionRepository.saveAll(projectDevPositions);

		return ProjectCreateResponse.from(project);
	}

	@Transactional(readOnly = true)
	public ProjectInfoResponse getProjectInfo(Long projectId, String memberId) {
		Project project = getProjectByIdAndOptionalMemberId(projectId, memberId);
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

	// 기술 스택 이름 조회
	private List<String> getTechStackNames(Long projectId) {
		return projectTechStackRepository.findByProjectId(projectId).stream()
			.map(projectTechStack -> projectTechStack.getTechStack().getName())
			.toList();
	}

	private List<TechStack> getTechStackNames(ProjectCreateRequest request) {
		return techStackRepository.findByIds(request.techStackIds());
	}

	private List<DevelopmentPosition> getDevelopmentPositions(ProjectCreateRequest request) {
		return developmentPositionRepository.findByIds(request.recruitmentPositions().keySet());
	}

	// 좋아요 개수
	private long getLikeCount(Long projectId) {
		return likeRepository.countByProjectId(projectId);
	}

	// 좋아요 누른 멤버 확인
	private boolean isLikedByMember(Long projectId, String memberId) {
		return memberId != null && likeRepository.existsByProjectIdAndMemberId(projectId, memberId);
	}

	// 이미 참가한 프로젝트 여부 확인
	private boolean isMemberParticipant(Long projectId, String memberId) {
		return memberId != null &&
			projectMembershipRepository.existsByProjectIdAndMemberIdAndProjectMembershipStatus(
				projectId, memberId, ProjectMembershipStatus.JOINED
			);
	}

	// 프로젝트 작성자 여부 확인
	private boolean isProjectOwner(Project project, String memberId) {
		return project.getMember().getId().equals(memberId);
	}
}
