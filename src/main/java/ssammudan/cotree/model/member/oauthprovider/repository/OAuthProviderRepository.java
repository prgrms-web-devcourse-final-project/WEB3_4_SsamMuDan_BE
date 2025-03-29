package ssammudan.cotree.model.member.oauthprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.member.oauthprovider.entity.OAuthProvider;

/**
 * PackageName : ssammudan.cotree.model.member.oauthprovider.repository
 * FileName    : OAuthProviderRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface OAuthProviderRepository extends JpaRepository<OAuthProvider, Long> {
}
