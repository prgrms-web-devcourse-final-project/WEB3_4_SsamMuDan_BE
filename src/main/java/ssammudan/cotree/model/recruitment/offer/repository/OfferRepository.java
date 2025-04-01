package ssammudan.cotree.model.recruitment.offer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.recruitment.offer.entity.Offer;

/**
 * PackageName : ssammudan.cotree.model.recruitment.offer.repository
 * FileName    : OfferRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface OfferRepository extends JpaRepository<Offer, Long> {
}
