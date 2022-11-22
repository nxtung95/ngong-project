package vn.ngong.cache;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.ngong.dto.MenuDto;
import vn.ngong.entity.*;
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
	private List<CityAgentCTV> cityAgentCTVList = new ArrayList<>();
	private List<AgentCTV> agentCTVList = new ArrayList<>();

	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private DistrictRepository districtRepository;
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;
	@Autowired
	private SystemParameterRepository systemParameterRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private CityAgentCTVRepository cityAgentCTVRepository;
	@Autowired
	private AgentCTVRepository agentCTVRepository;

	public void loadAgentCTVList() {
		log.info("--------Start agent ctv cache---------");
		agentCTVList = agentCTVRepository.findAllByStatusOrderByOrderNumberAsc(1);
		log.info("--------End load agent ctv cache, size: --------- " + agentCTVList.size());

	}

	public void loadCityAgentCTVList() {
		log.info("--------Start city agent ctv cache---------");
		cityAgentCTVList = cityAgentCTVRepository.findAllByStatusOrderByOrderNumberAsc(1);
		log.info("--------End load city agent ctv cache, size: --------- " + cityAgentCTVList.size());

	}

	public void loadCityDistrictWardList() {
		log.info("--------Start load city district ward cache---------");
		cityList = cityRepository.findAllByStatusOrderByOrderNumberAsc(1);
//		wardList = wardRepository.findAllByStatusOrderByOrderNumberAsc(1);
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
		loadCityAgentCTVList();
		loadAgentCTVList();
	}

	public String getPaymentNameById(String defaultPaymentId) {
		if (this.paymentMethodList.isEmpty()) {
			loadPaymentMethodList();
		}
		PaymentMethod paymentMethod = this.paymentMethodList.stream().filter(f -> defaultPaymentId.equals(f.getId())).findFirst().orElse(null);
		if (paymentMethod == null) {
			return null;
		}

		return paymentMethod.getName();
	}
}
