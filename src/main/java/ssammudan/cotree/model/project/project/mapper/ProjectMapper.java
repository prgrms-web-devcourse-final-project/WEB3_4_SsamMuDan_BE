package ssammudan.cotree.model.project.project.mapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ssammudan.cotree.domain.project.project.dto.ProjectLikeListResponse;
import ssammudan.cotree.domain.project.project.dto.ProjectListResponse;
import ssammudan.cotree.model.project.devposition.entity.ProjectDevPosition;
import ssammudan.cotree.model.project.project.entity.Project;

/**
 * PackageName : ssammudan.cotree.model.project.project.mapper
 * FileName    : ProjectMapper
 * Author      : sangxxjin
 * Date        : 2025. 4. 11.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 11.     sangxxjin               Initial creation
 */
@Component
public class ProjectMapper {

	public List<ProjectListResponse> toDtoOrdered(List<Project> projects, List<Long> orderedIds) {
		return convertToOrdered(projects, orderedIds, this::toDto);
	}

	public List<ProjectLikeListResponse> toLikeDtoOrdered(List<Project> projects, List<Long> orderedIds) {
		return convertToOrdered(projects, orderedIds, this::toLikeDto);
	}

	private <T> List<T> convertToOrdered(List<Project> projects, List<Long> orderedIds, Function<Project, T> mapper) {
		Map<Long, T> dtoMap = projects.stream()
			.collect(Collectors.toMap(Project::getId, mapper));
		return orderedIds.stream()
			.map(dtoMap::get)
			.toList();
	}

	private ProjectListResponse toDto(Project p) {
		String shortenedDescription = p.getDescription().length() > 30
			? p.getDescription().substring(0, 30) + "..."
			: p.getDescription();

		return new ProjectListResponse(
			p.getId(),
			p.getTitle(),
			shortenedDescription,
			p.getProjectImageUrl(),
			p.getViewCount(),
			p.getLikes().size(),
			getRecruitmentCount(p),
			p.getIsOpen(),
			p.getStartDate(),
			p.getEndDate(),
			getTechStackImageUrls(p),
			p.getMember().getUsername(),
			p.getMember().getProfileImageUrl()
		);
	}

	private ProjectLikeListResponse toLikeDto(Project p) {
		return new ProjectLikeListResponse(
			p.getId(),
			p.getTitle(),
			getRecruitmentCount(p),
			p.getIsOpen(),
			p.getStartDate(),
			p.getEndDate(),
			getTechStackImageUrls(p)
		);
	}

	private List<String> getTechStackImageUrls(Project p) {
		return p.getProjectTechStacks().stream()
			.map(ts -> ts.getTechStack().getImageUrl())
			.toList();
	}

	private int getRecruitmentCount(Project p) {
		return p.getProjectDevPositions().stream()
			.mapToInt(ProjectDevPosition::getAmount)
			.sum();
	}
}