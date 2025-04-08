package ssammudan.cotree.domain.category.service;

import java.util.List;

import ssammudan.cotree.domain.category.dto.CommunityCategoryResponse;
import ssammudan.cotree.domain.category.dto.EducationCategoryResponse;
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
 * 2025. 4. 7.     Baekgwa            교육 카테고리 조회 추가
 * 2025. 4. 8.     Baekgwa            커뮤니티 글 카테고리 조회 추가
 */
public interface CategoryService {

	List<TechStackResponse> findSkills();

	List<PositionResponse> findPositions();

	List<EducationCategoryResponse> findEducationCategoryList();

	List<CommunityCategoryResponse> findCommunityCategoryList();
}
