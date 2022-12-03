package vn.ngong.service;

import vn.ngong.dto.BannerDto;
import vn.ngong.dto.PartnerDto;
import vn.ngong.dto.ThanhTuuVaGiaiThuong;
import vn.ngong.dto.canhdongsechia.CanhDongSeChia;
import vn.ngong.dto.chuyendicuangong.ChuyenDiCuaNgong;
import vn.ngong.dto.footer.FooterDto;
import vn.ngong.dto.hethongsxvaql.HeThongSXQL;
import vn.ngong.dto.lienhe.Address;
import vn.ngong.dto.muaodau.MuaODauDto;
import vn.ngong.dto.soluocvengong.SoLuocVeNgongDto;
import vn.ngong.dto.trangchu.ImageQCSoGao;
import vn.ngong.dto.trangchuduan.TrangChuDuAn;
import vn.ngong.dto.tuyendungnews.TuyenDungNews;
import vn.ngong.entity.*;

import java.util.List;
import java.util.Map;

public interface UtilityService {
	List<City> getAllCity();

	List<District> getAllDistrictByCity(String cityCode);

	List<Ward> getAllWardByDistrict(String districtCode);

	String getValue(String key);

	RegisterTrip addTrip(RegisterTrip registerTrip);

	RegisterProject registerProject(RegisterProject registerProject);

	List<Project> findAllProject(int type);

	RegisterAgentCTV registerAgentCTV(RegisterAgentCTV registerAgentCTV);

	List<BannerDto> findAllBanner();

	BannerDto bannerMiddlePage();

	BannerDto bannerRightPage();

	SoLuocVeNgongDto getSoLuocVeNgongContent();

	List<ThanhTuuVaGiaiThuong> getAwardsNgong();

	HeThongSXQL getProductSystemContent();

	List<Address> getAddress();

	Question askQuestion(Question question);

	ImageQCSoGao getImageQCSoGao();

	List<PartnerDto> getPartner();

	ChuyenDiCuaNgong getChuyenDiCuaNgongContent();

	TrangChuDuAn getTrangChuDuAnContent();

	CanhDongSeChia getCanhDongSeChiaContent();

	TuyenDungNews getTuyenDungNewsContent();

	int getModeRegisterTrip();

    List<CityAgentCTV> getAllCityAgentCTV();

	List<AgentCTV> getAgentCTVListByCity(int cityCode);

	MuaODauDto getMuaODauContent();

	FooterDto getFooterContent();

	List<Feedback> getAllFeedback();
}
