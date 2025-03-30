package ssammudan.cotree.model.education.techtube.techtube.entity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.global.converter.DurationToLongConverter;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.techtube.category.entity.TechTubeEducationCategory;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.techtube.entity
 * FileName    : TechTube
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : TechTube 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
@Entity
@Table(name = "tech_tube")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TechTube extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "member_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_tech_tube_member_id")
	)
	private Member writer;    //TechTube 작성자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "education_level_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_tech_tube_education_level_id")
	)
	private EducationLevel educationLevel;    //TechTube 학습 난이도

	@Column(name = "title", nullable = false)
	private String title;    //TechTube 제목

	@Column(name = "description", nullable = false)
	private String description;    //TechTube 설명

	@Column(name = "introduction", columnDefinition = "TEXT", nullable = false)
	private String introduction;    //TechTube 소개

	@Column(name = "total_rating", nullable = false)
	private Integer totalRating;    //TechTube 전체 누적 평점

	@Column(name = "total_review_count", nullable = false)
	private Integer totalReviewCount;    //TechTube 전체 리뷰 수

	@Column(name = "tech_tube_url", nullable = false)
	private String techTubeUrl;    //TechTube 영상 URL

	@Column(name = "tech_tube_duration")
	@Convert(converter = DurationToLongConverter.class)
	private Duration techTubeDuration;    //TechTube 영상 재생 시간

	@Column(name = "tech_tube_thumbnail_url", nullable = false)
	private String techTubeThumbnailUrl;    //TechTube 썸네일 URL

	@Column(name = "price", nullable = false)
	private Integer price;    //TechTube 가격

	@Column(name = "view_count", nullable = false)
	private Integer viewCount = 0;    //TechTube 조회 수

	@OneToMany(mappedBy = "techTube")
	@Builder.Default
	private List<TechTubeEducationCategory> techTubeEducationCategories = new ArrayList<>();

	public static TechTube create(
		final Member writer,
		final EducationLevel educationLevel,
		final String title,
		final String description,
		final String introduction,
		final String techTubeUrl,
		final Duration techTubeDuration,
		final String techTubeThumbnailUrl,
		final Integer price
	) {
		//TODO: TechTube 작성자 연관관계 설정 확인 필요
		TechTube techTube = TechTube.builder()
			.writer(writer)
			.title(title)
			.description(description)
			.introduction(introduction)
			.techTubeUrl(techTubeUrl)
			.techTubeDuration(techTubeDuration)
			.techTubeThumbnailUrl(techTubeThumbnailUrl)
			.price(price)
			.build();
		techTube.setRelationshipWithEducationLevel(educationLevel);
		return techTube;
	}

	@PrePersist
	private void prePersist() {
		if (this.viewCount == null) {
			this.viewCount = 0;
		}
		if (this.techTubeEducationCategories == null) {
			this.techTubeEducationCategories = new ArrayList<>();
		}
	}

	private void setRelationshipWithEducationLevel(final EducationLevel educationLevel) {
		this.educationLevel = educationLevel;
		educationLevel.getTechTubes().add(this);
	}

	/**
	 * TechTube 엔티티 학습 난이도(EducationLevel) 수정
	 *
	 * @param newEducationLevel - 새로운 TechTube 학습 난이도
	 * @return this
	 */
	public TechTube modifyEducationLevel(final EducationLevel newEducationLevel) {
		if (!this.educationLevel.getId().equals(newEducationLevel.getId())) {
			this.educationLevel.getTechTubes().remove(this);
			setRelationshipWithEducationLevel(newEducationLevel);
		}
		return this;
	}

	public TechTube modifyDetail(
		final String newTitle,
		final String newDescription,
		final String newIntroduction,
		final String newTechTubeUrl,
		final Duration newTechTubeDuration,
		final String newTechTubeThumbnailUrl,
		final Integer newPrice
	) {
		if (!this.title.equals(newTitle) && !newTitle.isBlank()) {
			this.title = newTitle;
		}

		if (!this.description.equals(newDescription) && !newDescription.isBlank()) {
			this.description = newDescription;
		}

		if (!this.introduction.equals(newIntroduction) && !newIntroduction.isBlank()) {
			this.introduction = newIntroduction;
		}

		if (!this.techTubeUrl.equals(newTechTubeUrl) && !newTechTubeUrl.isBlank()) {
			this.techTubeUrl = newTechTubeUrl;
		}

		if (!this.techTubeDuration.equals(newTechTubeDuration) && newTechTubeDuration != null) {
			this.techTubeDuration = newTechTubeDuration;
		}

		if (!this.techTubeThumbnailUrl.equals(newTechTubeThumbnailUrl) && !newTechTubeThumbnailUrl.isBlank()) {
			this.techTubeThumbnailUrl = newTechTubeThumbnailUrl;
		}

		if (!this.price.equals(newPrice) && newPrice != null) {
			this.price = newPrice;
		}

		return this;
	}

	/**
	 * TechTube 조회 수 증가
	 *
	 * @return this
	 */
	public TechTube increaseViewCount() {
		this.viewCount += 1;
		return this;
	}

}
