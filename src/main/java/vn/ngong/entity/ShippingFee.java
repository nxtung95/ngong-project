package vn.ngong.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "shipping_fee")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShippingFee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Schema(name = "cusType", description = "0: Khách hàng cá nhân")
	@Column(name = "customer_type")
	private int customerType;

	@Column(name = "from_amount")
	private Integer fromAmount;

	@Column(name = "to_amount")
	private Integer toAmount;

	@Schema(name = "type", description = "1: Đơn nội thành, 2: Đơn ngoại thành, 3: Đơn huyện tỉnh, 4: Đơn gửi VNPOST")
	@Column(name = "type")
	private int type;

	@Column(name = "fee")
	private int fee;

	@Column(name = "status")
	private int status;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;
}
