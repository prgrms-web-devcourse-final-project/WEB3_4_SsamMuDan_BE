package ssammudan.cotree.domain.order.service;

import ssammudan.cotree.model.payment.order.category.entity.OrderCategory;

/**
 * PackageName : ssammudan.cotree.domain.order.service
 * FileName    : OrderCategoryService
 * Author      : loadingKKamo21
 * Date        : 25. 4. 8.
 * Description : OrderCategory 서비스
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 8.     loadingKKamo21       Initial creation
 */
public interface OrderCategoryService {

	OrderCategory findOrderCategoryById(Long id);

}
