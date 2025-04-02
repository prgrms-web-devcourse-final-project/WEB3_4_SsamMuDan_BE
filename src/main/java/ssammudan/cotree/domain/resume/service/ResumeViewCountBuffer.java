package ssammudan.cotree.domain.resume.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * PackageName : ssammudan.cotree.domain.resume.service
 * FileName    : ResumeViewCountBuffer
 * Author      : kwak
 * Date        : 2025. 4. 1.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 1.     kwak               Initial creation
 */
@Component
public class ResumeViewCountBuffer {

	private static final Map<Long, Integer> viewCountStorage = new ConcurrentHashMap<>();


	@Async
	public void increaseViewCount(Long resumeId) {
		viewCountStorage.compute(resumeId, (k, v) -> v == null ? 1 : v + 1);
	}

	public Map<Long, Integer> flushViewCount() {
		Map<Long, Integer> copy = new HashMap<>(viewCountStorage);
		viewCountStorage.clear();
		return copy;
	}
}
