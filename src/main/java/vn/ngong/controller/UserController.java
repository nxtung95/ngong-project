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
import org.springframework.security.core.userdetails.UserDetails;
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
		if (ValidtionUtils.checkEmptyOrNull(rq.getPassword(), rq.getUsername())) {
			res.setCode("01");
			res.setDesc("Invalid request");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		try {
			UserDetails userDetails = userService.login(rq.getUsername(), rq.getPassword());
			if (userDetails == null) {
				res.setCode("02");
				res.setDesc("User hoặc password sai");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
			final String token = jwtTokenUtil.generateToken(userDetails);
			User user = User.builder()
					.email(userDetails.getUsername())
					.build();
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
		if (ValidtionUtils.checkEmptyOrNull(rq.getName(), rq.getEmail(), rq.getPassword(), rq.getPhone(), rq.getAddress())) {
			res.setCode("01");
			res.setDesc("Invalid request");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		try {
			User user = User.builder()
					.name(rq.getName())
					.phone(rq.getPhone())
					.password(rq.getPassword())
					.email(rq.getEmail())
					.address(rq.getAddress())
					.build();
			boolean isExist = userService.checkExist(user);
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
			description = "\nCập nhật thông tin họ tên và số điện thoại. \nTrường code: \n 00: Thành công, 01: Invalid request, 02: Trùng sđt hoặc email, 03: Cập nhật thất bại")
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
		if (ValidtionUtils.checkEmptyOrNull(rq.getName(), rq.getEmail(), rq.getPhone(), rq.getAddress())) {
			res.setCode("01");
			res.setDesc("Invalid request");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		try {
			User user = User.builder()
					.name(rq.getName())
					.phone(rq.getPhone())
					.email(rq.getEmail())
					.build();
			boolean isExist = userService.checkExist(user);
			if (isExist) {
				res.setCode("02");
				res.setDesc("Số điện thoại hoặc email đã tồn tại");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}

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
