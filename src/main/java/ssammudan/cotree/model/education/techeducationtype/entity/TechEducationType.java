package ssammudan.cotree.model.education.techeducationtype.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

/**
 * PackageName : ssammudan.cotree.model.education.techeducationtype.entity
 * FileName    : TechEducationType
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : TechEducationType 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
@Entity
@Table(
	name = "techEducation_type",
	uniqueConstraints = {
		@UniqueConstraint(name = "uq_tech_education_type_name", columnNames = "name")
	}
)
@Getter
public class TechEducationType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

}
