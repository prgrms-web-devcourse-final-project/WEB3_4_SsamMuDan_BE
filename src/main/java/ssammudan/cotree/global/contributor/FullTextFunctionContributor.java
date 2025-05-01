package ssammudan.cotree.global.contributor;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.spi.TypeConfiguration;

/**
 * PackageName : ssammudan.cotree.global.config.contributor
 * FileName    : FullTextFunctionContributor
 * Author      : Baekgwa
 * Date        : 2025-05-01
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-05-01     Baekgwa               Initial creation
 */
public class FullTextFunctionContributor implements FunctionContributor {

	@Override
	public void contributeFunctions(FunctionContributions functionContributions) {
		TypeConfiguration typeConfiguration = functionContributions.getTypeConfiguration();
		functionContributions.getFunctionRegistry().registerPattern(
			"full_text_boolean_search_param_3",
			"MATCH (?1, ?2, ?3) AGAINST (?4 IN BOOLEAN MODE)",
			typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE)
		);

		functionContributions.getFunctionRegistry().registerPattern(
			"full_text_boolean_search_param_2",
			"MATCH (?1, ?2) AGAINST (?3 IN BOOLEAN MODE)",
			typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE)
		);
	}
}

