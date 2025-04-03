package ssammudan.cotree.domain.project.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.model.project.project.repository.ProjectRepository;

/**
 * PackageName : ssammudan.cotree.domain.project.redis
 * FileName    : RedisViewCountProcessor
 * Author      : sangxxjin
 * Date        : 2025. 4. 3.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 3.     sangxxjin               Initial creation
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisViewCountProcessor {

	private final RedisTemplate<String, String> redisTemplate;
	private final ProjectRepository projectRepository;
	private static final String VIEW_COUNT_QUEUE = "project:viewCountQueue";

	@PostConstruct
	public void processViewCountUpdates() {
		new Thread(() -> {
			while (true) {
				try {
					String projectIdStr = redisTemplate.opsForList().rightPop(VIEW_COUNT_QUEUE);

					if (projectIdStr != null) {
						Long projectId = Long.valueOf(projectIdStr);
						String key = "project:viewCount:" + projectId;
						String value = redisTemplate.opsForValue().get(key);

						if (value != null) {
							int viewCount = Integer.parseInt(value);
							projectRepository.incrementViewCount(projectId, viewCount);
							redisTemplate.delete(key);
						}
					}
				} catch (Exception e) {
					log.error("조회수 동기화 중 오류 발생", e);
				}
			}
		}).start();
	}
}
