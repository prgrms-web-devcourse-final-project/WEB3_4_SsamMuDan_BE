package ssammudan.cotree.integration.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.common.techstack.repository.TechStackRepository;

/**
 * PackageName : ssammudan.cotree.integration.factory
 * FileName    : TechStackDataFactory
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
public class TechStackDataFactory {

	private final EntityManager em;
	private final TechStackRepository techStackRepository;

	public List<TechStack> createAndSaveTechStack() {
		List<TechStack> techStackList = new ArrayList<>();

		techStackList.add(TechStack.create("Java", "null"));
		techStackList.add(TechStack.create("Kotlin", "null"));
		techStackList.add(TechStack.create("Spring", "null"));
		techStackList.add(TechStack.create("React", "null"));

		List<TechStack> saveTechStackList = techStackRepository.saveAll(techStackList);
		em.flush();
		em.clear();
		return saveTechStackList;
	}
}
