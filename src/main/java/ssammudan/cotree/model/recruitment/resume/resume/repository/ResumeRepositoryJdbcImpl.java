package ssammudan.cotree.model.recruitment.resume.resume.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.recruitment.resume.resume.repository
 * FileName    : ResumeRepositoryImpl
 * Author      : kwak
 * Date        : 2025. 4. 1.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 1.     kwak               Initial creation
 */
@RequiredArgsConstructor
public class ResumeRepositoryJdbcImpl implements ResumeRepositoryJdbc {

	private final JdbcTemplate jdbcTemplate;
	private static final String UPDATE_VIEW_COUNT_SQL = "UPDATE resume SET view_count = view_count + ? WHERE id = ?";
	private static final int MAX_BATCH_SIZE = 300;

	@Override
	public void bulkUpdateViewCount(Map<Long, Integer> viewCountData) {

		List<Map.Entry<Long, Integer>> entries = new ArrayList<>(viewCountData.entrySet());
		int totalSize = entries.size();
		int batchCount = (totalSize + MAX_BATCH_SIZE - 1) / MAX_BATCH_SIZE;

		for (int i = 0; i < batchCount; i++) {
			int fromIndex = i * MAX_BATCH_SIZE;
			int toIndex = Math.min((i + 1) * MAX_BATCH_SIZE, totalSize);

			final List<Map.Entry<Long, Integer>> batchEntries = entries.subList(fromIndex, toIndex);

			jdbcTemplate.batchUpdate(
				UPDATE_VIEW_COUNT_SQL,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Map.Entry<Long, Integer> entry = batchEntries.get(i);
						ps.setInt(1, entry.getValue());
						ps.setLong(2, entry.getKey());
					}

					@Override
					public int getBatchSize() {
						return batchEntries.size();
					}
				}
			);
		}
	}


}
