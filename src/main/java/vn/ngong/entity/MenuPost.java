package vn.ngong.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "wp_term_relationships")
@IdClass(MenuPostId.class)
public class MenuPost {
	@Id
	@Column(name = "term_taxonomy_id")
	private int menuId;
	@Id
	@Column(name = "object_id")
	private int postId;

	@Column(name = "term_order")
	private int termOrder;
}
