package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "register_projects")
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	private String email;

	@Column(name = "project_id")
	private int projectId;

	@Column(name = "feedback")
	private String feedback;

	@Column(name = "created_at")
	@Builder.Default
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

	@Column(name = "updated_at")
	private Timestamp updatedAt;
}
