package ssammudan.cotree.model.review.reviewtype.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.review.reviewtype.entity.TechEducationType;

/**
 * PackageName : ssammudan.cotree.model.review.techeducationtype.repository
 * FileName    : TechEducationTypeRepository
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : TechEducationType JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
public interface TechEducationTypeRepository extends JpaRepository<TechEducationType, Long> {
}
