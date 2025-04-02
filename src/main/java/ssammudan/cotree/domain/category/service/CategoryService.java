package ssammudan.cotree.domain.category.service;

import java.util.List;

import ssammudan.cotree.domain.category.dto.PositionResponse;
import ssammudan.cotree.domain.category.dto.TechStackResponse;

/**
 * PackageName : ssammudan.cotree.domain.category.service
 * FileName    : CategoryService
 * Author      : kwak
 * Date        : 2025. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     kwak               Initial creation
 */
public interface CategoryService {

	List<TechStackResponse> findSkills();

	List<PositionResponse> findPositions();
}
