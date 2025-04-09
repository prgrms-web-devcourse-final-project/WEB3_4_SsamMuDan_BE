package ssammudan.cotree.infra.viewcount.repository;

import java.util.Map;

import org.springframework.lang.NonNull;

import ssammudan.cotree.infra.viewcount.type.ViewCountType;

/**
 * PackageName : ssammudan.cotree.infra.viewcount.persistence
 * FileName    : ViewCountRepository
 * Author      : Baekgwa
 * Date        : 2025-04-09
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-09     Baekgwa               Initial creation
 */
public interface ViewCountRepository {
	int bulkUpdateViewCounts(@NonNull ViewCountType type, @NonNull Map<Long, Long> idToViewCountMap);
}
