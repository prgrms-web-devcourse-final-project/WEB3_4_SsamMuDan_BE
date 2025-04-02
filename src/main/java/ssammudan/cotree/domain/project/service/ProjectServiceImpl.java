package ssammudan.cotree.domain.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.project.dto.ProjectCreateRequest;
import ssammudan.cotree.domain.project.dto.ProjectCreateResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.infra.s3.S3Directory;
import ssammudan.cotree.infra.s3.S3Uploader;
import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;
import ssammudan.cotree.model.common.developmentposition.repository.DevelopmentPositionRepository;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.common.techstack.repository.TechStackRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.devposition.repository.ProjectDevPositionRepository;
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

	@Override
	@Transactional
	public ProjectCreateResponse create(@Valid ProjectCreateRequest request, MultipartFile projectImage,
		String memberId) {

		String savedImageUrl = Optional.ofNullable(projectImage)
			.map(img -> s3Uploader.upload(memberId, img, S3Directory.PROJECT).getSaveUrl())
			.orElse(null);

		List<TechStack> techStacks = getTechStacks(request);
		List<DevelopmentPosition> devPositions = getDevelopmentPositions(request);

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));

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

	private List<TechStack> getTechStacks(ProjectCreateRequest request) {
		return techStackRepository.findByIds(request.techStackIds());
	}

	private List<DevelopmentPosition> getDevelopmentPositions(ProjectCreateRequest request) {
		return developmentPositionRepository.findByIds(request.recruitmentPositions().keySet());
	}
}
