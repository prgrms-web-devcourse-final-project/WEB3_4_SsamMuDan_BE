package ssammudan.cotree.domain.project.project.dto;

/**
 * PackageName : ssammudan.cotree.domain.project.dto
 * FileName    : UpdateProjectPositionRequest
 * Author      : sangxxjin
 * Date        : 2025. 4. 9.
 * Description : UpdateProjectPositionRequest
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 9.     sangxxjin               Initial creation
 */
public record UpdateProjectPositionRequest(
	Long projectDevPositionId,
	Integer amount
) {
}
