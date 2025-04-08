package ssammudan.cotree.domain.category.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.category.dto.CommunityCategoryResponse;
import ssammudan.cotree.domain.category.dto.EducationCategoryResponse;
import ssammudan.cotree.domain.category.dto.PositionResponse;
import ssammudan.cotree.domain.category.dto.TechStackResponse;
import ssammudan.cotree.model.common.developmentposition.repository.DevelopmentPositionRepository;
import ssammudan.cotree.model.common.techstack.repository.TechStackRepository;
import ssammudan.cotree.model.community.category.repository.CommunityCategoryRepository;
import ssammudan.cotree.model.education.category.repository.EducationCategoryRepository;

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
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final TechStackRepository techStackRepository;
	private final DevelopmentPositionRepository developmentPositionRepository;
	private final EducationCategoryRepository educationCategoryRepository;
	private final CommunityCategoryRepository communityCategoryRepository;

	@Override
	@Transactional(readOnly = true)
	public List<TechStackResponse> findSkills() {
		return techStackRepository.findAll().stream()
			.map(techStack ->
				TechStackResponse.of(techStack.getId(), techStack.getName(), techStack.getImageUrl()))
			.sorted(Comparator.comparing(TechStackResponse::id))
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PositionResponse> findPositions() {
		return developmentPositionRepository.findAll().stream()
			.map(developmentPosition ->
				PositionResponse.of(developmentPosition.getId(), developmentPosition.getName()))
			.sorted(Comparator.comparing(PositionResponse::id))
			.toList();
	}

	@Transactional(readOnly = true)
	@Override
	public List<EducationCategoryResponse> findEducationCategoryList() {
		return educationCategoryRepository.findAll()
			.stream().map(EducationCategoryResponse::of)
			.sorted(Comparator.comparing(EducationCategoryResponse::id))
			.toList();
	}

	@Transactional(readOnly = true)
	@Override
	public List<CommunityCategoryResponse> findCommunityCategoryList() {
		return communityCategoryRepository.findAll()
			.stream().map(CommunityCategoryResponse::of)
			.sorted(Comparator.comparing(CommunityCategoryResponse::id))
			.toList();
	}
}
