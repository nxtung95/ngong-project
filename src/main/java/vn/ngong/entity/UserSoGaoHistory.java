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
@Table(name = "wp_user_sogao_histories")
@NoArgsConstructor
public class UserSoGaoHistory {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_sogao_id")
	private int userSoGaoId;

	@Column(name = "transaction_id")
	private int transactionId;

	@Column(name = "used_number")
	private int usedNumber;

	@Column(name = "added_number")
	private int addedNumber;

	@Column(name = "remaining_number")
	private int remainingNumber;

	@Column(name = "note")
	private String note;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "updated_by")
	private int updatedBy;
}
