package ssammudan.cotree.model.member.oauth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ssammudan.cotree.model.member.oauth.entity.OAuth;

/**
 * PackageName : ssammudan.cotree.model.member.oauth.repository
 * FileName    : OAuthRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface OAuthRepository extends JpaRepository<OAuth, Long> {
	@EntityGraph(attributePaths = {"member"})
	@Query("SELECT o FROM OAuth o WHERE o.providerId = :providerId")
	Optional<OAuth> findByProviderId(@Param("providerId") String providerId);

}
