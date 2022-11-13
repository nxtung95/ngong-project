package vn.ngong.dto.chuyendicuangong;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSend {
	private String author;
	private String date;
	private String rate;
	private String imageUrl;
	private String targetUrl;
	private String content;
	private String status;
	private int order;
}
