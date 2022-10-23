package vn.ngong.service;

import vn.ngong.entity.City;
import vn.ngong.entity.ShippingFee;
import vn.ngong.entity.SystemParameter;

import java.util.List;

public interface UtilityService {
	List<City> getAllCityDistrictWard();

	String getValue(String key);

	ShippingFee getShippingFee(String cityCode, String districtCode, int totalAmount);
}
