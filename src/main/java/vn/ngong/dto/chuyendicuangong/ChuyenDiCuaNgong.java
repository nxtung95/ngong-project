package vn.ngong.dto.chuyendicuangong;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChuyenDiCuaNgong {
	private Banner banner;
	private String moDau;
	private List<ThongDiep> thongDiepList;
	private List<CustomerSend> customerSendList;
	private ImageBanner imageRegisTrip;
	private List<KhamPhaTraiNghiem> khamPhaTraiNghiemList;
}
