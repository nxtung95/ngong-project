package vn.ngong.dto.chuyendicuangong;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KhamPhaTraiNghiem {
	private String title;
	private String imageUrl;
	private String targetUrl;
	private List<ContentKhamPhaTraiNghiem> content;
}
