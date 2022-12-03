package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.City;
import vn.ngong.entity.District;
import vn.ngong.entity.Ward;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class CityDistrictWardResponse extends BaseResponse{
	private List<City> cityList;
	private List<District> districtList;
	private List<Ward> wardList;
}
