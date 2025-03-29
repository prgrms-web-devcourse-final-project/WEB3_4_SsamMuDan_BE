package ssammudan.cotree.model.recruitment.resume.resume.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.model.recruitment.resume.resume.entity
 * FileName    : Resume
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
@Entity
@Getter
@Table(name = "resume")
public class Resume extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "profile_image", nullable = false)
	private String profileImage;

	@Column(name = "introduction", nullable = false, columnDefinition = "TEXT")
	private String introduction;

	@Column(name = "years", nullable = false)
	private Integer years;

	@Column(name = "is_open", nullable = false)
	private Boolean isOpen;

	@Column(name = "view_count", nullable = false)
	private Integer viewCount;
}
