package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.canhdongsechia.CanhDongSeChia;
import vn.ngong.dto.tuyendungnews.TuyenDungNews;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class TuyenDungNewsResponse extends BaseResponse {
	private TuyenDungNews tuyenDungNews;
}
