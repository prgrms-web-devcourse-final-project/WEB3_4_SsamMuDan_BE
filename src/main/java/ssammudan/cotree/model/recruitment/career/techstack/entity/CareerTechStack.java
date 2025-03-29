package ssammudan.cotree.model.recruitment.career.techstack.entity;

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
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.recruitment.career.career.entity.Career;

/**
 * PackageName : ssammudan.cotree.model.recruitment.career.techstack.entity
 * FileName    : CareerTechStack
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
@Table(name = "career_techStack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class CareerTechStack {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "career_id", nullable = false)
	private Career career;

	@ManyToOne
	@JoinColumn(name = "tech_stack_id", nullable = false)
	private TechStack techStack;

	public static CareerTechStack create(Career career, TechStack techStack) {
		return CareerTechStack.builder()
				.career(career)
				.techStack(techStack)
				.build();
	}
}