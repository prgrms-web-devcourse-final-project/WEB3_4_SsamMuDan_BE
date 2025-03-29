package ssammudan.cotree.model.education.educationlevel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

/**
 * PackageName : ssammudan.cotree.model.education.educationlevel.entity
 * FileName    : EducationLevel
 * Author      : SSamMuDan
 * Date        : 25. 3. 28.
 * Description : EducationLevel 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    SSamMuDan     		  Initial creation
 */
@Entity
@Table(
	name = "education_level",
	uniqueConstraints = {
		@UniqueConstraint(name = "uq_education_level_name", columnNames = "name")
	}
)
@Getter
public class EducationLevel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

}
