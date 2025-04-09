package ssammudan.cotree.domain.project.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.domain.project.service
 * FileName    : ProjectViewService
 * Author      : sangxxjin
 * Date        : 2025. 4. 3.
 * Description : 조회수 증가 로직
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 3.     sangxxjin               Initial creation
 * 2025. 4. 9.     Baekgwa                 ViewCount 증가 `ViewCountStore` 에서 통합 관리 진행
 */
@Service
@RequiredArgsConstructor
@Deprecated(since = "2025-04-09", forRemoval = true)
public class ProjectViewService {

	private final RedisTemplate<String, String> redisTemplate;
	private static final String VIEW_COUNT_KEY_PREFIX = "project:viewCount:";
	private static final String VIEW_COUNT_QUEUE = "project:viewCountQueue";

	@Deprecated(since = "2025-04-09", forRemoval = true)
	public void incrementViewCount(Long projectId) {
		String key = VIEW_COUNT_KEY_PREFIX + projectId;
		redisTemplate.opsForValue().increment(key);
		redisTemplate.opsForSet().add(VIEW_COUNT_QUEUE, projectId.toString());
	}
}