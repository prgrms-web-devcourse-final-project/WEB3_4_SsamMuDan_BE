package ssammudan.cotree.model.education.techtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.education.techtube.entity.TechTube;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.repository
 * FileName    : TechTubeRepository
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : TechTube JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
public interface TechTubeRepository extends JpaRepository<TechTube, Long> {
}
