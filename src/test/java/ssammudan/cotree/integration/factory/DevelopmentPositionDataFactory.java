package ssammudan.cotree.integration.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;
import ssammudan.cotree.model.common.developmentposition.repository.DevelopmentPositionRepository;

/**
 * PackageName : ssammudan.cotree.integration.factory
 * FileName    : DevelopmentPositionDataFactory
 * Author      : Baekgwa
 * Date        : 2025-04-14
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-14     Baekgwa               Initial creation
 */
@Component
@RequiredArgsConstructor
public class DevelopmentPositionDataFactory {

	private final EntityManager em;
	private final DevelopmentPositionRepository developmentPositionRepository;

	public List<DevelopmentPosition> createAndSaveDevelopmentPosition() {
		List<DevelopmentPosition> developmentPositionList = new ArrayList<>();

		developmentPositionList.add(DevelopmentPosition.create("프론트엔드"));
		developmentPositionList.add(DevelopmentPosition.create("백엔드"));
		developmentPositionList.add(DevelopmentPosition.create("풀스택"));

		List<DevelopmentPosition> saveDevPos = developmentPositionRepository.saveAll(developmentPositionList);
		em.flush();
		em.clear();
		return saveDevPos;
	}
}
