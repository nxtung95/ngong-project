package vn.ngong.dto;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BannerDto {
	private int id;
	private String imageUrl;
	private String targetUrl;
	private String status;
	private int order;
}
