package ssammudan.cotree.model.project.project.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.domain.project.project.dto.ProjectCreateRequest;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.common.like.entity.Like;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.membership.entity.ProjectMembership;
import ssammudan.cotree.model.project.techstack.entity.ProjectTechStack;

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

	@OneToMany(mappedBy = "project")
	@OrderBy("techStack.id ASC")
	private Set<ProjectTechStack> projectTechStacks;

	@OneToMany(mappedBy = "project")
	@OrderBy("developmentPosition.id ASC")
	private Set<ProjectDevPosition> projectDevPositions;

	@OneToMany(mappedBy = "project")
	private Set<ProjectMembership> projectMemberships;

	@OneToMany(mappedBy = "project")
	private Set<Like> likes;

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

	public void toggleIsOpen() {
		this.isOpen = !this.isOpen;
	}
}
