package ssammudan.cotree.model.education.techtube.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import ssammudan.cotree.model.education.category.entity.EducationCategory;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.educationcategory.entity
 * FileName    : TechTubeEducationCategory
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : TechTubeEducationCategory 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
@Entity
@Table(name = "techTube_educationCategory")
@Getter
public class TechTubeEducationCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "tech_tube_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_tech_tube_education_category_tech_tube_id")
	)
	private TechTube techTube;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "education_category_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_tech_tube_education_category_education_category_id")
	)
	private EducationCategory educationCategory;

}
