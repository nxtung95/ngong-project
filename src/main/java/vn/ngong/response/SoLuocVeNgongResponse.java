package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.BannerDto;
import vn.ngong.dto.soluocvengong.SoLuocVeNgongDto;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class SoLuocVeNgongResponse extends BaseResponse {
	private SoLuocVeNgongDto soLuocVeNgong;
}
