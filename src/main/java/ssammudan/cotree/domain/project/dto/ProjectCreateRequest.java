package ssammudan.cotree.domain.project.dto;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

/**
 * PackageName : ssammudan.cotree.domain.project.dto
 * FileName    : ProjectRequest
 * Author      : sangxxjin
 * Date        : 2025. 4. 2.
 * Description : 1개 이상의 테크스택과, 포지션별 인원수를 요구함
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 2.     sangxxjin               Initial creation
 */
public record ProjectCreateRequest(
	@NotBlank @Size(max = 20) String title,
	@NotBlank @Size(min = 1, max = 500) String description,
	@NotBlank @Email String contact,
	@NotBlank String partnerType,
	@NotEmpty @Size(min = 1, max = 9) Set<Long> techStackIds,
	@NotEmpty Map<Long, @NotNull @Min(1) @Max(10) Integer> recruitmentPositions,
	@NotNull @PastOrPresent @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
	@Nullable @FutureOrPresent @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
) {
}