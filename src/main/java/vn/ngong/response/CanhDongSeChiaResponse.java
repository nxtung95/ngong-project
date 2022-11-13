package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.canhdongsechia.CanhDongSeChia;
import vn.ngong.dto.trangchuduan.TrangChuDuAn;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class CanhDongSeChiaResponse extends BaseResponse {
	private CanhDongSeChia canhDongSeChia;
}
