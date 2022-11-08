package vn.ngong.dto.hethongsxvaql;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class HeThongSXQL {
	private List<DoiTacCuaNgong> partnerList;
	private HTKiemSoatChatLuong qualityControlSystem;
}
