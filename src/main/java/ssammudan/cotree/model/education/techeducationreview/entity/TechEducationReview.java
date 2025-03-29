package ssammudan.cotree.model.education.techeducationreview.entity;

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
import ssammudan.cotree.model.education.techeducationtype.entity.TechEducationType;
import ssammudan.cotree.model.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.model.education.techeducationreview.entity
 * FileName    : TechEducationReview
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : TechEducationReview 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan	          Initial creation
 */
@Entity
@Table(name = "techEducation_review")
@Getter
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
	private Member member;

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

}
