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
@Table(name = "cities")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class City implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "order_number")
	private int orderNumber;

	@Column(name = "status")
	private int status;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@OneToMany(fetch = FetchType.EAGER, targetEntity = District.class)
	@JoinColumn(name = "city_code", referencedColumnName = "code", insertable = false, updatable = false)
	@Where(clause = "status = 1")
	@OrderBy("orderNumber ASC")
	private List<District> districtList;
}
