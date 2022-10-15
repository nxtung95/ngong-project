package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.entity.SystemParameter;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.request.GetConfigRequest;
import vn.ngong.response.GetConfigResponse;
import vn.ngong.service.ConfigService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class ConfigController {
	@Autowired
	private ConfigService configService;

	@Operation(summary = "API Lấy danh sách cấu hình theo key truyền vào (phục vụ header/footer/slide)",
			description = "Trường code: \n 00: Thành công")
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@RequestMapping(value = "/configs", method = RequestMethod.POST)
	public ResponseEntity<GetConfigResponse> getConfig(@RequestBody GetConfigRequest rq) throws Exception {
		GetConfigResponse res = GetConfigResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		List<String> keyList = Arrays.asList(rq.getKeys().split(","));
		Map<String, List<SystemParameter>> systemParameters = new HashMap<>();
		for (String key : keyList) {
			if (ValidtionUtils.checkEmptyOrNull(key)) {
				continue;
			}
			List<SystemParameter> parameters = configService.getValue(key);
			systemParameters.put(key, parameters);
		}
		res.setSystemParameters(systemParameters);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
