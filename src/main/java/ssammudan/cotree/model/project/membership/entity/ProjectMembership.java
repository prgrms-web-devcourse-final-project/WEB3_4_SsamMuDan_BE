package ssammudan.cotree.model.project.membership.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.project.membership.type.ProjectMembershipStatus;
import ssammudan.cotree.model.project.project.entity.Project;

/**
 * PackageName : ssammudan.cotree.model.project.membership.entity
 * FileName    : ProjectMembership
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : ProjectMembership entity
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 * 2025-04-03     sangxxjin             빠져있던 member 컬럼 추가
 * 2025-04-08     sangxxjin             ProjectMebership 빌더 구현
 */
@Entity
@Getter
@Table(name = "project_membership")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectMembership extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@Enumerated(EnumType.STRING)
	@Column(name = "project_membership_status", nullable = false)
	private ProjectMembershipStatus projectMembershipStatus;

	public static ProjectMembership builderForApply(Project project, Member member) {
		return builder()
			.project(project)
			.member(member)
			.projectMembershipStatus(ProjectMembershipStatus.PENDING)
			.build();
	}
}
