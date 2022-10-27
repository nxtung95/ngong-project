package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.dto.RequestTransProductDto;
import vn.ngong.dto.ResponseTransProductDto;
import vn.ngong.entity.Transaction;
import vn.ngong.entity.User;
import vn.ngong.entity.UserSoGao;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.repository.UserSoGaoRepository;
import vn.ngong.request.PaymentRequest;
import vn.ngong.response.PaymentMethodListResponse;
import vn.ngong.response.PaymentResponse;
import vn.ngong.service.PaymentService;
import vn.ngong.service.UserService;

import java.sql.Timestamp;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/payment")
public class PaymentController {
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private UserSoGaoRepository userSoGaoRepository;
	@Autowired
	private UserService userService;

	@Operation(summary = "API thanh toán sản phẩm trong giỏ hàng",
			description = "User đã đăng nhập: Đính kèm token vào header khi gọi API")
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaymentResponse> transaction(@RequestBody PaymentRequest rq) throws Exception {
		PaymentResponse res = PaymentResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (rq.getCustomer() == null ||
				ValidtionUtils.checkEmptyOrNull(
						rq.getCustomer().getCusPhone(),
						rq.getCustomer().getCusEmail(),
						rq.getCustomer().getCusCity(),
						rq.getCustomer().getCusDistrict(),
						rq.getCustomer().getCusWard())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập đủ thông tin khách hàng");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		if (!ValidtionUtils.validPhoneNumber(rq.getCustomer().getCusPhone())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập số điện thoại đúng định dạng");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}

		if (ValidtionUtils.checkEmptyOrNull(rq.getPaymentMethodId())) {
			res.setCode("01");
			res.setDesc("Vui lòng chọn phương thức thanh toán");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}

		if (rq.getProductList() == null) {
			res.setCode("01");
			res.setDesc("Vui lòng chọn sản phẩm để thanh toán");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		List<RequestTransProductDto> productRequestList = rq.getProductList();
		if (productRequestList.stream().anyMatch(p ->
				ValidtionUtils.checkEmptyOrNull(p.getProductCode()) || p.getQuantity() <= 0 || p.getPrice() <= 0 || p.getPriceDiscount() <= 0)) {
			res.setCode("01");
			res.setDesc("Thông tin sản phẩm thanh toán không được trống");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}

		if (ValidtionUtils.checkEmptyOrNull(rq.getOriginAmount(), rq.getTotalAmount())) {
			res.setCode("01");
			res.setDesc("Vui lòng tính toán tổng tiền ban đầu và tổng tiền phải trả");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		try {

			//Check tồn kho
			List<RequestTransProductDto> productList = rq.getProductList();
			List<ResponseTransProductDto> responseTransProducts = paymentService.checkInventory(productList);
			if (responseTransProducts == null) {
				res.setCode("02");
				res.setDesc("Có lỗi kết nối hệ thống tồn kho");
				return ResponseEntity.internalServerError().body(res);
			}
			if (!responseTransProducts.isEmpty()) {
				res.setCode("03");
				res.setDesc("Số lượng tồn kho của một trong sản phẩm ở giỏ hàng không đủ, vui lòng kiểm tra lại");
				res.setProductList(responseTransProducts);
				return ResponseEntity.badRequest().body(res);
			}

			Transaction transaction = null;
			if (paymentService.isHaveSoGao(rq.getProductList())) {
				// Đơn hàng không có sổ gạo
				transaction = paymentService.paymentWithNoSoGao(rq);
			} else {

				//Lấy user đặt hàng (chưa có thì tạo)
				User user = userService.makeUserForPayment(rq);
				if (user == null) {
					res.setCode("04");
					res.setDesc("Có lỗi hệ thống");
					return ResponseEntity.internalServerError().body(res);
				}
				Timestamp currentDate = new Timestamp(System.currentTimeMillis());
				List<UserSoGao> userSoGaoList = userSoGaoRepository.findAllByUserIdAndAndIsActiveAndExpireDateAfter(
						user.getId(), 1, currentDate);
				if (userSoGaoList.isEmpty()) {
					// User có sổ gạo

					// Check số gạo trong sổ có đủ để thanh toán không

				} else {
					// User chưa có sổ gạo
					transaction = paymentService.paymentWithNoSoGao(rq);
				}

			}
			res.setTransaction(transaction);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Operation(summary = "API lấy danh sách phương thức thanh toán")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<PaymentMethodListResponse> listPaymentMethod() throws Exception {
		PaymentMethodListResponse res = PaymentMethodListResponse.builder().code("00").desc("Success").build();
		res.setPaymentMethodList(paymentService.findAllPaymentMethod());
		return ResponseEntity.ok(res);
	}
}
