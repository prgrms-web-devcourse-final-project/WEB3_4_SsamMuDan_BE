package ssammudan.cotree.model.education.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

/**
 * PackageName : ssammudan.cotree.model.education.educationcategory.entity
 * FileName    : EducationCategory
 * Author      : SSamMuDan
 * Date        : 25. 3. 28.
 * Description : EducationCategory 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    SSamMuDan		      Initial creation
 */
@Entity
@Table(
	name = "education_category",
	uniqueConstraints = {
		@UniqueConstraint(name = "uc_education_category_name", columnNames = "name")
	}
)
@Getter
public class EducationCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

}
