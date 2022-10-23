package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.cache.LocalCacheConfig;
import vn.ngong.entity.City;
import vn.ngong.entity.ShippingFee;
import vn.ngong.entity.SystemParameter;
import vn.ngong.service.UtilityService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UtilityServiceImpl implements UtilityService {
	@Autowired
	private LocalCacheConfig localCacheConfig;

	@Override
	public List<City> getAllCityDistrictWard() {
		if (localCacheConfig.getCityList() == null || localCacheConfig.getCityList().isEmpty()) {
			localCacheConfig.loadCityDistrictWardList();
		}
		return localCacheConfig.getCityList();
	}

	@Override
	public List<SystemParameter> getValue(String key) {
		if (localCacheConfig.getConfigMap().isEmpty()) {
			localCacheConfig.loadSystemParameterList();
		}
		Map<String, List<SystemParameter>> configMap = localCacheConfig.getConfigMap();

		return configMap.containsKey(key) ? configMap.get(key) : new ArrayList<>();
	}

	@Override
	public ShippingFee getShippingFee(String cityCode, String districtCode, int totalAmount) {
		if (localCacheConfig.getShippingFeeList() == null || localCacheConfig.getShippingFeeList().isEmpty()) {
			localCacheConfig.loadCacheAllShippingFee();
		}
		List<ShippingFee> shippingFeeList = localCacheConfig.getShippingFeeList();
		if (localCacheConfig.getConfigMap().isEmpty()) {
			localCacheConfig.loadSystemParameterList();
		}
		Map<String, List<SystemParameter>> configMap = localCacheConfig.getConfigMap();
		List<SystemParameter> innerDistrictCity;
		if (configMap.containsKey("INNER_DISTRICT_CITY_SHIP")) {
			innerDistrictCity = configMap.get("INNER_DISTRICT_CITY_SHIP");
		} else {
			innerDistrictCity = new ArrayList<>();
			innerDistrictCity.add(SystemParameter.builder()
					.key("INNER_DISTRICT_CITY_SHIP")
					.value("001,021,005,006,007,002,008,019,003,009")
					.build());

		}

		List<SystemParameter> outerDistrictCity;
		if (configMap.containsKey("INNER_DISTRICT_CITY_SHIP")) {
			outerDistrictCity = configMap.get("OUTER_DISTRICT_CITY_SHIP");
		} else {
			outerDistrictCity = new ArrayList<>();
			outerDistrictCity.add(SystemParameter.builder()
					.key("OUTER_DISTRICT_CITY_SHIP")
					.value("268,004")
					.build());

		}

		List<SystemParameter> feeDistrictCity;
		if (configMap.containsKey("DISTRICT_CITY_SHIP")) {
			feeDistrictCity = configMap.get("DISTRICT_CITY_SHIP");
		} else {
			feeDistrictCity = new ArrayList<>();
			feeDistrictCity.add(SystemParameter.builder()
					.key("OUTER_DISTRICT_CITY_SHIP")
					.value("269,271,277,272,273,017,018,274,250,282,280,275,016,276,278,279,281,020")
					.build());

		}

		if (Arrays.asList(innerDistrictCity.get(0).getValue().split(",")).contains(districtCode)) {
			ShippingFee shippingFee = shippingFeeList.stream()
					.filter(s -> (s.getFromAmount() <= totalAmount && totalAmount < s.getToAmount())
							&& s.getType() == 1 && s.getStatus() == 1
							&& s.getCustomerType() == 1)
					.findFirst().orElse(null);
			if (shippingFee == null) {
				//Free ship
				shippingFee.setFee(0);
			}
			return shippingFee;
		} else if (Arrays.asList(outerDistrictCity.get(0).getValue().split(",")).contains(districtCode)) {
			ShippingFee shippingFee = shippingFeeList.stream()
					.filter(s -> (s.getFromAmount() <= totalAmount && totalAmount < s.getToAmount())
							&& s.getType() == 2 && s.getStatus() == 1
							&& s.getCustomerType() == 1)
					.findFirst().orElse(null);
			if (shippingFee == null) {
				//Free ship
				shippingFee.setFee(0);
			}
			return shippingFee;
		} else if (Arrays.asList(feeDistrictCity.get(0).getValue().split(",")).contains(districtCode)) {
			ShippingFee shippingFee = shippingFeeList.stream()
					.filter(s -> (s.getFromAmount() <= totalAmount && totalAmount < s.getToAmount())
							&& s.getType() == 3 && s.getStatus() == 1
							&& s.getCustomerType() == 1)
					.findFirst().orElse(null);
			if (shippingFee == null) {
				//Free ship
				shippingFee.setFee(0);
			}
			return shippingFee;
		} else {
			ShippingFee shippingFee = shippingFeeList.stream()
					.filter(s -> (s.getFromAmount() <= totalAmount && totalAmount < s.getToAmount())
							&& s.getType() == 3 && s.getStatus() == 1
							&& s.getCustomerType() == 1)
					.findFirst().orElse(null);
			if (shippingFee == null) {
				//Free ship
				shippingFee.setFee(0);
			}
			return shippingFee;
		}
	}
}
