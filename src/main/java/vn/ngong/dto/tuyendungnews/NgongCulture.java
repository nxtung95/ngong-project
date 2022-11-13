package vn.ngong.dto.tuyendungnews;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NgongCulture {
	private String title;
	private String content;
	private String imageUrl;
	private String targetUrl;
	private String status;
	private int order;
}
