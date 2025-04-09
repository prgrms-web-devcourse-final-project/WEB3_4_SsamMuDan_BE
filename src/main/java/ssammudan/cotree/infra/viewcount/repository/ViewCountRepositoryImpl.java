package ssammudan.cotree.infra.viewcount.repository;

import static ssammudan.cotree.infra.viewcount.constant.ViewCountConstant.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.infra.viewcount.type.ViewCountType;

/**
 * PackageName : ssammudan.cotree.infra.viewcount.repository
 * FileName    : ViewCountRepositoryImpl
 * Author      : Baekgwa
 * Date        : 2025-04-09
 * Description : 영속성 DB 에 조회수 반영하는 Repository
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-09     Baekgwa               Initial creation
 */
@Component
@RequiredArgsConstructor
public class ViewCountRepositoryImpl implements ViewCountRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	/**
	 * 주어진 도메인 타입과 ID별 증가된 조회수 데이터를 기반으로 DB의 조회수를 업데이트한다.
	 * @param type 조회수 업데이트 대상 도메인 타입
	 * @param idToViewCountMap ID별 증가된 조회수 맵
	 * @return 업데이트된 row 수
	 */
	@Override
	public int bulkUpdateViewCounts(@NonNull ViewCountType type, @NonNull Map<Long, Long> idToViewCountMap) {

		// 쿼리 적용할 테이블 이름 검색
		String targetTableName = getTableName(type);

		// 현재 DB의 view_count 조사 및 update 될 view_count 계산
		Map<Long, Long> currentCounts = findCurrentViewCountMap(targetTableName, idToViewCountMap.keySet());
		Map<Long, Long> updatedCounts = calculateUpdatedCountMap(currentCounts, idToViewCountMap);

		// 업데이트 쿼리문 및 params 빌딩
		String updateSql = buildUpdateQuery(targetTableName, updatedCounts);
		Map<String, Object> updateParams = buildUpdateParams(updatedCounts);

		// query 적용 및 처리 개수 반환
		return jdbcTemplate.update(updateSql, updateParams);
	}

	/**
	 * ViewCountType 에 따른 실제 테이블 이름을 반환한다.
	 * @param type 도메인 타입
	 * @return 매핑된 테이블 이름
	 */
	private String getTableName(ViewCountType type) {
		return switch (type) {
			case PROJECT -> PROJECT_TABLE_NAME;
			case RESUME -> RESUME_TABLE_NAME;
			case TECH_TUBE -> TECH_TUBE_TABLE_NAME;
			case TECH_BOOK -> TECH_BOOK_TABLE_NAME;
			case COMMUNITY -> COMMUNITY_TABLE_NAME;
		};
	}

	/**
	 * 주어진 ID 목록을 기반으로 현재 DB에 저장된 조회수를 조회한다.
	 * @param tableName 조회 대상 테이블명
	 * @param ids 조회할 ID 목록
	 * @return ID별 현재 조회수 맵
	 */
	private Map<Long, Long> findCurrentViewCountMap(String tableName, Set<Long> ids) {
		String selectSql = "SELECT id, view_count FROM " + tableName + " WHERE id IN (:ids)";
		Map<String, Object> params = Map.of("ids", ids);

		Map<Long, Long> result = new HashMap<>();
		jdbcTemplate.query(selectSql, params, rs -> {
			result.put(rs.getLong("id"), rs.getLong("view_count"));
		});
		return result;
	}

	/**
	 * Redis 에서 가져온 증분 데이터와 DB의 현재 값을 기반으로 최종 업데이트할 조회수를 계산한다.
	 * @param current 현재 DB에 저장된 값
	 * @param increment Redis 에서 가져온 증분 값
	 * @return ID별 최종 업데이트할 조회수 맵
	 */
	private Map<Long, Long> calculateUpdatedCountMap(Map<Long, Long> current, Map<Long, Long> increment) {
		Map<Long, Long> result = new HashMap<>();
		for (Map.Entry<Long, Long> entry : increment.entrySet()) {
			Long id = entry.getKey();
			Long newCount = current.getOrDefault(id, 0L) + entry.getValue();
			result.put(id, newCount);
		}
		return result;
	}

	/**
	 * 동적 CASE 쿼리를 생성하여 ID별로 조회수를 개별 업데이트하는 SQL을 생성한다.
	 * @param tableName 대상 테이블 이름
	 * @param updatedCounts 최종 업데이트할 조회수 맵
	 * @return 완성된 UPDATE SQL
	 */
	private String buildUpdateQuery(String tableName, Map<Long, Long> updatedCounts) {
		StringBuilder caseClause = new StringBuilder("CASE id ");
		for (Long id : updatedCounts.keySet()) {
			caseClause.append("WHEN :id").append(id)
				.append(" THEN :count").append(id).append(" ");
		}
		caseClause.append("ELSE view_count END");

		return "UPDATE " + tableName + " SET view_count = " + caseClause + " WHERE id IN (:ids)";
	}

	/**
	 * SQL에 사용할 파라미터 맵을 구성한다.
	 * @param updatedCounts 최종 업데이트할 조회수 맵
	 * @return SQL 바인딩용 파라미터 맵
	 */
	private Map<String, Object> buildUpdateParams(Map<Long, Long> updatedCounts) {
		Map<String, Object> params = new HashMap<>();
		params.put("ids", updatedCounts.keySet());
		for (Map.Entry<Long, Long> entry : updatedCounts.entrySet()) {
			params.put("id" + entry.getKey(), entry.getKey());
			params.put("count" + entry.getKey(), entry.getValue());
		}
		return params;
	}
}
