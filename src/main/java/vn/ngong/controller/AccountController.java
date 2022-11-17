package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.entity.User;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.repository.TransactionNotifyRepository;
import vn.ngong.response.NotifyTransactionListResponse;
import vn.ngong.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping(value = "/account")
@RestController
public class AccountController {
	@Autowired
	private TransactionNotifyRepository transactionNotifyRepository;
	@Autowired
	private AuthenticationUtil authenticationUtil;
	@Autowired
	private UserService userService;

	@Operation(summary = "API lấy danh sách thông báo của user")
	@RequestMapping(value = "/notify", method = RequestMethod.GET)
	public ResponseEntity<NotifyTransactionListResponse> listNotifyUser(HttpServletRequest httpServletRequest) throws Exception {
		NotifyTransactionListResponse res = NotifyTransactionListResponse.builder().code("00").desc("Success").build();
		Integer userId = null;
		String token = authenticationUtil.extractTokenFromRequest(httpServletRequest);
		if (!ValidtionUtils.checkEmptyOrNull(token)) {
			User user = authenticationUtil.getUserFromToken(token);
			userId = user.getId();
		}
		res.setTransactionNotifyList(transactionNotifyRepository.findAllByUserIdOrderByUpdatedDateDesc(userId));
		return ResponseEntity.ok(res);
	}
}
