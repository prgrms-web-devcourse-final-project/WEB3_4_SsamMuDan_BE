package ssammudan.cotree.model.recruitment.portfolio.portfolio.entity;

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
import ssammudan.cotree.domain.resume.dto.PortfolioInfo;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.recruitment.portfolio.techstack.entity.PortfolioTechStack;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

/**
 * PackageName : ssammudan.cotree.model.recruitment.portfolio.portfolio.entity
 * FileName    : Portfolio
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
@Table(name = "portfolio")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Portfolio extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resume_id", nullable = false)
	private Resume resume;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "description", columnDefinition = "TEXT", nullable = false)
	private String description;

	@Column(name = "is_developing", nullable = false)
	private boolean isDeveloping;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Nullable
	@Column(name = "end_date")
	private LocalDate endDate;

	@OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<PortfolioTechStack> portfolioTechStacks = new ArrayList<>();

	public static Portfolio create(PortfolioInfo portfolioInfo, Resume resume, List<TechStack> techStacks) {
		Portfolio portfolio = Portfolio.builder()
			.resume(resume)
			.name(portfolioInfo.projectName())
			.description(portfolioInfo.projectDescription())
			.isDeveloping(true)
			.startDate(portfolioInfo.startDate())
			.endDate(portfolioInfo.endDate())
			.build();
		portfolio.addPortfolioTechStack(techStacks);
		return portfolio;
	}

	private void addPortfolioTechStack(List<TechStack> techStacks) {
		techStacks.stream().map(techStack ->
			PortfolioTechStack.create(this, techStack)
		).forEach(this.portfolioTechStacks::add);
	}
}
