package ssammudan.cotree.domain.project.common.helper;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.project.project.entity.Project;
import ssammudan.cotree.model.project.project.repository.ProjectRepository;

/**
 * PackageName : ssammudan.cotree.domain.project.common.helper
 * FileName    : ProjectHelper
 * Author      : sangxxjin
 * Date        : 2025. 4. 9.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 9.     sangxxjin               Initial creation
 */
@Component
@RequiredArgsConstructor
public class ProjectHelper {
	private final ProjectRepository projectRepository;
	private final MemberRepository memberRepository;

	public Project getProjectOrThrow(Long projectId) {
		return projectRepository.findById(projectId)
			.orElseThrow(() -> new GlobalException(ErrorCode.PROJECT_NOT_FOUND));
	}

	public Member getMemberOrThrow(String memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
	}

	public static boolean isProjectOwner(Project project, String memberId) {
		return project.getMember().getId().equals(memberId);
	}
}
