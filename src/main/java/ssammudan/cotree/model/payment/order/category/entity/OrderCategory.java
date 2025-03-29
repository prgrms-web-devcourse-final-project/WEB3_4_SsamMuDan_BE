package ssammudan.cotree.model.payment.order.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

/**
 * PackageName : ssammudan.cotree.model.payment.order.category.entity
 * FileName    : OrderCategory
 * Author      : SSamMuDan
 * Date        : 25. 3. 29.
 * Description : OrderCategory 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 29.    SSamMuDan            Initial creation
 */
@Entity
@Table(
	name = "order_category",
	uniqueConstraints = {
		@UniqueConstraint(name = "uq_product_type_name", columnNames = "name")
	}
)
@Getter
public class OrderCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

}
