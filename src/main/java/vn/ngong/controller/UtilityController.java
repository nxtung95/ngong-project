package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.entity.ShippingFee;
import vn.ngong.entity.SystemParameter;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.request.GetConfigRequest;
import vn.ngong.request.ShippingFeeRequest;
import vn.ngong.response.CityDistrictWardResponse;
import vn.ngong.response.GetConfigResponse;
import vn.ngong.response.GetShippingFeeResponse;
import vn.ngong.service.UtilityService;

import java.util.*;

@RestController
@Slf4j
@RequestMapping(value = "/utility")
public class UtilityController {
	@Autowired
	private UtilityService utilityService;


	@Operation(summary = "API Lấy danh sách tỉnh thành/quận huyện/phường xã")
	@RequestMapping(value = "/city", method = RequestMethod.GET)
	public ResponseEntity<CityDistrictWardResponse> getCityDistrictWard() throws Exception {
		CityDistrictWardResponse res = CityDistrictWardResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setCityList(utilityService.getAllCityDistrictWard());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API Lấy danh sách cấu hình theo key truyền vào (phục vụ header/footer/slide)")
	@RequestMapping(value = "/config", method = RequestMethod.POST)
	public ResponseEntity<GetConfigResponse> getConfig(@RequestBody GetConfigRequest rq) throws Exception {
		GetConfigResponse res = GetConfigResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		List<String> keyList = Arrays.asList(rq.getKeys().split(","));
		Map<String, String> systemParameters = new HashMap<>();
		List<Map<String, String>> returnConfigList = new ArrayList<>();
		for (String key : keyList) {
			if (ValidtionUtils.checkEmptyOrNull(key)) {
				continue;
			}
			systemParameters.put(key, utilityService.getValue(key));
			returnConfigList.add(systemParameters);
		}
		res.setSystemParameters(returnConfigList);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API Lấy phí ship theo tình thành và tổng tiền")
	@RequestMapping(value = "/shippingFee", method = RequestMethod.POST)
	public ResponseEntity<GetShippingFeeResponse> getShippingFee(@RequestBody ShippingFeeRequest rq) throws Exception {
		GetShippingFeeResponse res = GetShippingFeeResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (ValidtionUtils.checkEmptyOrNull(rq.getCityCode(), rq.getDistrictCode()) || rq.getTotalAmount() <= 0) {
			res.setCode("01");
			res.setDesc("Invalid data");
			return ResponseEntity.ok(res);
		}
		ShippingFee shippingFee = utilityService.getShippingFee(rq.getCityCode(), rq.getDistrictCode(), rq.getTotalAmount());
		res.setShippingFee(shippingFee);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
