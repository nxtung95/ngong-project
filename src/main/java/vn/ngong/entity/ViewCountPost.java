package vn.ngong.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "view_count_posts")
@NoArgsConstructor
@AllArgsConstructor
public class ViewCountPost {
	@Id
	@Column(name = "post_id")
	private int postId;

	@Column(name = "view_count")
	private int viewCount;

	@Column(name = "slug")
	private String slug;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "updated_date")
	@Builder.Default
	private Timestamp updatedDate = new Timestamp(System.currentTimeMillis());

}
