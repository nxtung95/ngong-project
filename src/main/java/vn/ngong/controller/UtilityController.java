package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.entity.RegisterAgentCTV;
import vn.ngong.entity.RegisterProject;
import vn.ngong.entity.ShippingFee;
import vn.ngong.entity.RegisterTrip;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.request.*;
import vn.ngong.response.*;
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

	@Operation(summary = "API đăng ký chuyến đi")
	@RequestMapping(value = "/register-trip", method = RequestMethod.POST)
	public ResponseEntity<RegisterTripResponse> registerTrip(@RequestBody RegisterTripRequest rq) throws Exception {
		RegisterTripResponse res = RegisterTripResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (ValidtionUtils.checkEmptyOrNull(rq.getName())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập họ tên");
			return ResponseEntity.ok(res);
		}
		if (ValidtionUtils.checkEmptyOrNull(rq.getPhone())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập số điện thoại");
			return ResponseEntity.ok(res);
		} else if (!ValidtionUtils.validPhoneNumber(rq.getPhone())) {
			res.setCode("01");
			res.setDesc("Số điện thoại sai định dạng");
			return ResponseEntity.ok(res);
		}
		if (!ValidtionUtils.checkEmptyOrNull(rq.getEmail()) && !ValidtionUtils.validEmail(rq.getEmail())) {
			res.setCode("01");
			res.setDesc("Email sai định dạng");
			return ResponseEntity.ok(res);
		}
		RegisterTrip registerTrip = RegisterTrip.builder()
				.name(rq.getName())
				.phone(rq.getPhone())
				.address(rq.getAddress())
				.description(rq.getDescription())
				.email(rq.getEmail())
				.build();
		RegisterTrip returnRegisterTrip = utilityService.addTrip(registerTrip);
		res.setRegisterTrip(returnRegisterTrip);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API đăng ký cộng tác viên")
	@RequestMapping(value = "/register-agent-ctv", method = RequestMethod.POST)
	public ResponseEntity<RegisterAgentCtvResponse> registerAgentCtv(@RequestBody RegisterAgentCtvRequest rq) throws Exception {
		RegisterAgentCtvResponse res = RegisterAgentCtvResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (ValidtionUtils.checkEmptyOrNull(rq.getName())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập họ tên");
			return ResponseEntity.ok(res);
		}
		if (ValidtionUtils.checkEmptyOrNull(rq.getPhone())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập số điện thoại");
			return ResponseEntity.ok(res);
		} else if (!ValidtionUtils.validPhoneNumber(rq.getPhone())) {
			res.setCode("01");
			res.setDesc("Số điện thoại sai định dạng");
			return ResponseEntity.ok(res);
		}
		if (!ValidtionUtils.checkEmptyOrNull(rq.getEmail()) && !ValidtionUtils.validEmail(rq.getEmail())) {
			res.setCode("01");
			res.setDesc("Email sai định dạng");
			return ResponseEntity.ok(res);
		}
		RegisterAgentCTV registerAgentCTV = RegisterAgentCTV.builder()
				.name(rq.getName())
				.phone(rq.getPhone())
				.description(rq.getDescription())
				.email(rq.getEmail())
				.build();
		RegisterAgentCTV regis = utilityService.registerAgentCTV(registerAgentCTV);
		res.setRegister(regis);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API đăng ký quan tâm dự án")
	@RequestMapping(value = "/register-project", method = RequestMethod.POST)
	public ResponseEntity<RegisterProjectResponse> registerProject(@RequestBody RegisterProjectRequest rq) throws Exception {
		RegisterProjectResponse res = RegisterProjectResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (ValidtionUtils.checkEmptyOrNull(rq.getName())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập họ tên");
			return ResponseEntity.ok(res);
		}
		if (ValidtionUtils.checkEmptyOrNull(rq.getPhone())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập số điện thoại");
			return ResponseEntity.ok(res);
		} else if (!ValidtionUtils.validPhoneNumber(rq.getPhone())) {
			res.setCode("01");
			res.setDesc("Số điện thoại sai định dạng");
			return ResponseEntity.ok(res);
		}
		if (!ValidtionUtils.checkEmptyOrNull(rq.getEmail()) && !ValidtionUtils.validEmail(rq.getEmail())) {
			res.setCode("01");
			res.setDesc("Email sai định dạng");
			return ResponseEntity.ok(res);
		}
		if (rq.getProjectId() <= 0) {
			res.setCode("01");
			res.setDesc("Vui lòng chọn dự án");
			return ResponseEntity.ok(res);
		}
		RegisterProject registerProj = RegisterProject.builder()
				.name(rq.getName())
				.phone(rq.getPhone())
				.email(rq.getEmail())
				.projectId(rq.getProjectId())
				.feedback(rq.getFeedback())
				.build();
		RegisterProject returnRegisterProj = utilityService.registerProject(registerProj);
		res.setRegisterProject(returnRegisterProj);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API lấy về danh sách dự án")
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public ResponseEntity<GetProjectResponse> getAllProject() throws Exception {
		GetProjectResponse res = GetProjectResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		res.setProjectList(utilityService.findAllProject());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}