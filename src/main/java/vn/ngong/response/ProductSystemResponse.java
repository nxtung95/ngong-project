package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.dto.ThanhTuuVaGiaiThuong;
import vn.ngong.dto.hethongsxvaql.HeThongSXQL;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ProductSystemResponse extends BaseResponse {
	private HeThongSXQL productSystem;
}
