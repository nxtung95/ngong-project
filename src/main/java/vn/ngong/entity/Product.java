package vn.ngong.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "products")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "brand_name")
	private String brandName;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "description")
	private String description;

	@Column(name = "so_gao_flag")
	private int soGaoFlag;

	@Column(name = "status")
	private int status;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "updated_by")
	private int updatedBy;
}
