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
import vn.ngong.entity.ShippingFee;
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
	private Map<Integer, String> imageMap = new HashMap<>();
	private Map<Integer, String> descriptionMap = new HashMap<>();
	private Map<String, List<SystemParameter>> configMap = new HashMap<>();
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

	public void loadSystemParameterList() {
		log.info("--------Start system parameter cache---------");
		Map<String, List<SystemParameter>> configMap = new HashMap<>();
		List<SystemParameter> systemParameters = systemParameterRepository.findAllByStatusOrderByOrderNumberAsc(1);
		for (SystemParameter s : systemParameters) {
			if (configMap.containsKey(s.getKey())) {
				List<SystemParameter> listSystem = configMap.get(s.getKey());
				listSystem.add(s);
				configMap.put(s.getKey(), listSystem);
			} else {
				List<SystemParameter> listSystem = new ArrayList<>();
				listSystem.add(s);
				configMap.put(s.getKey(), listSystem);
			}
		}
		this.configMap = configMap;
		log.info("--------End load system parameter cache, size: --------- " + systemParameters.size());

	}

	public void loadCacheMenu() {
		log.info("--------Start menu cache---------");
		menuList = menuRepository.findAllMenu();
		log.info("--------End load menu cache, size: --------- " + menuList.size());

	}

	public void loadCacheAllImagePost() {
		log.info("--------Start image post cache---------");
		imageMap = menuRepository.findAllImageRepresent();
		log.info("--------End load image post cache, size: --------- " + imageMap.size());

	}

	public void loadCacheAllDescriptionPost() {
		log.info("--------Start description post cache---------");
		descriptionMap = menuRepository.findAllDescription();
		log.info("--------End load description post cache, size: --------- " + descriptionMap.size());

	}

	public void loadCacheAllShippingFee() {
		log.info("--------Start shipping fee cache---------");
		shippingFeeList = shippingRepository.findAllByStatus(1);
		log.info("--------End load shipping fee cache, size: --------- " + shippingFeeList.size());

	}

	@Scheduled(cron = "0 * 0/1 * * ?")
	public void scheduleReloadCache() {
		loadPaymentMethodList();
		loadSystemParameterList();
		loadCityDistrictWardList();
		loadCacheMenu();
		loadCacheAllImagePost();
		loadCacheAllDescriptionPost();
		loadCacheAllShippingFee();
	}
}
