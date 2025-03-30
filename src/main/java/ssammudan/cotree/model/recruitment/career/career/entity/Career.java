package ssammudan.cotree.model.recruitment.career.career.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
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
import ssammudan.cotree.domain.resume.dto.CareerInfo;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.recruitment.career.techstack.entity.CareerTechStack;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

/**
 * PackageName : ssammudan.cotree.model.recruitment.career.career.entity
 * FileName    : Career
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
@Table(name = "career")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Career extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resume_id", nullable = false)
	private Resume resume;

	@Column(name = "position", nullable = false, length = 255)
	private String position;

	@Column(name = "company", nullable = false, length = 255)
	private String company;

	@Column(name = "description", columnDefinition = "TEXT", nullable = false)
	private String description;

	@Column(name = "is_working", nullable = false)
	private boolean isWorking = false;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Nullable
	@Column(name = "end_date")
	private LocalDate endDate;

	@OneToMany(mappedBy = "career", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CareerTechStack> careerTechStacks = new ArrayList<>();

	public static Career create(CareerInfo careerInfo, Resume resume, List<TechStack> techStacks) {
		Career career = Career.builder()
				.resume(resume)
				.position(careerInfo.position())
				.company(careerInfo.companyName())
				.description(careerInfo.careerDescription())
				.isWorking(careerInfo.isWorking())
				.startDate(careerInfo.startDate())
				.endDate(careerInfo.endDate())
				.build();
		career.addCareerTechStack(techStacks);
		return career;
	}

	private void addCareerTechStack(List<TechStack> techStacks) {
		techStacks.stream().map(techStack ->
				CareerTechStack.create(this, techStack)
		).forEach(this.careerTechStacks::add);
	}

}
