package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "status")
	private int status;

	@Column(name = "created_at")
	@Builder.Default
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "target_url")
	private String targetUrl;

	@Column(name = "type")
	int type;
}
