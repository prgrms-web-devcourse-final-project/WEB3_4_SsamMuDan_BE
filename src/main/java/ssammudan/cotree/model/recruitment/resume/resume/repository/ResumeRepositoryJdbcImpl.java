package ssammudan.cotree.model.recruitment.resume.resume.repository;

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
 * 2025. 4. 9.     Baekgwa             ViewCount 증가 `ViewCountStore`, `ViewCountScheduler` 에서 통합 관리 진행
 */
@RequiredArgsConstructor
public class ResumeRepositoryJdbcImpl implements ResumeRepositoryJdbc {

	private final JdbcTemplate jdbcTemplate;
	private static final String UPDATE_VIEW_COUNT_SQL = "UPDATE resume SET view_count = view_count + ? WHERE id = ?";
	private static final int MAX_BATCH_SIZE = 300;
}
