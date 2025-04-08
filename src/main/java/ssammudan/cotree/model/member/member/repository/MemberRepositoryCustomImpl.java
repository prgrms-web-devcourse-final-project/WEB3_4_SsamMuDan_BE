package ssammudan.cotree.model.member.member.repository;

import static ssammudan.cotree.domain.member.type.OrderProductCategoryType.*;
import static ssammudan.cotree.model.education.techbook.techbook.entity.QTechBook.*;
import static ssammudan.cotree.model.education.techtube.techtube.entity.QTechTube.*;
import static ssammudan.cotree.model.member.member.entity.QMember.*;
import static ssammudan.cotree.model.payment.order.category.entity.QOrderCategory.*;
import static ssammudan.cotree.model.payment.order.history.entity.QOrderHistory.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.member.dto.MemberOrderResponse;
import ssammudan.cotree.domain.member.type.OrderProductCategoryType;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.payment.order.category.repository.OrderCategoryRepository;

/**
 * PackageName : ssammudan.cotree.model.member.member.repository
 * FileName    : MemberRepositoryCustomImpl
 * Author      : kwak
 * Date        : 2025. 4. 8.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 8.     kwak               Initial creation
 */
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private final OrderCategoryRepository orderCategoryRepository;

	@Override
	public Page<MemberOrderResponse> getOrderList(Pageable pageable, OrderProductCategoryType type,
		String memberId) {

		// FK 관계 없으므로 무결성 검증 로직 진행
		if (!orderCategoryRepository.existsById(type.getId())) {
			throw new GlobalException(ErrorCode.ORDER_CATEGORY_NOT_FOUND);
		}

		if (type == TECH_TUBE) {
			return getOrderTechTube(type.getId(), memberId, pageable);
		} else {
			return getOrderTechBook(type.getId(), memberId, pageable);
		}
	}

	private Page<MemberOrderResponse> getOrderTechTube(Long productTypeId, String memberId, Pageable pageable) {
		List<MemberOrderResponse> responses = jpaQueryFactory
			.select(
				Projections.constructor(
					MemberOrderResponse.class,
					orderHistory.productId,
					orderCategory.name,
					techTube.techTubeThumbnailUrl,
					techTube.title,
					techTube.description
				))
			.from(orderHistory)
			.join(orderHistory.customer, member)
			.join(orderHistory.orderCategory, orderCategory)
			.join(techTube).on(orderHistory.productId.eq(techTube.id))
			.where(
				member.id.eq(memberId),
				orderHistory.orderCategory.id.eq(productTypeId))
			.orderBy(orderHistory.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = getTotal(productTypeId, memberId);

		return new PageImpl<>(responses, pageable, total != null ? total : 0);
	}

	private Page<MemberOrderResponse> getOrderTechBook(Long productTypeId, String memberId, Pageable pageable) {
		List<MemberOrderResponse> responses = jpaQueryFactory
			.select(
				Projections.constructor(
					MemberOrderResponse.class,
					orderHistory.productId,
					orderCategory.name,
					techBook.techBookThumbnailUrl,
					techBook.title,
					techBook.description
				))
			.from(orderHistory)
			.join(orderHistory.customer, member)
			.join(orderHistory.orderCategory, orderCategory)
			.join(techBook).on(orderHistory.productId.eq(techBook.id))
			.where(
				member.id.eq(memberId),
				orderHistory.orderCategory.id.eq(productTypeId))
			.orderBy(orderHistory.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = getTotal(productTypeId, memberId);

		return new PageImpl<>(responses, pageable, total != null ? total : 0);
	}

	private Long getTotal(Long productTypeId, String memberId) {
		return jpaQueryFactory
			.select(orderHistory.count())
			.from(orderHistory)
			.join(orderHistory.customer, member)
			.where(
				orderHistory.customer.id.eq(memberId),
				orderHistory.orderCategory.id.eq(productTypeId))
			.fetchOne();
	}
}
