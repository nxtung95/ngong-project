package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "detail_orders")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "product_code")
	private String productCode;

	@Column(name = "order_id")
	private int orderId;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "amount")
	private int amount;

	@Column(name = "amount_discount")
	private int amountDiscount;

	@Column(name = "created_date")
	@Builder.Default
	private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

	@Column(name = "created_by")
	private String createdBy;
}