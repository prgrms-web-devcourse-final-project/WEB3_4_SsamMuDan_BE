package ssammudan.cotree.domain.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.payment.order.category.entity.OrderCategory;
import ssammudan.cotree.model.payment.order.category.repository.OrderCategoryRepository;

/**
 * PackageName : ssammudan.cotree.domain.order.service
 * FileName    : OrderCategoryServiceImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 8.
 * Description : OrderCategory 서비스 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 8.     loadingKKamo21       Initial creation
 */
@Service
@RequiredArgsConstructor
public class OrderCategoryServiceImpl implements OrderCategoryService {

	private final OrderCategoryRepository orderCategoryRepository;

	@Transactional(readOnly = true)
	@Override
	public OrderCategory findOrderCategoryById(final Long id) {
		return orderCategoryRepository.findById(id)
			.orElseThrow(() -> new GlobalException(ErrorCode.ORDER_CATEGORY_NOT_FOUND));
	}

}
