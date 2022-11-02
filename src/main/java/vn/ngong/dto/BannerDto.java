package vn.ngong.dto;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BannerDto {
	private int id;
	private String image;
	private int status;
}
