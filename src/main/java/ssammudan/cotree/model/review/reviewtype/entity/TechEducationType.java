package ssammudan.cotree.model.review.reviewtype.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.review.techeducationtype.entity
 * FileName    : TechEducationType
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : TechEducationType 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
@Entity
@Table(
	name = "techEducation_type",
	uniqueConstraints = {
		@UniqueConstraint(name = "uq_tech_education_type_name", columnNames = "name")
	}
)
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TechEducationType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	public static TechEducationType create(final String name) {
		return TechEducationType.builder().name(name).build();
	}

}
