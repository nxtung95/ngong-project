package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "register_agent_ctv")
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAgentCTV {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	private String email;

	@Column(name = "description")
	private String description;

	@Column(name = "created_at")
	@Builder.Default
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

	@Column(name = "updated_at")
	private Timestamp updatedAt;
}
