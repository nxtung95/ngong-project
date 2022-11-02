package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.BannerDto;
import vn.ngong.entity.Project;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetBannerResponse extends BaseResponse {
	private List<BannerDto> bannerList;
}
