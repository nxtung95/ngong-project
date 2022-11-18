package vn.ngong.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.cache.LocalCacheConfig;
import vn.ngong.dto.BannerDto;
import vn.ngong.dto.PartnerDto;
import vn.ngong.dto.ThanhTuuVaGiaiThuong;
import vn.ngong.dto.canhdongsechia.CanhDongSeChia;
import vn.ngong.dto.canhdongsechia.Info;
import vn.ngong.dto.canhdongsechia.SoGao;
import vn.ngong.dto.chuyendicuangong.*;
import vn.ngong.dto.hethongsxvaql.DoiTacCuaNgong;
import vn.ngong.dto.hethongsxvaql.HTKiemSoatChatLuong;
import vn.ngong.dto.hethongsxvaql.HeThongSXQL;
import vn.ngong.dto.lienhe.Address;
import vn.ngong.dto.soluocvengong.*;
import vn.ngong.dto.trangchu.ImageQCSoGao;
import vn.ngong.dto.trangchuduan.TrangChuDuAn;
import vn.ngong.dto.tuyendungnews.NgongCulture;
import vn.ngong.dto.tuyendungnews.NgongJourney;
import vn.ngong.dto.tuyendungnews.TuyenDungNews;
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
	public BannerDto bannerMiddlePage() {
		String value = getValue("GET_BANNER_GIUA_TRANG");
		return gson.fromJson(value, BannerDto.class);
	}

	@Override
	public BannerDto bannerRightPage() {
		String value = getValue("BANNER_PHAI_CHI_TIET");
		return gson.fromJson(value, BannerDto.class);
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

	@Override
	public ImageQCSoGao getImageQCSoGao() {
		return gson.fromJson(getValue("QUANG_CAO_SO_GAO"), ImageQCSoGao.class);
	}

	@Override
	public List<PartnerDto> getPartner() {
		List<PartnerDto> partnerList = gson.fromJson(getValue("DOI_TAC_CUA_NGONG"), new TypeToken<List<DoiTacCuaNgong>>(){}.getType());
		return partnerList;
	}

	@Override
	public ChuyenDiCuaNgong getChuyenDiCuaNgongContent() {
		Banner banner = gson.fromJson(getValue("CHUYEN_DI_CUA_NGONG_BANNER"), Banner.class);
		String moDau = getValue("CHUYEN_DI_CUA_NGONG_MO_DAU");
		List<ThongDiep> thongDiepList = gson.fromJson(getValue("CHUYEN_DI_CUA_NGONG_CONTENT_1"), new TypeToken<List<ThongDiep>>(){}.getType());
		List<KhamPhaTraiNghiem> khamPhaTraiNghiemList = gson.fromJson(getValue("CHUYEN_DI_CUA_NGONG_CONTENT_2"), new TypeToken<List<KhamPhaTraiNghiem>>(){}.getType());
		List<CustomerSend> customerSendList = gson.fromJson(getValue("CHUYEN_DI_CUA_NGONG_CONTENT_3"), new TypeToken<List<CustomerSend>>(){}.getType());
		ImageBanner imageBanner = gson.fromJson(getValue("CHUYEN_DI_CUA_NGONG_CONTENT_4"), ImageBanner.class);

		return ChuyenDiCuaNgong.builder()
				.banner(banner)
				.moDau(moDau)
				.thongDiepList(thongDiepList)
				.khamPhaTraiNghiemList(khamPhaTraiNghiemList)
				.customerSendList(customerSendList)
				.imageRegisTrip(imageBanner)
				.build();
	}

	@Override
	public TrangChuDuAn getTrangChuDuAnContent() {
		ImageBanner banner = gson.fromJson(getValue("TRANG_CHU_DU_AN_BANNER"), ImageBanner.class);
		String moDau = getValue("TRANG_CHU_DU_AN_MO_DAU");
		List<Project> projectList = projectRepository.findAllByStatusOrderByStartDateAsc(1);
		return TrangChuDuAn.builder()
				.banner(banner)
				.moDau(moDau)
				.projectList(projectList)
				.build();
	}

	@Override
	public CanhDongSeChia getCanhDongSeChiaContent() {
		vn.ngong.dto.canhdongsechia.Banner banner = gson.fromJson(getValue("CANH_DONG_SE_CHIA_BANNER"), vn.ngong.dto.canhdongsechia.Banner.class);
		List<String> moDau = gson.fromJson(getValue("CANH_DONG_SE_CHIA_MO_DAU"), new TypeToken<List<String>>(){}.getType());
		List<Info> infos = gson.fromJson(getValue("CANH_DONG_SE_CHIA_CONTENT_1"), new TypeToken<List<Info>>(){}.getType());
		List<SoGao> soGaos = gson.fromJson(getValue("CANH_DONG_SE_CHIA_CONTENT_2"), new TypeToken<List<SoGao>>(){}.getType());
		vn.ngong.dto.canhdongsechia.Banner imageTrip = gson.fromJson(getValue("CANH_DONG_SE_CHIA_CONTENT_3"), vn.ngong.dto.canhdongsechia.Banner.class);

		return CanhDongSeChia.builder()
				.banner(banner)
				.moDau(moDau)
				.info(infos)
				.soGao(soGaos)
				.imageRegisTrip(imageTrip)
				.build();
	}

	@Override
	public TuyenDungNews getTuyenDungNewsContent() {
		List<NgongJourney> journeyList = gson.fromJson(getValue("TUYEN_DUNG_NEW_CONTENT_1"), new TypeToken<List<NgongJourney>>(){}.getType());
		List<NgongCulture> cultureList = gson.fromJson(getValue("TUYEN_DUNG_NEW_CONTENT_2"), new TypeToken<List<NgongCulture>>(){}.getType());
		return TuyenDungNews.builder()
				.cultureList(cultureList)
				.journeyList(journeyList)
				.build();
	}

	@Override
	public int getModeRegisterTrip() {
		int mode = Integer.parseInt(getValue("CHINH_SACH_DAI_LY_MODE_REGISTER_TRIP"));
		return mode;
	}

	@Override
	public List<CityAgentCTV> getAllCityAgentCTV() {
		if (localCacheConfig.getCityAgentCTVList().isEmpty()) {
			localCacheConfig.loadCityAgentCTVList();
		}
		return localCacheConfig.getCityAgentCTVList();
	}

	@Override
	public List<AgentCTV> getAgentCTVListByCity(int cityCode) {
		if (localCacheConfig.getAgentCTVList().isEmpty()) {
			localCacheConfig.loadAgentCTVList();
		}
		if (cityCode == 0) {
			return localCacheConfig.getAgentCTVList();
		}
		return localCacheConfig.getAgentCTVList().stream()
				.filter(a -> cityCode == a.getCityAgenCTVId()).collect(Collectors.toList());
	}
}
