package vn.ngong.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "user_sogaos")
@NoArgsConstructor
public class UserSoGao {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "sogao_id")
	private int soGaoid;

	@Column(name = "size")
	private int size;

	@Column(name = "expire_date")
	private Timestamp expireDate;

	@Column(name = "is_active")
	private int isActive;

	@Column(name = "is_admin")
	private int isAdmin;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;
}
