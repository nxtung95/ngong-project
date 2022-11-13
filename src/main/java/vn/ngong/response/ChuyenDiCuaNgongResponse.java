package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.chuyendicuangong.ChuyenDiCuaNgong;
import vn.ngong.dto.soluocvengong.SoLuocVeNgongDto;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ChuyenDiCuaNgongResponse extends BaseResponse {
	private ChuyenDiCuaNgong chuyenDiCuaNgong;
}
