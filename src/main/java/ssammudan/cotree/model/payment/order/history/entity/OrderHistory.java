package ssammudan.cotree.model.payment.order.history.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.payment.order.category.entity.OrderCategory;

/**
 * PackageName : ssammudan.cotree.model.payment.order.history.entity
 * FileName    : OrderHistory
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : OrderHistory 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
@Entity
@Table(
	name = "order_history",
	uniqueConstraints = {
		@UniqueConstraint(name = "uq_order_history_order_id", columnNames = "order_id")
	}
)
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "order_category_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_order_history_order_category_id")
	)
	private OrderCategory orderCategory;    //판매 제품 카테고리

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "member_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_order_history_member_id")
	)
	private Member customer;    //구매자

	@Column(nullable = false)
	private String orderId;    //주문 번호

	@Column(nullable = false)
	private Long productId;    //제품 ID

	@Column(nullable = false)
	private String productName;    //상품명

	@Column(nullable = false)
	private Integer price;    //가격

	public static OrderHistory create(
		final Member customer,
		final String orderId,
		final Long productId,
		final String productName,
		final Integer price
	) {
		//TODO: 구매자 연관관계 설정 확인 필요
		return OrderHistory.builder()
			.customer(customer)
			.orderId(orderId)
			.productId(productId)
			.productName(productName)
			.price(price)
			.build();
	}

}
