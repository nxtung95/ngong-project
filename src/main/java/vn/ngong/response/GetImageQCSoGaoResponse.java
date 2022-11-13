package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.BannerDto;
import vn.ngong.dto.trangchu.FeatureQCSoGao;
import vn.ngong.dto.trangchu.ImageQCSoGao;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetImageQCSoGaoResponse extends BaseResponse {
	private ImageQCSoGao imageQCSoGao;
}
