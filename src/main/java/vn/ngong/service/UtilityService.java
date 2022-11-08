package vn.ngong.service;

import vn.ngong.dto.BannerDto;
import vn.ngong.dto.ThanhTuuVaGiaiThuong;
import vn.ngong.dto.hethongsxvaql.HeThongSXQL;
import vn.ngong.dto.lienhe.Address;
import vn.ngong.dto.soluocvengong.SoLuocVeNgongDto;
import vn.ngong.entity.*;

import java.util.List;
import java.util.Map;

public interface UtilityService {
	List<City> getAllCityDistrictWard();

	String getValue(String key);

	ShippingFee getShippingFee(String cityCode, String districtCode, int totalAmount);

	RegisterTrip addTrip(RegisterTrip registerTrip);

	RegisterProject registerProject(RegisterProject registerProject);

	List<Project> findAllProject();

	RegisterAgentCTV registerAgentCTV(RegisterAgentCTV registerAgentCTV);

	List<BannerDto> findAllBanner();

	SoLuocVeNgongDto getSoLuocVeNgongContent();

	List<ThanhTuuVaGiaiThuong> getAwardsNgong();

	HeThongSXQL getProductSystemContent();

	List<Address> getAddress();

	Question askQuestion(Question question);
}
