package vn.ngong.service;

import vn.ngong.dto.BannerDto;
import vn.ngong.entity.*;

import java.util.List;

public interface UtilityService {
	List<City> getAllCityDistrictWard();

	String getValue(String key);

	ShippingFee getShippingFee(String cityCode, String districtCode, int totalAmount);

	RegisterTrip addTrip(RegisterTrip registerTrip);

	RegisterProject registerProject(RegisterProject registerProject);

	List<Project> findAllProject();

	RegisterAgentCTV registerAgentCTV(RegisterAgentCTV registerAgentCTV);

	List<BannerDto> findAllBanner();
}
