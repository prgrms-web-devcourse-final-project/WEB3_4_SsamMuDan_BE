package ssammudan.cotree.infra.viewcount.persistence;

import static ssammudan.cotree.infra.viewcount.constant.ViewCountConstant.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.infra.viewcount.type.ViewCountType;

/**
 * PackageName : ssammudan.cotree.infra.viewcount
 * FileName    : ViewCountStoreRedis
 * Author      : Baekgwa
 * Date        : 2025-04-08
 * Description : RedisTemplate 으로, ViewCounter 증가/조회 처리용. repository 성 class
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-08     Baekgwa               Initial creation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountStoreRedis implements ViewCountStore {

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * Redis에 해당 ID의 조회수를 1 증가시키고, dirty set에 ID를 추가합니다.
	 * <p>비동기로 동작하며, 실패 시 최대 3번 재시도합니다.</p>
	 * @param category ViewCount 도메인 타입
	 * @param itemId 조회수 증가 대상 ID
	 */
	@Async
	@Retryable(
		retryFor = {Exception.class},
		maxAttempts = 3,
		backoff = @Backoff(delay = 1000)
	)
	@Override
	public void incrementViewCount(ViewCountType category, Long itemId) {
		try {
			// countKey, dirtyKey 생성
			String countKey = countKeyGenerator(category, itemId);
			String dirtyKey = dirtySetKeyGenerator(category);

			incrementByKey(countKey);
			addDirtySet(dirtyKey, itemId);

			log.info("category = {}, id = {}, 조회수 증가 (임시저장)", category.name(), itemId);
		} catch (Exception e) {
			log.error("ViewCount increment 실패 - category : {}, itemId : {}, ", category.name(), itemId, e);
		}
	}

	/**
	 * 주어진 카테고리에 해당하는 dirty ID 집합을 반환합니다.
	 * <p>해당 ID들은 추후 DB 반영 대상입니다.</p>
	 *
	 * @param category 대상 ViewCount 도메인 타입
	 * @return dirty 상태인 ID Set (값이 없으면 빈 Set, 절대 null 아님)
	 */
	@Override
	@NonNull
	public Set<Long> getDirtyIdSet(ViewCountType category) {
		// dirtyKey 생성
		String dirtyKey = dirtySetKeyGenerator(category);

		// 키에 해당하는 전체 update target 조회
		return findDirtySetByKey(dirtyKey);
	}

	/**
	 * 특정 카테고리/ID에 대한 현재 Redis 상의 조회수 값을 조회합니다.
	 *
	 * @param category ViewCount 도메인 타입
	 * @param itemId 대상 ID
	 * @return 현재 Redis 상에 저장된 조회수 값 (없을 경우 null)
	 */
	@Override
	public Object getCount(ViewCountType category, Long itemId) {
		// countKey 생성
		String countKey = countKeyGenerator(category, itemId);

		// countKey 에 해당하는 update 할 viewCount 조회
		return findViewCountByKey(countKey);
	}

	/**
	 * 특정 카테고리와 관련된 Redis 키 (조회수, dirty set 등)를 모두 삭제합니다.
	 * <p>DB 반영 이후 호출됩니다.</p>
	 *
	 * @param category 삭제 대상 카테고리
	 */
	@Override
	public void removeUpdatedItem(ViewCountType category) {
		String keyPrefix = switch (category) {
			case PROJECT -> VIEW_COUNT_PROJECT_KEY_PREFIX;
			case RESUME -> VIEW_COUNT_RESUME_KEY_PREFIX;
			case TECH_TUBE -> VIEW_COUNT_TECH_TUBE_KEY_PREFIX;
			case TECH_BOOK -> VIEW_COUNT_TECH_BOOK_KEY_PREFIX;
			case COMMUNITY -> VIEW_COUNT_COMMUNITY_KEY_PREFIX;
		};

		Set<String> keySetToDelete = redisTemplate.keys(keyPrefix + "*");
		if (keySetToDelete != null && !keySetToDelete.isEmpty()) {
			redisTemplate.delete(keySetToDelete);
		}
	}

	/**
	 * ViewCountType과 ID로 조회수 키를 생성합니다.
	 */
	private String countKeyGenerator(ViewCountType category, Long itemId) {
		return switch (category) {
			case PROJECT -> VIEW_COUNT_PROJECT_KEY_PREFIX + itemId;
			case RESUME -> VIEW_COUNT_RESUME_KEY_PREFIX + itemId;
			case TECH_TUBE -> VIEW_COUNT_TECH_TUBE_KEY_PREFIX + itemId;
			case TECH_BOOK -> VIEW_COUNT_TECH_BOOK_KEY_PREFIX + itemId;
			case COMMUNITY -> VIEW_COUNT_COMMUNITY_KEY_PREFIX + itemId;
		};
	}

	/**
	 * ViewCountType으로 dirty set 키를 생성합니다.
	 */
	private String dirtySetKeyGenerator(ViewCountType category) {
		return switch (category) {
			case PROJECT -> VIEW_COUNT_PROJECT_DIRTY_SET;
			case RESUME -> VIEW_COUNT_RESUME_DIRTY_SET;
			case TECH_TUBE -> VIEW_COUNT_TECH_TUBE_DIRTY_SET;
			case TECH_BOOK -> VIEW_COUNT_TECH_BOOK_DIRTY_SET;
			case COMMUNITY -> VIEW_COUNT_COMMUNITY_DIRTY_SET;
		};
	}

	/**
	 * 조회수 키에 대해 Redis에서 1 증가시킵니다.
	 */
	private void incrementByKey(String key) {
		redisTemplate.opsForValue().increment(key);
	}

	/**
	 * Dirty Set 키에 ID를 추가합니다.
	 */
	private void addDirtySet(String key, Long itemId) {
		redisTemplate.opsForSet().add(key, itemId);
	}

	/**
	 * 주어진 dirtyKey에 해당하는 ID 집합을 Redis에서 조회합니다.
	 *
	 * @param dirtyKey Redis Set 키
	 * @return 조회된 ID 집합 (값이 없으면 빈 Set, 절대 null 아님)
	 */
	@NonNull
	private Set<Long> findDirtySetByKey(String dirtyKey) {
		Set<Object> dirtyKeySet = redisTemplate.opsForSet().members(dirtyKey);

		if (dirtyKeySet == null || dirtyKeySet.isEmpty()) {
			return Set.of();
		}

		return dirtyKeySet
			.stream()
			.map(obj -> ((Number)obj).longValue())
			.collect(Collectors.toSet());
	}

	/**
	 * Redis에서 조회수 키에 해당하는 값을 조회합니다.
	 */
	private Object findViewCountByKey(String countKey) {
		return redisTemplate.opsForValue().get(countKey);
	}
}
