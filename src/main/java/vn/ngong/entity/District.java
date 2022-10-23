package vn.ngong.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "districts")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class District implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "city_code")
	private String cityCode;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "status")
	private int status;

	@Column(name = "order_number")
	private int orderNumber;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@OneToMany(fetch = FetchType.EAGER, targetEntity = Ward.class)
	@JoinColumn(name = "district_code", referencedColumnName = "code", insertable = false, updatable = false)
	@Where(clause = "status = 1")
	@OrderBy("orderNumber ASC")
	private List<Ward> wardList;
}
