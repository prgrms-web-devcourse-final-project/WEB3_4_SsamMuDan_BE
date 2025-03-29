package ssammudan.cotree.model.education.techtube.entity;

import java.sql.Time;

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
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.education.educationlevel.entity.EducationLevel;
import ssammudan.cotree.model.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.model.education.techtube.entity
 * FileName    : TechTube
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : TechTube 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
@Entity
@Table(name = "tech_tube")
@Getter
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
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "education_level_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_tech_tube_education_level_id")
	)
	private EducationLevel educationLevel;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "introduction", columnDefinition = "TEXT", nullable = false)
	private String introduction;

	@Column(name = "total_rating", nullable = false)
	private Integer totalRating;

	@Column(name = "total_review_count", nullable = false)
	private Integer totalReviewCount;

	@Column(name = "tech_tube_url", nullable = false)
	private String techTubeUrl;

	@Column(name = "tech_tube_duration", nullable = false)
	private Time techTubeDuration;

	@Column(name = "tech_tube_thumbnail_url", nullable = false)
	private String techTubeThumbnailUrl;

	@Column(name = "price", nullable = false)
	private Integer price;

	@Column(name = "view_count", nullable = false)
	private Integer viewCount;

}
