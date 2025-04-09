package ssammudan.cotree.domain.project.project.dto;

import lombok.AccessLevel;
import lombok.Builder;
import ssammudan.cotree.model.project.project.entity.Project;

/**
 * PackageName : ssammudan.cotree.domain.project.dto
 * FileName    : ProjectCreateResponse
 * Author      : sangxxjin
 * Date        : 2025. 4. 2.
 * Description : 생성한 project의 id만 넘김
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     sangxxjin               Initial creation
 */
@Builder(access = AccessLevel.PRIVATE)
public record ProjectCreateResponse(
	Long projectId
) {
	public static ProjectCreateResponse from(Project project) {
		return ProjectCreateResponse.builder()
			.projectId(project.getId())
			.build();
	}
}
