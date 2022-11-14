package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "transaction_notify")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TransactionNotify {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "tranx_id")
	private int tranxId;

	@Column(name = "tranx_code")
	private String tranxCode;

	@Column(name = "title")
	private String title;

	@Column(name = "image")
	private String image;

	@Column(name = "content")
	private String content;

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
