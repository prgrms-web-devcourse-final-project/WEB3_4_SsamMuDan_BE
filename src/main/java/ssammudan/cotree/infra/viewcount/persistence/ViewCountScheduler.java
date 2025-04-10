package ssammudan.cotree.infra.viewcount.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.infra.viewcount.repository.ViewCountRepository;
import ssammudan.cotree.infra.viewcount.type.ViewCountType;

/**
 * PackageName : ssammudan.cotree.infra.viewcount.persistence
 * FileName    : ViewCountScheduler
 * Author      : Baekgwa
 * Date        : 2025-04-08
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-08     Baekgwa               Initial creation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountScheduler {

	private final ViewCountRepository viewCountRepository;
	private final ViewCountStore viewCountStore;

	/**
	 * Redis 에 저장된, ViewCount 들을 영속성에 동기화 하는 스케줄러
	 */
	@Transactional
	@Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
	public void updateViewCount() {
		// 모든 ViewCountType 처리
		for (ViewCountType type : ViewCountType.values()) {
			processViewCountUpdates(type);
		}
	}

	/**
	 * 특정 ViewCountType 의 조회수 업데이트를 처리하는 메소드
	 * @param type 처리할 ViewCountType
	 */
	private void processViewCountUpdates(ViewCountType type) {
		// view_count 증가가 필요한 ID Set 검색
		Set<Long> dirtyIdSet = viewCountStore.getDirtyIdSet(type);

		// 없다면, 업데이트 종료
		if (dirtyIdSet.isEmpty()) {
			return;
		}

		// ID별 최신 조회수를 맵에 저장
		Map<Long, Long> idToViewCountMap = buildViewCountMap(type, dirtyIdSet);

		// 없다면, 업데이트 종료
		if (idToViewCountMap.isEmpty()) {
			return;
		}

		// 벌크 업데이트 수행
		int updateCount = viewCountRepository.bulkUpdateViewCounts(type, idToViewCountMap);

		// 처리 완료된 업데이트 대기 데이터 삭제 처리
		viewCountStore.removeUpdatedItem(type);
	}

	/**
	 * Redis에서 조회한 ID 목록에 대응하는 조회수 맵을 생성
	 * @param viewCountType 조회수 카테고리
	 * @param dirtyIdSet 업데이트할 ID 집합
	 * @return (key) ItemId : (value) view_count
	 * 이때 반환되는 view_count 는, db 에 추가되어야 할 값
	 */
	private Map<Long, Long> buildViewCountMap(ViewCountType viewCountType, Set<Long> dirtyIdSet) {
		Map<Long, Long> idToViewCountMap = new HashMap<>();
		for (Long itemId : dirtyIdSet) {
			Object countObj = viewCountStore.getCount(viewCountType, itemId);
			if (countObj != null) {
				Long count = Long.valueOf(countObj.toString());
				idToViewCountMap.put(itemId, count);
			}
		}
		return idToViewCountMap;
	}
}