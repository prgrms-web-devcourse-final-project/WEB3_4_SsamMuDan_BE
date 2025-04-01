package ssammudan.cotree.model.review.reviewtype.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.review.reviewtype.entity.TechEducationType;

/**
 * PackageName : ssammudan.cotree.model.review.techeducationtype.repository
 * FileName    : TechEducationTypeRepository
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : TechEducationType JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
public interface TechEducationTypeRepository extends JpaRepository<TechEducationType, Long>,
	TechEducationTypeRepositoryCustom {

	Optional<TechEducationType> findByNameIgnoreCase(String name);

}
