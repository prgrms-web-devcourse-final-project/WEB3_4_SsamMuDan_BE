package ssammudan.cotree.model.project.project.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import ssammudan.cotree.domain.project.dto.ProjectCreateRequest;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.model.project.project.entity
 * FileName    : Project
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 * 2025-04--2     sangxxjin             add builder
 */
@Entity
@Getter
@Table(name = "project")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Project extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;

	@Column(name = "contact", nullable = false)
	private String contact;

	@Column(name = "project_image_url")
	private String projectImageUrl;

	@Column(name = "partner_type", nullable = false, columnDefinition = "TEXT")
	private String partnerType;

	@Column(name = "is_open", nullable = false)
	@Builder.Default
	private Boolean isOpen = true;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "view_count", nullable = false)
	@Builder.Default
	private Integer viewCount = 0;

	public static Project create(Member member, ProjectCreateRequest request, String savedImageUrl) {
		return Project.builder()
			.member(member)
			.title(request.title())
			.description(request.description())
			.contact(request.contact())
			.projectImageUrl(savedImageUrl)
			.partnerType(request.partnerType())
			.startDate(request.startDate())
			.endDate(request.endDate())
			.build();
	}
}
