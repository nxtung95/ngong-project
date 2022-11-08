package vn.ngong.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThanhTuuVaGiaiThuong {
	private String time;
	private String content;
	private String imageUrl;
	private String targetUrl;
	private int status;
	private int order;
}
