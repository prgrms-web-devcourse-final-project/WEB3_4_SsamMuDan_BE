package ssammudan.cotree.model.review.review.entity;

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
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.review.reviewtype.entity.TechEducationType;

/**
 * PackageName : ssammudan.cotree.model.review.techeducationreview.entity
 * FileName    : TechEducationReview
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : TechEducationReview 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
@Entity
@Table(name = "techEducation_review")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TechEducationReview extends BaseEntity {

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
		foreignKey = @ForeignKey(name = "fk_tech_education_review_member_id")
	)
	private Member reviewer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "tech_education_type_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_tech_education_review_tech_education_type_id")
	)
	private TechEducationType techEducationType;

	@Column(name = "rating", nullable = false)
	private Integer rating;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "item_id", nullable = false)
	private Long itemId;

	public static TechEducationReview create(
		final Member reviewer,
		final TechEducationType techEducationType,
		final Integer rating,
		final String content,
		final Long itemId
	) {
		//TODO: TechEducationReview 작성자 연관관계 설정 확인 필요
		return TechEducationReview.builder()
			.reviewer(reviewer)
			.techEducationType(techEducationType)
			.rating(rating)
			.content(content)
			.itemId(itemId)
			.build();
	}

}
