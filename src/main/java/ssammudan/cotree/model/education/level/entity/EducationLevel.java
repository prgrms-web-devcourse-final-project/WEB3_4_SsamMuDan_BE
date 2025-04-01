package ssammudan.cotree.model.education.level.entity;

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
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;

/**
 * PackageName : ssammudan.cotree.model.education.level.entity
 * FileName    : EducationLevel
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : EducationLevel 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21		  Initial creation
 */
@Entity
@Table(
	name = "education_level",
	uniqueConstraints = {
		@UniqueConstraint(name = "uq_education_level_name", columnNames = "name")
	}
)
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EducationLevel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;    //학습 난이도 명칭: 입문, 초급, 중급, ...

	@OneToMany(mappedBy = "educationLevel")
	@Builder.Default
	private List<TechBook> techBooks = new ArrayList<>();

	@OneToMany(mappedBy = "educationLevel")
	@Builder.Default
	private List<TechTube> techTubes = new ArrayList<>();

	public static EducationLevel create(final String name) {
		return EducationLevel.builder().name(name).build();
	}

	@PrePersist
	private void prePersist() {
		if (this.techBooks == null) {
			this.techBooks = new ArrayList<>();
		}
		if (this.techTubes == null) {
			this.techTubes = new ArrayList<>();
		}
	}

	/**
	 * EducationLevel 엔티티 정보 수정
	 *
	 * @param newName - 새로운 학습 난이도 명칭
	 * @return this
	 */
	public EducationLevel modify(final String newName) {
		if (!this.name.equals(newName) && !newName.isBlank()) {
			this.name = newName;
		}
		return this;
	}

}
