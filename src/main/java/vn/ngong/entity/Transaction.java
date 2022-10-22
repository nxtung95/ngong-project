package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "transactions")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "order_id")
	private int orderId;

	@Column(name = "payment_method_id")
	private String paymentMethodId;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "total_amount")
	private int totalAmount;

	@Column(name = "status")
	private int status;

	@Column(name = "created_date")
	@Builder.Default
	private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_date")
	@Builder.Default
	private Timestamp updatedDate = new Timestamp(System.currentTimeMillis());

	@Column(name = "updated_by")
	private String updatedBy;
}
