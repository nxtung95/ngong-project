package vn.ngong.dto.chuyendicuangong;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Banner {
	private List<ImageBanner> images;
	private String content;
}
