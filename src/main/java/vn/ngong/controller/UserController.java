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
import vn.ngong.entity.User;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.request.LoginRequest;
import vn.ngong.request.RegisterRequest;
import vn.ngong.response.LoginResponse;
import vn.ngong.response.RegisterResponse;
import vn.ngong.service.UserService;

@RestController
@Slf4j
public class UserController {

	@Autowired
	private AuthenticationUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/health", method = RequestMethod.GET)
	public ResponseEntity<?> createAuthenticationToken() throws Exception {
		return ResponseEntity.ok("Service up!!!");
	}

	@Operation(summary = "API đăng nhập user",
			description = "Trường code: \n 00: Thành công, 01: Invalid request, 02: User hoặc password sai")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
		@ApiResponse(responseCode = "400", description = "User hoặc mật khẩu sai", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
		@ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest rq) throws Exception {
		LoginResponse res = LoginResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (ValidtionUtils.checkEmptyOrNull(rq.getUsername())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập số điện thoại");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		if (ValidtionUtils.checkEmptyOrNull(rq.getPassword())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập password");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		try {
			User user = userService.login(rq.getUsername(), rq.getPassword());
			if (user == null) {
				res.setCode("02");
				res.setDesc("Số điện thoại hoặc password sai");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
			user.setPasswordPlainText(null);
			user.setPassword(null);
			final String token = jwtTokenUtil.generateToken(user);
			res.setUser(user);
			res.setJwttoken(token);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Operation(summary = "API đăng ký user",
			description = "Trường code: \n 00: Thành công, 01: Invalid request, 02: Trùng sđt hoặc email, 03: Đăng ký thất bại")
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "400", description = "Thất bại", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisterResponse> regis(@RequestBody RegisterRequest rq) throws Exception {
		RegisterResponse res = RegisterResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (!ValidtionUtils.checkEmptyOrNull(rq.getEmail())) {
			if (!ValidtionUtils.validEmail(rq.getEmail())) {
				res.setCode("01");
				res.setDesc("Email sai định dạng");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
		}
		if (ValidtionUtils.checkEmptyOrNull(rq.getPassword())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập password");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		if (ValidtionUtils.checkEmptyOrNull(rq.getName())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập tên");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		if (ValidtionUtils.checkEmptyOrNull(rq.getPhone())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập số điện thoại");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		} else if (!ValidtionUtils.validPhoneNumber(rq.getPhone())) {
			res.setCode("01");
			res.setDesc("Số điện thoại phải là số có 10 hoặc 11 chữ số. Xin vui lòng nhập lại...");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		try {
			User user = User.builder()
					.name(rq.getName())
					.phone(rq.getPhone())
					.password(rq.getPassword())
					.email(rq.getEmail())
					.address(rq.getAddress())
					.passwordPlainText("")
					.actived(1)
					.build();
			boolean isExist = userService.checkExistByPhoneOrEmail(user);
			if (isExist) {
				res.setCode("02");
				res.setDesc("Số điện thoại hoặc email đã tồn tại");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}

			boolean register = userService.register(user);
			if (!register) {
				res.setCode("03");
				res.setDesc("Đăng ký thất bại");
				return new ResponseEntity<>(res, HttpStatus.BAD_GATEWAY);
			}
			user.setPassword("******");
			res.setUser(user);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Operation(summary = "API cập nhật thông tin user",
			description = "\nCập nhật thông tin họ tên theo số điện thoại. \nTrường code: \n 00: Thành công, 01: Invalid request, 02: Trùng sđt hoặc email, 03: Cập nhật thất bại")
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Thành công", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "400", description = "Thất bại", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@RequestMapping(value = "/user/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisterResponse> update(@RequestBody RegisterRequest rq) throws Exception {
		RegisterResponse res = RegisterResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (ValidtionUtils.checkEmptyOrNull(rq.getPhone())) {
			res.setCode("01");
			res.setDesc("Số điện thoại bắt buộc có");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		} else if (!ValidtionUtils.validPhoneNumber(rq.getPhone())) {
			res.setCode("01");
			res.setDesc("Số điện thoại phải là số có 10 hoặc 11 chữ số");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		try {
			boolean isExist = userService.checkExistByEmail(rq.getEmail());
			if (isExist) {
				res.setCode("02");
				res.setDesc("Email đã tồn tại");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
			User user = User.builder()
					.name(rq.getName())
					.phone(rq.getPhone())
					.email(rq.getEmail())
					.address(rq.getAddress())
					.build();
			boolean update = userService.update(user);
			if (!update) {
				res.setCode("03");
				res.setDesc("Cập nhật thất bại");
				return new ResponseEntity<>(res, HttpStatus.BAD_GATEWAY);
			}
			res.setUser(user);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
