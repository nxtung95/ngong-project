package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "system_parameter")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SystemParameter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "key")
	private String key;

	@Column(name = "value")
	private String value;

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
