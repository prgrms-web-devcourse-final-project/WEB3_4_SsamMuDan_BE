package ssammudan.cotree.infra.viewcount.persistence;

import java.util.Set;

import ssammudan.cotree.infra.viewcount.type.ViewCountType;

/**
 * PackageName : ssammudan.cotree.infra.viewcount
 * FileName    : ViewCountStore
 * Author      : Baekgwa
 * Date        : 2025-04-08
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-08     Baekgwa               Initial creation
 */
public interface ViewCountStore {

	void incrementViewCount(ViewCountType category, Long itemId);

	Set<Long> getDirtyIdSet(ViewCountType category);

	Object getCount(ViewCountType category, Long itemId);

	void removeUpdatedItem(ViewCountType category);
}
