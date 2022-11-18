package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.CityAgentCTV;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetAllCityAgentCTVResponse extends BaseResponse {
	private List<CityAgentCTV> cityAgentCTVList;
}
