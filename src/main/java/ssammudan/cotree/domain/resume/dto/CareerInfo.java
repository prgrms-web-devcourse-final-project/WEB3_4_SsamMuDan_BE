package ssammudan.cotree.domain.resume.dto;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

/**
 * PackageName : ssammudan.cotree.domain.resume.dto
 * FileName    : CareerInfo
 * Author      : kwak
 * Date        : 2025. 3. 28.
 * Description : 커리어 정보에는 시작날짜, 종료날짜, 회사이름, 주요업무 및 성과 존재 + 개발직무와 기술스택은 별도의 API 호출
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 28.     kwak               Initial creation
 */
public record CareerInfo(
	@NotNull(message = "시작 날짜는 필수입니다.")
	@PastOrPresent(message = "시작 날짜는 현재 날짜 이후일 수 없습니다.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate startDate,

	@Nullable
	@FutureOrPresent(message = "종료 날짜는 시작 날짜 이후여야 합니다.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate endDate,

	@NotBlank(message = "직무 설명은 필수입니다.")
	String position,

	@NotBlank(message = "회사 이름은 필수입니다.")
	String companyName,

	@NotBlank(message = "주요 업무 및 성과는 필수입니다.")
	@Size(min = 1, max = 1000, message = "최소 1자에서 최대 1000자 까지 입력가능합니다.")
	String careerDescription,

	boolean isWorking,

	@NotEmpty(message = "기술 스택 ID는 최소 1개 이상이어야 합니다.")
	@Size(min = 1, max = 9, message = "기술 스택은 최소 1개에서 최대 10개 미만입니다.")
	Set<Long> techStackIds
) {
}
