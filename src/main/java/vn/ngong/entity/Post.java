package vn.ngong.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class Post {
	private int id;
	private int menuId;
	private String name;
	@Schema(name = "content_html", description = "Nội dung html để hiển thị trong 1 bài viết")
	private String contentHtml;
	private boolean isHtml;
	private int order;
	private int status;
}
