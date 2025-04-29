package ssammudan.cotree.domain.project.project.type;

import static com.querydsl.core.types.dsl.Expressions.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberTemplate;

import ssammudan.cotree.model.project.project.entity.QProject;

/**
 * PackageName : ssammudan.cotree.domain.project.project.type
 * FileName    : SearchProjectSort
 * Author      : sangxxjin
 * Date        : 2025. 4. 29.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 29.     sangxxjin               Initial creation
 */
public enum SearchProjectSort {
	LIKE {
		@Override
		public OrderSpecifier<?> getOrderSpecifier(QProject project) {
			NumberTemplate<Long> likeCountExpr = numberTemplate(Long.class,
				"(select count(l1.id) from Like l1 where l1.project.id = {0})", project.id);
			return likeCountExpr.desc();
		}
	},
	CREATED_AT {
		@Override
		public OrderSpecifier<?> getOrderSpecifier(QProject project) {
			return project.createdAt.desc();
		}
	};

	public abstract OrderSpecifier<?> getOrderSpecifier(QProject project);

	public static SearchProjectSort from(String value) {
		try {
			return SearchProjectSort.valueOf(value.toUpperCase());
		} catch (Exception e) {
			return CREATED_AT;
		}
	}
}