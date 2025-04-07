package ssammudan.cotree.domain.project.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
	private final LikeRepository likeRepository;
	private final ProjectMembershipRepository projectMembershipRepository;
	private final ProjectViewService projectViewService;

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
		Project project = projectRepository.findById(projectId)
			.orElseThrow(() -> new GlobalException(ErrorCode.PROJECT_NOT_FOUND));

		projectViewService.incrementViewCount(projectId);

		Member creator = project.getMember();

		return ProjectInfoResponse.of(
			project,
			creator,
			getLikeCount(projectId),
			getDevPositionsInfo(projectId),
			getTechStackNames(projectId),
			isLikedByMember(projectId, memberId),
			isMemberParticipant(projectId, memberId),
			isProjectOwner(project, memberId)
		);
	}

	@Transactional(readOnly = true)
	public PageResponse<ProjectListResponse> getHotProjectsForMain(Pageable pageable) {
		Page<Project> projects = projectRepository.findByIsOpenTrue(pageable);
		List<ProjectListResponse> hotPojectList = projects.stream()
			.map(this::toProjectResponse)
			.toList();

		Page<ProjectListResponse> hotProjectPage = new PageImpl<>(hotPojectList, pageable, projects.getTotalElements());
		return PageResponse.of(hotProjectPage);
	}

	@Transactional(readOnly = true)
	public List<ProjectListResponse> getHotProjectsForProject() {
		//todo: 캐싱 작업
		return projectRepository.findTop2ByIsOpenTrueOrderByViewCountDescCreatedAtDesc().stream()
			.map(this::toProjectResponse)
			.toList();
	}

	@Transactional(readOnly = true)
	public PageResponse<ProjectListResponse> getProjects(Pageable pageable, List<Long> techStackIds,
		List<Long> devPositionIds,
		String sort) {
		Page<Project> projects = projectRepository.findByFilters(pageable, techStackIds, devPositionIds, sort);
		List<ProjectListResponse> content = projects.getContent().stream()
			.map(this::toProjectResponse)
			.toList();

		Page<ProjectListResponse> page = new PageImpl<>(content, pageable, projects.getTotalElements());
		return PageResponse.of(page);
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

	// 모집분야명, 인원수 조회
	private List<Map<String, Integer>> getDevPositionsInfo(Long projectId) {
		return projectDevPositionRepository.findByProjectId(projectId).stream()
			.map(projectDevPosition -> Map.of(
				projectDevPosition.getDevelopmentPosition().getName(),
				projectDevPosition.getAmount()
			))
			.toList();
	}

	// 기술 스택 이름 조회
	private List<String> getTechStackNames(Long projectId) {
		return projectTechStackRepository.findByProjectId(projectId).stream()
			.map(projectTechStack -> projectTechStack.getTechStack().getName())
			.toList();
	}

	// 프로젝트 데이터가공
	private ProjectListResponse toProjectResponse(Project project) {
		List<String> techStackImageUrls = getTechStackImageUrls(project.getId());
		long likeCount = likeRepository.countByProjectId(project.getId());
		Member member = memberRepository.findById(project.getMember().getId()).orElse(null);
		int recruitmentCount = getRecruitmentCount(project.getId());

		return ProjectListResponse.from(project, techStackImageUrls, likeCount, member, recruitmentCount);
	}

	// 프로젝트에 해당하는 techstack들의 이미지들 가져오는 메서드
	private List<String> getTechStackImageUrls(Long projectId) {
		List<Long> techStackIds = projectTechStackRepository.findByProjectId(projectId).stream()
			.map(projectTechStack -> projectTechStack.getTechStack().getId())
			.toList();

		return techStackRepository.findAllById(techStackIds).stream()
			.map(TechStack::getImageUrl)
			.toList();
	}

	// 프로젝트 총 모집인원 계산 메서드
	private int getRecruitmentCount(Long projectId) {
		return projectDevPositionRepository.findByProjectId(projectId).stream()
			.mapToInt(ProjectDevPosition::getAmount)
			.sum();
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
