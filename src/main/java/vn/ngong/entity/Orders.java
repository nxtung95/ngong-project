package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "orders")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "customer_receiver_id")
	private int customerReceiverId;

	@Column(name = "origin_amount")
	private int originAmount;

	@Column(name = "discount_amount")
	private int discountAmount;

	@Column(name = "total_amount")
	private int totalAmount;

	@Column(name = "status")
	private int status;

	@Column(name = "created_date")
	@Builder.Default
	private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

	@Column(name = "created_by")
	private String createdBy;
}
