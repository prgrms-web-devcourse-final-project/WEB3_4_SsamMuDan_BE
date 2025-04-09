package ssammudan.cotree.model.member.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ssammudan.cotree.domain.member.dto.MemberOrderResponse;
import ssammudan.cotree.domain.member.type.OrderProductCategoryType;

/**
 * PackageName : ssammudan.cotree.model.member.member.repository
 * FileName    : MemberRepositoryCustom
 * Author      : kwak
 * Date        : 2025. 4. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 8.     kwak               Initial creation
 */
public interface MemberRepositoryCustom {

	Page<MemberOrderResponse> getOrderList(Pageable pageable, OrderProductCategoryType type, String id);
}

