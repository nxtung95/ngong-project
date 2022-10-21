package vn.ngong.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Table(name = "wp_posts")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "post_author")
	private int postAuthor;

	@Column(name = "post_date")
	private Timestamp postDate;

	@Column(name = "post_date_gmt")
	private Timestamp postDateGmt;

	@Schema(name = "content", description = "Nội dung chi tiết bài viết")
	@Column(name = "post_content")
	private String postContent;

	@Schema(name = "title", description = "Title bài viết")
	@Column(name = "post_title")
	private String postTitle;

	@Column(name = "post_excerpt")
	private String postExcerpt;

	@Column(name = "post_status")
	private String postStatus;

	@Column(name = "comment_status")
	private String commentStatus;

	@Column(name = "ping_status")
	private String pingStatus;

	@Column(name = "post_password")
	private String postPassword;

	@Column(name = "post_name")
	private String postName;

	@Column(name = "to_ping")
	private String toPing;

	@Column(name = "pinged")
	private String pinged;

	@Column(name = "post_modified")
	private Timestamp postModified;

	@Column(name = "post_modified_gmt")
	private Timestamp postModifiedGmt;

	@Column(name = "post_content_filtered")
	private String postContentFiltered;

	@Column(name = "post_parent")
	private int postParent;

	@Column(name = "guid")
	private String guid;

	@Column(name = "menu_order")
	private int menuOrder;

	@Column(name = "post_type")
	private String postType;

	@Column(name = "post_mime_type")
	private String postMimeType;

	@Column(name = "comment_count")
	private int commentCount;
	
	@Transient
	@Schema(name = "image", description = "Ảnh đại diện 1 bài viết")
	private String menuImage;

	@Transient
	@Schema(name = "description", description = "Mô tả tóm tắt 1 bài viết")
	private String description;
}
