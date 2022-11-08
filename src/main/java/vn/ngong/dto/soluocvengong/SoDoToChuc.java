package vn.ngong.dto.soluocvengong;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SoDoToChuc {
	private KhoiThietKeVaThiCong khoiThietKeVaThiCong;
	private List<String> khoiSanXuat;
	private List<String> khoiHanhChinhKeToan;
	private List<String> khoiBanHang;
	private List<String> heThongShowroom;

}
