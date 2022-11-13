package vn.ngong.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerDto {
	private String title;
	private String content;
	private String imageUrl;
	private String targetUrl;
	private String status;
	private int order;
}
