package ssammudan.cotree.domain.category.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.category.dto.PositionResponse;
import ssammudan.cotree.domain.category.dto.TechStackResponse;
import ssammudan.cotree.model.common.developmentposition.repository.DevelopmentPositionRepository;
import ssammudan.cotree.model.common.techstack.repository.TechStackRepository;

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
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final TechStackRepository techStackRepository;
	private final DevelopmentPositionRepository developmentPositionRepository;

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
	@Transactional
	public List<PositionResponse> findPositions() {
		return developmentPositionRepository.findAll().stream()
			.map(developmentPosition ->
				PositionResponse.of(developmentPosition.getId(), developmentPosition.getName()))
			.sorted(Comparator.comparing(PositionResponse::id))
			.toList();
	}
}
