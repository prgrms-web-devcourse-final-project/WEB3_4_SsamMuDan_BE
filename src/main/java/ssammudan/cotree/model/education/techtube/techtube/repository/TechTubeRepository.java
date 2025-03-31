package ssammudan.cotree.model.education.techtube.techtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.techtube.repository
 * FileName    : TechTubeRepository
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : TechTube JPA 리포지토리
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
public interface TechTubeRepository extends JpaRepository<TechTube, Long>, TechTubeRepositoryCustom {
}
