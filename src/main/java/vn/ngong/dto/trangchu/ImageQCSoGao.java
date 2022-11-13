package vn.ngong.dto.trangchu;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImageQCSoGao {
	private String imgSoGao;
	private String targetImgSoGao;
	private String content;
	private List<FeatureQCSoGao> feature;
}
