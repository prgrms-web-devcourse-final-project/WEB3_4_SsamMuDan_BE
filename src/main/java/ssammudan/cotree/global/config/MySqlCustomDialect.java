package ssammudan.cotree.global.config;

import static org.hibernate.type.StandardBasicTypes.*;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.dialect.MySQLDialect;

/**
 * PackageName : ssammudan.cotree.global.config
 * FileName    : MySqlCustomDialect
 * Author      : Baekgwa
 * Date        : 2025-04-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-29     Baekgwa               Initial creation
 */
public class MySqlCustomDialect extends MySQLDialect implements FunctionContributor {

	private static final String FUNCTION_NAME = "match_against";
	private static final String FUNCTION_PATTERN = "match (?1, ?2) against (?3 in boolean mode)";

	@Override
	public void contributeFunctions(FunctionContributions functionContributions) {
		functionContributions.getFunctionRegistry()
			.registerPattern(FUNCTION_NAME, FUNCTION_PATTERN,
				functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(DOUBLE));
	}
}

