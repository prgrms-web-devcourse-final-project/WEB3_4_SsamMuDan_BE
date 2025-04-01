package ssammudan.cotree.model.member.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
