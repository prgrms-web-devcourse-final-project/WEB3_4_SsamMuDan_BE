package ssammudan.cotree.model.payment.order.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PackageName : ssammudan.cotree.model.payment.order.category.entity
 * FileName    : OrderCategory
 * Author      : loadingKKamo21
 * Date        : 25. 3. 29.
 * Description : OrderCategory 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    loadingKKamo21       Initial creation
 */
@Entity
@Table(
	name = "order_category",
	uniqueConstraints = {
		@UniqueConstraint(name = "uq_product_type_name", columnNames = "name")
	}
)
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;    //판매 제품 타입

	public static OrderCategory create(final String name) {
		return OrderCategory.builder().name(name).build();
	}

	/**
	 * OrderCategory 엔티티 정보 수정
	 *
	 * @param newName - 새로운 판매 제품 타입 명칭
	 * @return this
	 */
	public OrderCategory modify(final String newName) {
		if (!this.name.equals(newName) && !newName.isBlank()) {
			this.name = newName;
		}
		return this;
	}

}
