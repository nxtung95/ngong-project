package vn.ngong.dto.canhdongsechia;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CanhDongSeChia {
	private Banner banner;
	private List<String> moDau;
	private List<Info> info;
	private List<SoGao> soGao;
	private Banner imageRegisTrip;
}
