package ssammudan.cotree.domain.project.dto;

import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;

/**
 * PackageName : ssammudan.cotree.domain.project.dto
 * FileName    : ProjectDevPositionResponse
 * Author      : sangxxjin
 * Date        : 2025. 4. 9.
 * Description : ProjectDevPositionResponse
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 9.     sangxxjin               Initial creation
 */
public record ProjectDevPositionResponse(
	Long projectDevPositionId,
	String positionName,
	int amount
) {
	public static ProjectDevPositionResponse from(ProjectDevPosition devPosition) {
		return new ProjectDevPositionResponse(
			devPosition.getId(),
			devPosition.getDevelopmentPosition().getName(),
			devPosition.getAmount()
		);
	}
}