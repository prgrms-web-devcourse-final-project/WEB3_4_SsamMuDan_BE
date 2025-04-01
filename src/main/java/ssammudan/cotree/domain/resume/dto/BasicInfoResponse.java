package ssammudan.cotree.domain.resume.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import ssammudan.cotree.domain.resume.dto.query.BasicInfoQueryDto;
import ssammudan.cotree.domain.resume.dto.query.TechStackInfo;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : BasicResponse
 * Author      : kwak
 * Date        : 2025. 3. 30.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 30.     kwak               Initial creation
 */
@Builder(access = AccessLevel.PRIVATE)
public record BasicInfoResponse(
	String name,
	String imageUrl,
	List<String> positionNames,
	List<TechStackInfo> techStackInfos,
	Integer years,
	String email,
	String introduction,
	Integer viewCount
) {
	public static BasicInfoResponse of(BasicInfoQueryDto basicInfoQueryDto, List<String> positionNames,
		List<TechStackInfo> techStackInfos) {
		return BasicInfoResponse.builder()
			.name(basicInfoQueryDto.getName())
			.imageUrl(builder().imageUrl)
			.positionNames(positionNames)
			.techStackInfos(techStackInfos)
			.years(basicInfoQueryDto.getYears())
			.email(basicInfoQueryDto.getEmail())
			.introduction(basicInfoQueryDto.getIntroduction())
			.viewCount(basicInfoQueryDto.getViewCount())
			.build();
	}
}
