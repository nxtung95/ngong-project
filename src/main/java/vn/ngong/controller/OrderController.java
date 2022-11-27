package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.dto.CartDto;
import vn.ngong.entity.User;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.response.CartListResponse;
import vn.ngong.response.GetListOrderResponse;
import vn.ngong.service.OrderService;
import vn.ngong.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/orders")
public class OrderController {
	@Autowired
	private AuthenticationUtil authenticationUtil;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;

	@Operation(summary = "API get danh sách đơn hàng",
			description = "Trường status trong request: \n 0: tất cả \n 1: Chờ xác nhận \n 2: Đang giao \n 3: Đã hoàn thành \n 4: Đã hủy")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<GetListOrderResponse> list(HttpServletRequest httpServletRequest, @RequestParam(name = "status") int status) throws Exception {
		GetListOrderResponse res = GetListOrderResponse
				.builder()
				.code("00")
				.desc("Success")
				.build();

		int userId = 0;
		String token = authenticationUtil.extractTokenFromRequest(httpServletRequest);
		if (!ValidtionUtils.checkEmptyOrNull(token)) {
			User user = authenticationUtil.getUserFromToken(token);
			userId = user.getId();
		}
		User user = userService.findById(userId);
		if (user == null) {
			res.setCode("01");
			res.setDesc("User không tồn tại trong hệ thống, vui lòng đăng ký");
			return ResponseEntity.ok(res);
		}
		res.setData(orderService.getOrderList(user.getCode(), user.getName(), status));
		return ResponseEntity.ok(res);
	}
}
