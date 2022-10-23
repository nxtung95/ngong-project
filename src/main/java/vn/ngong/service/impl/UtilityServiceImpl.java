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
	public String getValue(String key) {
		if (localCacheConfig.getConfigMap().isEmpty()) {
			localCacheConfig.loadSystemParameterMap();
		}

		return localCacheConfig.getConfig(key, "");
	}

	@Override
	public ShippingFee getShippingFee(String cityCode, String districtCode, int totalAmount) {
		if (localCacheConfig.getShippingFeeList() == null || localCacheConfig.getShippingFeeList().isEmpty()) {
			localCacheConfig.loadCacheAllShippingFee();
		}
		List<ShippingFee> shippingFeeList = localCacheConfig.getShippingFeeList();
		if (localCacheConfig.getConfigMap().isEmpty()) {
			localCacheConfig.loadSystemParameterMap();
		}

		String innerDistrictCity = localCacheConfig.getConfig("INNER_DISTRICT_CITY_SHIP",
				"001,021,005,006,007,002,008,019,003,009");
		String outerDistrictCity = localCacheConfig.getConfig("OUTER_DISTRICT_CITY_SHIP",
				"268,004");
		String feeDistrictCity = localCacheConfig.getConfig("DISTRICT_CITY_SHIP",
				"269,271,277,272,273,017,018,274,250,282,280,275,016,276,278,279,281,020");

		if (Arrays.asList(innerDistrictCity.split(",")).contains(districtCode)) {
			// District code for shipping fee discount
			String discountFeeDistrictCode = localCacheConfig.getConfig("DISCOUNT_DISTRICT_CODE_SHIP_FEE", "005");
			List<String> discountFeeDistrictCodeList = Arrays.asList(discountFeeDistrictCode.split(","));
			if (discountFeeDistrictCodeList.contains(districtCode)) {
				// Cầu giấy giảm phí ship
				int discountFee = Integer.parseInt(localCacheConfig.getConfig("DISCOUNT_DISTRICT_SHIP_FEE", "20000"));
				return ShippingFee.builder().fee(discountFee).build();
			}

			ShippingFee shippingFee = shippingFeeList.stream()
					.filter(s -> (s.getFromAmount() <= totalAmount && totalAmount <= s.getToAmount())
							&& s.getType() == 1
							&& s.getStatus() == 1
							&& s.getCustomerType() == 1)
					.findFirst().orElse(null);
			if (shippingFee == null) {
				shippingFee = shippingFeeList.stream()
						.filter(s -> (s.getFromAmount() <= totalAmount && s.getToAmount() == null)
								&& s.getType() == 1
								&& s.getStatus() == 1
								&& s.getCustomerType() == 1)
						.findFirst().orElse(null);
			}
			return shippingFee;
		} else if (Arrays.asList(outerDistrictCity.split(",")).contains(districtCode)) {
			ShippingFee shippingFee = shippingFeeList.stream()
					.filter(s -> (s.getFromAmount() <= totalAmount && totalAmount <= s.getToAmount())
							&& s.getType() == 2
							&& s.getStatus() == 1
							&& s.getCustomerType() == 1)
					.findFirst().orElse(null);
			if (shippingFee == null) {
				shippingFee = shippingFeeList.stream()
						.filter(s -> (s.getFromAmount() <= totalAmount && s.getToAmount() == null)
								&& s.getType() == 2
								&& s.getStatus() == 1
								&& s.getCustomerType() == 1)
						.findFirst().orElse(null);
			}
			return shippingFee;
		} else if (Arrays.asList(feeDistrictCity.split(",")).contains(districtCode)) {
			ShippingFee shippingFee = shippingFeeList.stream()
					.filter(s -> (s.getFromAmount() <= totalAmount && totalAmount <= s.getToAmount())
							&& s.getType() == 3
							&& s.getStatus() == 1
							&& s.getCustomerType() == 1)
					.findFirst().orElse(null);
			if (shippingFee == null) {
				shippingFee = shippingFeeList.stream()
						.filter(s -> (s.getFromAmount() <= totalAmount && s.getToAmount() == null)
								&& s.getType() == 3
								&& s.getStatus() == 1
								&& s.getCustomerType() == 1)
						.findFirst().orElse(null);
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
