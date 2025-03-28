package ssammudan.cotree.model.community.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * PackageName : ssammudan.cotree.model.community.category.entity
 * FileName    : CommunityCategory
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : community_category table `JPA Entity`
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28     Baekgwa               Initial creation
 */
@Entity
@Table(name = "community_category")
public class CommunityCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;
}
