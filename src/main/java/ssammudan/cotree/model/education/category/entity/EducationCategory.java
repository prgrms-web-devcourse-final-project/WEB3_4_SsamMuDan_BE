package ssammudan.cotree.model.education.category.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.model.education.techbook.category.entity.TechBookEducationCategory;
import ssammudan.cotree.model.education.techtube.category.entity.TechTubeEducationCategory;

/**
 * PackageName : ssammudan.cotree.model.education.category.entity
 * FileName    : EducationCategory
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : EducationCategory 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21	      Initial creation
 */
@Entity
@Table(
	name = "education_category",
	uniqueConstraints = {
		@UniqueConstraint(name = "uc_education_category_name", columnNames = "name")
	}
)
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EducationCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;    //교육 카테고리: 백엔드, 프론트엔드, 웹 개발, 데이터, ...

	@OneToMany(mappedBy = "educationCategory")
	@Builder.Default
	private List<TechBookEducationCategory> techBookEducationCategories = new ArrayList<>();

	@OneToMany(mappedBy = "educationCategory")
	@Builder.Default
	private List<TechTubeEducationCategory> techTubeEducationCategories = new ArrayList<>();

	public static EducationCategory create(final String name) {
		return EducationCategory.builder().name(name).build();
	}

	@PrePersist
	private void prePersist() {
		if (this.techBookEducationCategories == null) {
			this.techBookEducationCategories = new ArrayList<>();
		}
		if (this.techTubeEducationCategories == null) {
			this.techTubeEducationCategories = new ArrayList<>();
		}
	}

	/**
	 * EducationCategory 엔티티 정보 수정
	 *
	 * @param newName - 새로운 교육 카테고리 명칭
	 * @return this
	 */
	public EducationCategory modify(final String newName) {
		if (!this.name.equals(newName) && !newName.isBlank()) {
			this.name = newName;
		}
		return this;
	}

}
