package ssammudan.cotree.domain.resume.scheduler;

import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.resume.service.ResumeService;
import ssammudan.cotree.domain.resume.service.ResumeViewCountBuffer;

/**
 * PackageName : ssammudan.cotree.domain.resume.scheduler
 * FileName    : ResumeScheduler
 * Author      : kwak
 * Date        : 2025. 4. 1.
 * Description : 이력서 도메인과 관련된 스케줄러 입니다
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 1.     kwak               Initial creation
 */
@Component
@RequiredArgsConstructor
public class ResumeScheduler {

	private final ResumeViewCountBuffer resumeViewCountBuffer;
	private final ResumeService resumeService;

	@Transactional
	@Scheduled(fixedRate = 3000)
	public void updateViewCount() {
		Map<Long, Integer> viewCountData = resumeViewCountBuffer.flushViewCount();
		if (viewCountData.isEmpty()) {
			return;
		}
		resumeService.bulkViewCount(viewCountData);
	}

}
