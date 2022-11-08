package vn.ngong.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.cache.LocalCacheConfig;
import vn.ngong.dto.BannerDto;
import vn.ngong.dto.ThanhTuuVaGiaiThuong;
import vn.ngong.dto.hethongsxvaql.DoiTacCuaNgong;
import vn.ngong.dto.hethongsxvaql.HTKiemSoatChatLuong;
import vn.ngong.dto.hethongsxvaql.HeThongSXQL;
import vn.ngong.dto.lienhe.Address;
import vn.ngong.dto.soluocvengong.*;
import vn.ngong.entity.*;
import vn.ngong.repository.*;
import vn.ngong.service.UtilityService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UtilityServiceImpl implements UtilityService {
	@Autowired
	private LocalCacheConfig localCacheConfig;
	@Autowired
	private RegisterTripRepository registerTripRepository;
	@Autowired
	private RegisterProjectRepository registerProjectRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private RegisterAgentCTVRepository registerAgentCTVRepository;
	@Autowired
	private Gson gson;
	@Autowired
	private QuestionRepository questionRepository;

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
	public RegisterTrip addTrip(RegisterTrip registerTrip) {
		try {
			return registerTripRepository.saveAndFlush(registerTrip);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public RegisterProject registerProject(RegisterProject registerProject) {
		try {
			return registerProjectRepository.saveAndFlush(registerProject);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<Project> findAllProject() {
		return projectRepository.findAllByStatusOrderByStartDateAsc(1);
	}

	@Override
	public RegisterAgentCTV registerAgentCTV(RegisterAgentCTV registerAgentCTV) {
		try {
			return registerAgentCTVRepository.saveAndFlush(registerAgentCTV);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<BannerDto> findAllBanner() {
		String value = getValue("GET_BANNER");
		List<BannerDto> bannerDtoList = gson.fromJson(value,  new TypeToken<List<BannerDto>>(){}.getType());

		return bannerDtoList.stream().filter(b -> "1".equals(b.getStatus())).sorted(Comparator.comparingInt(b -> b.getOrder())).collect(Collectors.toList());
	}

	@Override
	public SoLuocVeNgongDto getSoLuocVeNgongContent() {
		String moDau = getValue("SO_LUOC_VE_NGONG_MO_DAU");
		TamNhinSuMenhGTCL tamNhinSM = gson.fromJson(getValue("SO_LUOC_VE_NGONG_CONTENT_1"), TamNhinSuMenhGTCL.class);
		CauChuyenThuongHieu cauChuyenThuongHieu = gson.fromJson(getValue("SO_LUOC_VE_NGONG_CONTENT_2"), CauChuyenThuongHieu.class);
		List<QTHinhThanhPhatTrien> qtHinhThanhPT = gson.fromJson(getValue("SO_LUOC_VE_NGONG_CONTENT_3"), new TypeToken<List<QTHinhThanhPhatTrien>>(){}.getType());
		DinhHuongLinhVuc dinhHuongPT = gson.fromJson(getValue("SO_LUOC_VE_NGONG_CONTENT_4"), DinhHuongLinhVuc.class);
		SoDoToChuc soDoToChuc = gson.fromJson(getValue("SO_LUOC_VE_NGONG_CONTENT_5"), SoDoToChuc.class);
		List<CamKetKhachHang> camKetKH = gson.fromJson(getValue("SO_LUOC_VE_NGONG_CONTENT_6"), new TypeToken<List<CamKetKhachHang>>(){}.getType());

		return SoLuocVeNgongDto.builder()
				.moDau(moDau)
				.tamNhinSuMenhGTCL(tamNhinSM)
				.cauChuyenThuongHieu(cauChuyenThuongHieu)
				.qtHinhThanhPhatTrien(qtHinhThanhPT)
				.dinhHuongLinhVuc(dinhHuongPT)
				.soDoToChuc(soDoToChuc)
				.camKetKhachHang(camKetKH)
				.build();
	}

	@Override
	public List<ThanhTuuVaGiaiThuong> getAwardsNgong() {
		List<ThanhTuuVaGiaiThuong> thanhTuu = gson.fromJson(getValue("THANH_TUU_VA_GIAI_THUONG"), new TypeToken<List<ThanhTuuVaGiaiThuong>>(){}.getType());
		return thanhTuu;
	}

	@Override
	public HeThongSXQL getProductSystemContent() {
		List<DoiTacCuaNgong> partnerList = gson.fromJson(getValue("DOI_TAC_CUA_NGONG"), new TypeToken<List<DoiTacCuaNgong>>(){}.getType());
		HTKiemSoatChatLuong quality = gson.fromJson(getValue("HETHONG_KIEM_SOAT_CHAT_LUONG"), HTKiemSoatChatLuong.class);
		return HeThongSXQL.builder()
				.partnerList(partnerList)
				.qualityControlSystem(quality)
				.build();
	}

	@Override
	public List<Address> getAddress() {
		List<Address> addresses = gson.fromJson(getValue("LIEN_HE"), new TypeToken<List<Address>>(){}.getType());
		return addresses;
	}

	@Override
	public Question askQuestion(Question question) {
		try {
			return questionRepository.saveAndFlush(question);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
