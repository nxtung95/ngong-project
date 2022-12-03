package vn.ngong.dto.soluocvengong;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.dto.ThanhTuuVaGiaiThuong;

import java.util.List;

@Getter
@Setter
@Builder
public class SoLuocVeNgongDto {
	private String banner;
	private String moDau;
	private TamNhinSuMenhGTCL tamNhinSuMenhGTCL;
	private CauChuyenThuongHieu cauChuyenThuongHieu;
	private List<QTHinhThanhPhatTrien> qtHinhThanhPhatTrien;
	private DinhHuongLinhVuc dinhHuongLinhVuc;
	private SoDoToChuc soDoToChuc;
	private List<CamKetKhachHang> camKetKhachHang;
}
