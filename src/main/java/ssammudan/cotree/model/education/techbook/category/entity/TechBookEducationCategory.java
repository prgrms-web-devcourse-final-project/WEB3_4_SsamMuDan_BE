package ssammudan.cotree.model.education.techbook.category.entity;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.model.education.category.entity.EducationCategory;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.category.entity
 * FileName    : TechBookEducationCategory
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBookEducationCategory 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 */
@Entity
@Table(name = "techBook_educationCategory")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
		foreignKey = @ForeignKey(name = "fk_tech_book_education_category_education_category_id")
	)
	private EducationCategory educationCategory;

	public static TechBookEducationCategory create(final TechBook techBook, final EducationCategory educationCategory) {
		TechBookEducationCategory techBookEducationCategory = TechBookEducationCategory.builder().build();
		techBookEducationCategory.setRelationshipWithTechBook(techBook);
		techBookEducationCategory.setRelationshipWithEducationCategory(educationCategory);
		return techBookEducationCategory;
	}

	private void setRelationshipWithTechBook(final TechBook techBook) {
		this.techBook = techBook;
		techBook.getTechBookEducationCategories().add(this);
	}

	private void setRelationshipWithEducationCategory(final EducationCategory educationCategory) {
		this.educationCategory = educationCategory;
		educationCategory.getTechBookEducationCategories().add(this);
	}

}
