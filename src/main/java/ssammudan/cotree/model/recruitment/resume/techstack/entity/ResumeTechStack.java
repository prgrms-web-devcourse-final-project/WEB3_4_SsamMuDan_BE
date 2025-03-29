package ssammudan.cotree.model.recruitment.resume.techstack.entity;

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
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

/**
 * PackageName : ssammudan.cotree.model.recruitment.resume.techstack.entity
 * FileName    : ResumeTechStack
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
@Table(name = "resume_techStack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ResumeTechStack {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resume_id", nullable = false)
	private Resume resume;

	@ManyToOne
	@JoinColumn(name = "tech_stack_id", nullable = false)
	private TechStack techStack;

	public static ResumeTechStack create(Resume resume, TechStack techStack) {
		return ResumeTechStack.builder()
				.resume(resume)
				.techStack(techStack)
				.build();
	}
}