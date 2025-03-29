package ssammudan.cotree.model.recruitment.resume.resume.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.recruitment.resume.developmentposition.entity.ResumeDevelopmentPosition;
import ssammudan.cotree.model.recruitment.resume.techstack.entity.ResumeTechStack;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Resume extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, columnDefinition = "BIGINT")
	private Long id;

	@Column(name = "member_id", nullable = false, length = 255)
	private String memberId;

	@Column(name = "email", nullable = false, length = 255)
	private String email;

	@Column(name = "profile_image", nullable = false, length = 255)
	private String profileImage;

	@Column(name = "introduction", nullable = false, columnDefinition = "TEXT")
	private String introduction;

	@Column(name = "years", nullable = false)
	private Integer years;

	@Column(name = "is_open", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
	private Boolean isOpen;

	@Column(name = "view_count", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
	private Integer viewCount;

	@OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ResumeTechStack> resumeTechStacks = new ArrayList<>();

	@OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ResumeDevelopmentPosition> resumeDevelopmentPositions = new ArrayList<>();

	public static Resume create(
			ResumeCreateRequest request, String memberId,
			List<TechStack> techStacks, List<DevelopmentPosition> developmentPositions
	) {
		Resume resume = Resume.builder()
				.memberId(memberId)
				.email(request.basicInfo().email())
				.profileImage(request.basicInfo().profileImage())
				.introduction(request.basicInfo().introduction())
				.years(request.basicInfo().years())
				.isOpen(true)
				.viewCount(0)
				.build();
		resume.addTechStacks(techStacks);
		resume.addDevelopmentPositions(developmentPositions);
		return resume;
	}

	private void addTechStacks(List<TechStack> techStacks) {
		techStacks.stream().map(ts ->
				ResumeTechStack.create(this, ts)
		).forEach(this.resumeTechStacks::add);
	}

	private void addDevelopmentPositions(List<DevelopmentPosition> developmentPositions) {
		developmentPositions.stream().map(dp ->
				ResumeDevelopmentPosition.create(this, dp)
		).forEach(this.resumeDevelopmentPositions::add);
	}
}
