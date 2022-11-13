package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.chuyendicuangong.ChuyenDiCuaNgong;
import vn.ngong.dto.trangchuduan.TrangChuDuAn;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class TrangChuDuAnResponse extends BaseResponse {
	private TrangChuDuAn trangChuDuAn;
}
