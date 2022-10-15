package vn.ngong.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.ngong.entity.User;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.request.LoginRequest;
import vn.ngong.request.RegisterRequest;
import vn.ngong.response.LoginResponse;
import vn.ngong.response.RegisterResponse;
import vn.ngong.service.JWTUserDetailsService;
import vn.ngong.service.UserService;

@RestController
@CrossOrigin
@Slf4j
public class AuthenticationController {

	@Autowired
	private AuthenticationUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/health", method = RequestMethod.GET)
	public ResponseEntity<?> createAuthenticationToken() throws Exception {
		return ResponseEntity.ok("Service up!!!");
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest rq) throws Exception {
		LoginResponse res = LoginResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		try {
			UserDetails userDetails = userService.login(rq.getUsername(), rq.getPassword());
			if (userDetails == null) {
				res.setCode("01");
				res.setDesc("User hoặc password sai");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
			final String token = jwtTokenUtil.generateToken(userDetails);
			res.setJwttoken(token);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisterResponse> regis(@RequestBody RegisterRequest rq) throws Exception {
		RegisterResponse res = RegisterResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		try {
			User user = User.builder()
					.name(rq.getName())
					.phone(rq.getPhone())
					.password(rq.getPassword())
					.email(rq.getEmail())
					.build();
			boolean register = userService.register(user);
//			if (!register) {
//				res.setCode("01");
//				res.setDesc("User hoặc password sai");
//				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
//			}
			res.setUser(user);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
