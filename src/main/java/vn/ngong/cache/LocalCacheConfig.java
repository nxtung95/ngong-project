package vn.ngong.cache;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.ngong.dto.MenuDto;
import vn.ngong.entity.City;
import vn.ngong.entity.PaymentMethod;
import vn.ngong.entity.SystemParameter;
import vn.ngong.repository.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Getter
@EnableScheduling
public class LocalCacheConfig {
	private List<City> cityList = new ArrayList<>();
	private List<PaymentMethod> paymentMethodList = new ArrayList<>();
	private List<MenuDto> menuList = new ArrayList<>();
	private Map<String, String> configMap = new HashMap<>();
	private List<ShippingFee> shippingFeeList = new ArrayList<>();

	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;
	@Autowired
	private SystemParameterRepository systemParameterRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private ShippingRepository shippingRepository;

	public void loadCityDistrictWardList() {
		log.info("--------Start load city cache---------");
		cityList = cityRepository.findAllByStatusOrderByOrderNumberAsc(1);
		log.info("--------End load city cache, size: --------- " + cityList.size());

	}

	public void loadPaymentMethodList() {
		log.info("--------Start payment method cache---------");
		paymentMethodList = paymentMethodRepository.findAllByStatusOrderByOrderNumberAsc(1);
		log.info("--------End load payment method cache, size: --------- " + paymentMethodList.size());

	}

	public void loadSystemParameterMap() {
		log.info("--------Start system parameter cache---------");
		Map<String, String> configMap = new HashMap<>();
		List<SystemParameter> systemParameters = systemParameterRepository.findAllByStatus(1);
		for (SystemParameter s : systemParameters) {
			configMap.put(s.getKey(), s.getValue());
		}
		this.configMap = configMap;
		log.info("--------End load system parameter cache, size: --------- " + configMap.size());

	}

	public void loadCacheMenu() {
		log.info("--------Start menu cache---------");
		menuList = menuRepository.findAllMenu();
		log.info("--------End load menu cache, size: --------- " + menuList.size());

	}

	public void loadCacheAllShippingFee() {
		log.info("--------Start shipping fee cache---------");
		shippingFeeList = shippingRepository.findAllByStatus(1);
		log.info("--------End load shipping fee cache, size: --------- " + shippingFeeList.size());

	}

	public String getConfig(String key, String defaultValue) {
		String config;
		if (configMap.containsKey(key)) {
			config = configMap.get(key);
		} else {
			config = defaultValue;

		}
		return config;
	}

	@Scheduled(cron = "0 0 0/1 * * ?")
	public void scheduleReloadCache() {
		loadPaymentMethodList();
		loadSystemParameterMap();
		loadCityDistrictWardList();
		loadCacheMenu();
		loadCacheAllShippingFee();
	}
}
