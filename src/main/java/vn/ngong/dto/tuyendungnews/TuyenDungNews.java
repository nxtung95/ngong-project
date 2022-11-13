package vn.ngong.dto.tuyendungnews;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TuyenDungNews {
	private List<NgongJourney> journeyList;
	private List<NgongCulture> cultureList;
}
