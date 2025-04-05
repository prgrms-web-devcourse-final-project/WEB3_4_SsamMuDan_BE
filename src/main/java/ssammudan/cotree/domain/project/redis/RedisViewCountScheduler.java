package ssammudan.cotree.domain.project.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.model.project.project.repository.ProjectRepository;

/**
 * PackageName : ssammudan.cotree.domain.project.redis
 * FileName    : RedisViewCountScheduler
 * Author      : sangxxjin
 * Date        : 2025. 4. 4.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 4.     sangxxjin               Initial creation
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisViewCountScheduler {

	private final RedisTemplate<String, String> redisTemplate;
	private final ProjectRepository projectRepository;
	private static final String VIEW_COUNT_KEY_PREFIX = "project:viewCount:";
	private static final String VIEW_COUNT_QUEUE = "project:viewCountQueue";

	@Scheduled(fixedDelay = 4000)
	public void processViewCountUpdates() {
		Set<String> projectIds = redisTemplate.opsForSet().members(VIEW_COUNT_QUEUE);
		if (projectIds == null || projectIds.isEmpty()) {
			return;
		}

		Map<Long, Integer> viewCounts = new HashMap<>();

		for (String projectIdStr : projectIds) {
			try {
				Long projectId = Long.valueOf(projectIdStr);
				String key = VIEW_COUNT_KEY_PREFIX + projectId;
				String value = redisTemplate.opsForValue().get(key);

				if (value != null) {
					int viewCount = Integer.parseInt(value);
					viewCounts.put(projectId, viewCount);

					redisTemplate.delete(key);
				}
			} catch (Exception e) {
				log.error("조회수 동기화 중 오류 발생: {}", projectIdStr, e);
			}
		}

		if (!viewCounts.isEmpty()) {
			viewCounts.forEach(projectRepository::incrementViewCount);
			redisTemplate.delete(VIEW_COUNT_QUEUE);
		}
	}
}