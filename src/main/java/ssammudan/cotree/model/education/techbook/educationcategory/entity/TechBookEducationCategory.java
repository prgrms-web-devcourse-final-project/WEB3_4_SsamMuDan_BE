package ssammudan.cotree.model.education.techbook.educationcategory.entity;

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
import ssammudan.cotree.model.education.educationcategory.entity.EducationCategory;
import ssammudan.cotree.model.education.techbook.entity.TechBook;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.educationcategory.entity
 * FileName    : TechBookEducationCategory
 * Author      : SSamMuDan
 * Date        : 25. 3. 28.
 * Description : TechBookEducationCategory 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    SSamMuDan		      Initial creation
 */
@Entity
@Table(name = "techBook_educationCategory")
@Getter
public class TechBookEducationCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "tech_book_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_tech_book_education_category_tech_book_id")
	)
	private TechBook techBook;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "education_category_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_tech_book_education_category_education_category_id")
	)
	private EducationCategory educationCategory;

}
