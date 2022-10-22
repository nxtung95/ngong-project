package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "payment_method")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "payment_type")
	private int paymentType;

	@Column(name = "name")
	private String name;

	@Column(name = "status")
	private int status;

	@Column(name = "order_number")
	private int orderNumber;

	@Column(name = "description")
	private String description;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;
}
