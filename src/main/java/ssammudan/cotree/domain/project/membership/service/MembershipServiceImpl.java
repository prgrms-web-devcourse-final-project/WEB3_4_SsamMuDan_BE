package ssammudan.cotree.domain.project.membership.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.project.common.helper.ProjectHelper;
import ssammudan.cotree.domain.project.membership.dto.MembershipResponse;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.project.membership.entity.ProjectMembership;
import ssammudan.cotree.model.project.membership.repository.ProjectMembershipRepository;
import ssammudan.cotree.model.project.project.entity.Project;

/**
 * PackageName : ssammudan.cotree.domain.project.membership.service
 * FileName    : MembershipServiceImpl
 * Author      : sangxxjin
 * Date        : 2025. 4. 9.
 * Description : MembershipServiceImpl
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 9.     sangxxjin               Initial creation
 */
@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

	private final ProjectMembershipRepository projectMembershipRepository;
	private final ProjectHelper projectHelper;

	@Override
	@Transactional
	public void applyForProject(Long projectId, String memberId) {
		Project project = projectHelper.getProjectOrThrow(projectId);
		if (Boolean.FALSE.equals(project.getIsOpen()))
			throw new GlobalException(ErrorCode.PROJECT_NOT_OPEN);
		if (ProjectHelper.isProjectOwner(project, memberId))
			throw new GlobalException(ErrorCode.PROJECT_OWNER_CANNOT_JOIN);
		if (isMemberAlreadyApplied(projectId, memberId))
			throw new GlobalException(ErrorCode.PROJECT_MEMBER_ALREADY_EXISTS);
		ProjectMembership projectMembership = ProjectMembership.builderForApply(project,
			projectHelper.getMemberOrThrow(memberId));
		projectMembershipRepository.save(projectMembership);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MembershipResponse> getMemberships(Long projectId, String memberId) {
		Project project = projectHelper.getProjectOrThrow(projectId);
		if (!ProjectHelper.isProjectOwner(project, memberId))
			throw new GlobalException(ErrorCode.PROJECT_OWNER_ONLY_CAN_VIEW);
		return projectMembershipRepository.findMembershipResponsesByProjectId(projectId);
	}

	private boolean isMemberAlreadyApplied(Long projectId, String memberId) {
		return projectMembershipRepository.existsByProjectIdAndMemberId(projectId, memberId);
	}
}
