package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ngong.config.ShareConfig;
import vn.ngong.dto.payment.*;
import vn.ngong.entity.Transaction;
import vn.ngong.entity.User;
import vn.ngong.entity.UserSoGao;
import vn.ngong.helper.FormatUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.repository.UserSoGaoRepository;
import vn.ngong.request.CalculateAmountRequest;
import vn.ngong.request.PaymentRequest;
import vn.ngong.response.CalculateAmountResponse;
import vn.ngong.response.PaymentMethodListResponse;
import vn.ngong.response.PaymentResponse;
import vn.ngong.service.PaymentService;
import vn.ngong.service.UserService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/payment")
@RestController
public class PaymentController {
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private UserSoGaoRepository userSoGaoRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ShareConfig shareConfig;

	@Operation(summary = "Thanh toán",
			description = "API tính tổng tiền sản phẩm trong giỏ hàng")
	@RequestMapping(value = "/amount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CalculateAmountResponse> calculateAmount(@RequestBody CalculateAmountRequest rq) {
		CalculateAmountResponse res = CalculateAmountResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		try {
			long originAmount = 0;
			long discountAmount = 0;
			long totalAmount = 0;
			List<TransProductDto> productList = rq.getProductList();
			List<TransSoGaoDto> soGaoList = rq.getSoGaoList();
			if (productList == null || !paymentService.isHaveRiceProduct(productList)) {
				// Đơn hàng không có sản phẩm gạo
				if (productList != null && !productList.isEmpty()) {
					originAmount += productList.stream().mapToLong(p -> p.getPrice()).sum();
					discountAmount += productList.stream().mapToLong(p -> p.getPriceDiscount()).sum();
				}
				if (soGaoList != null && !soGaoList.isEmpty()) {
					originAmount += soGaoList.stream().mapToLong(p -> p.getPrice()).sum();
					discountAmount += soGaoList.stream().mapToLong(p -> p.getPriceDiscount()).sum();
				}
				totalAmount = originAmount - discountAmount;
			} else {
				// Kiểm tra có trong hệ thống không?
				Optional<User> optionalUser = userService.findByPhone(rq.getCustomer().getCusPhone());
				if (!optionalUser.isPresent()) { // User chưa tồn tại trong hệ thống
					if (productList != null && !productList.isEmpty()) {
						originAmount += productList.stream().mapToLong(p -> p.getPrice()).sum();
						discountAmount += productList.stream().mapToLong(p -> p.getPriceDiscount()).sum();
					}
					if (soGaoList != null && !soGaoList.isEmpty()) {
						originAmount += soGaoList.stream().mapToLong(p -> p.getPrice()).sum();
						discountAmount += soGaoList.stream().mapToLong(p -> p.getPriceDiscount()).sum();
					}
					totalAmount = originAmount - discountAmount;
				} else {
					User user = optionalUser.get();
					// Check số gạo trong sổ của user
					List<UserSoGao> userSoGaoList = userSoGaoRepository.findAllByUserIdAndStatusAndExpireDateAfterOrderByExpireDateAsc(
							user.getId(), 1, new Timestamp(System.currentTimeMillis()));
					if (!userSoGaoList.isEmpty()) { // Có sổ gạo
						// Cập nhật
						for (TransProductDto p : productList) {
							if ("1".equals(p.getGaoFlag())) {
								p.setPrice(0);
								p.setPriceDiscount(0);
							}
						}
					}
					originAmount += productList.stream().mapToLong(p -> p.getPrice()).sum();
					discountAmount += productList.stream().mapToLong(p -> p.getPriceDiscount()).sum();

					if (soGaoList != null && !soGaoList.isEmpty()) {
						originAmount += soGaoList.stream().mapToLong(p -> p.getPrice()).sum();
						discountAmount += soGaoList.stream().mapToLong(p -> p.getPriceDiscount()).sum();
					}
					totalAmount = originAmount - discountAmount;
				}
			}
			ShippingFeeRequestDto shippingFeeReq = rq.getShippingFeeInfo();
			int shipPrice = paymentService.getShipPrice(shippingFeeReq.getCityCode(), shippingFeeReq.getDistrictCode(),
					shippingFeeReq.getWeight(), shippingFeeReq.getTotalPrice());

			res.setProductList(productList);
			res.setSoGaoList(soGaoList);
			res.setOriginAmount(FormatUtil.formatCurrency(originAmount));
			res.setAmountDiscount(FormatUtil.formatCurrency(discountAmount));
			res.setTotalAmount(FormatUtil.formatCurrency(totalAmount));
			res.setShippingFee(FormatUtil.formatCurrency(shipPrice));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			res.setCode("01");
			res.setDesc("Có lỗi xảy ra... xin vui lòng thử lại");
		}
		return ResponseEntity.ok(res);
	}

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
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		if (!ValidtionUtils.validPhoneNumber(rq.getCustomer().getCusPhone())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập số điện thoại đúng định dạng");
			return new ResponseEntity<>(res, HttpStatus.OK);
		}

		if (ValidtionUtils.checkEmptyOrNull(rq.getPaymentMethodId())) {
			res.setCode("01");
			res.setDesc("Vui lòng chọn phương thức thanh toán");
			return new ResponseEntity<>(res, HttpStatus.OK);
		}

//		List<TransProductDto> productRequestList = rq.getProductList();
//		if (productRequestList.stream().anyMatch(p ->
//				ValidtionUtils.checkEmptyOrNull(p.getProductCode()) || p.getQuantity() <= 0 || p.getPrice() <= 0 || p.getPriceDiscount() <= 0)) {
//			res.setCode("01");
//			res.setDesc("Thông tin sản phẩm thanh toán không được trống");
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}

		if (ValidtionUtils.checkEmptyOrNull(rq.getOriginAmount(), rq.getTotalAmount())) {
			res.setCode("01");
			res.setDesc("Vui lòng tính toán tổng tiền ban đầu và tổng tiền phải trả");
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		try {

			//Check tồn kho
			if (rq.getProductList() != null && !rq.getProductList().isEmpty()) {
				List<TransProductDto> productList = rq.getProductList();
				List<ResponseTransProductDto> responseTransProducts = paymentService.checkInventory(productList);
//				if (responseTransProducts == null) {
//					res.setCode("02");
//					res.setDesc("Có lỗi kết nối hệ thống tồn kho");
//					return ResponseEntity.ok().body(res);
//				}
				if (responseTransProducts != null && !responseTransProducts.isEmpty()) {
					res.setCode("03");
					res.setDesc("Số lượng tồn kho của một trong sản phẩm ở giỏ hàng không đủ, vui lòng kiểm tra lại");
					res.setStockProductList(responseTransProducts);
					return ResponseEntity.ok().body(res);
				}
			}

			// Kiểm tra có trong hệ thống không?
			Optional<User> optionalUser = userService.findByPhone(rq.getCustomer().getCusPhone());

			Transaction transaction;
			if (rq.getProductList() == null || !paymentService.isHaveRiceProduct(rq.getProductList())) {
				// Đơn hàng không có sản phẩm gạo
				User user = optionalUser.isPresent() ? optionalUser.get() : userService.makeUserForPayment(rq);
				transaction = paymentService.paymentWithNoRiceProduct(rq, user);
			} else {
				Timestamp currentDate = new Timestamp(System.currentTimeMillis());
				List<UserSoGao> userSoGaoList = new ArrayList<>();
				if (optionalUser.isPresent()) {
					userSoGaoList = userSoGaoRepository.findAllByUserIdAndStatusAndExpireDateAfterOrderByExpireDateAsc(
							optionalUser.get().getId(), 1, currentDate);
				}
				if (userSoGaoList.isEmpty()) {
					// User chưa có sổ gạo
					User user = optionalUser.isPresent() ? optionalUser.get() : userService.makeUserForPayment(rq);
					transaction = paymentService.paymentWithNoRiceProduct(rq, user);
				} else {
					// User có sổ gạo
					User user = optionalUser.get();
					// Check số gạo trong sổ có đủ để thanh toán không
					long currentSizeSoGaoKg = userSoGaoList.stream().mapToInt(u -> u.getSize()).sum();
					long paymentSizeSoGaoKg = rq.getProductList().stream()
							.filter(p -> p.getGaoFlag() == 1)
							.mapToInt(p -> p.getSize() * p.getQuantity())
							.sum();
					log.info("Current size so gao (kg): " + currentSizeSoGaoKg);
					log.info("Size payment so gao (kg): " + paymentSizeSoGaoKg);
					if (currentSizeSoGaoKg >= paymentSizeSoGaoKg) {
						// Số gạo trong sổ đủ để thanh toán
						try {
							transaction = paymentService.paymentWithRiceProduct(rq, user);
						} catch (Exception e) {
							// Số gạo trong sổ không đủ để thanh toán
							String amountFixRemainGao = shareConfig.getAmountFixRemainGao();
							long remainSizeGao = Long.parseLong(e.getMessage());
							String amountRemainGao = String.valueOf(remainSizeGao * Long.parseLong(amountFixRemainGao));
							RemainGaoProductDto remainGaoProductDto = RemainGaoProductDto.builder()
									.amountFixRemainGao(amountFixRemainGao)
									.amountRemainGao(amountRemainGao)
									.remainSizeGao(remainSizeGao)
									.build();
							res.setRemainGaoProduct(remainGaoProductDto);
							res.setCode("05");
							res.setDesc("Số gạo trong sổ không đủ để thanh toán sản phẩm gạo");
							return new ResponseEntity<>(res, HttpStatus.OK);
						}
					} else {
						String amountFixRemainGao = shareConfig.getAmountFixRemainGao();
						long remainSizeGao = paymentSizeSoGaoKg - currentSizeSoGaoKg;
						String amountRemainGao = String.valueOf(remainSizeGao * Long.parseLong(amountFixRemainGao));
						RemainGaoProductDto remainGaoProductDto = RemainGaoProductDto.builder()
								.amountFixRemainGao(amountFixRemainGao)
								.amountRemainGao(amountRemainGao)
								.remainSizeGao(remainSizeGao)
								.build();
						// Số gạo trong sổ không đủ để thanh toán
						res.setCode("05");
						res.setDesc("Số gạo trong sổ không đủ để thanh toán sản phẩm gạo");
						res.setRemainGaoProduct(remainGaoProductDto);
						return new ResponseEntity<>(res, HttpStatus.OK);
					}
				}

			}
			if (transaction == null) {
				res.setCode("06");
				res.setDesc("Giao dịch thất bại");
				return new ResponseEntity<>(res, HttpStatus.OK);
			}
			res.setTransaction(transaction);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API thanh toán lại",
			description = "API thanh toán khi số gạo của user trong sổ gạo không đủ để trừ sản phẩm gạo và không muốn mua sổ gạo mới")
	@RequestMapping(value = "/again", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaymentResponse> paymentAgain(@RequestBody PaymentRequest rq) throws Exception {
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
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		if (!ValidtionUtils.validPhoneNumber(rq.getCustomer().getCusPhone())) {
			res.setCode("01");
			res.setDesc("Vui lòng nhập số điện thoại đúng định dạng");
			return new ResponseEntity<>(res, HttpStatus.OK);
		}

		if (ValidtionUtils.checkEmptyOrNull(rq.getPaymentMethodId())) {
			res.setCode("01");
			res.setDesc("Vui lòng chọn phương thức thanh toán");
			return new ResponseEntity<>(res, HttpStatus.OK);
		}

		if (rq.getProductList() == null) {
			res.setCode("01");
			res.setDesc("Vui lòng chọn sản phẩm để thanh toán");
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		List<TransProductDto> productRequestList = rq.getProductList();
		if (productRequestList.stream().anyMatch(p ->
				ValidtionUtils.checkEmptyOrNull(p.getProductCode()) || p.getQuantity() <= 0 || p.getPrice() <= 0 || p.getPriceDiscount() <= 0)) {
			res.setCode("01");
			res.setDesc("Thông tin sản phẩm thanh toán không được trống");
			return new ResponseEntity<>(res, HttpStatus.OK);
		}

		if (rq.getRemainGaoProductList() == null || rq.getRemainGaoProductList().isEmpty()) {
			res.setCode("01");
			res.setDesc("Thông tin sản phẩm gạo được quy đổi sang tiền để thanh toán lại không được trống");
			return new ResponseEntity<>(res, HttpStatus.OK);
		}

		if (ValidtionUtils.checkEmptyOrNull(rq.getOriginAmount(), rq.getTotalAmount())) {
			res.setCode("01");
			res.setDesc("Vui lòng tính toán tổng tiền ban đầu và tổng tiền phải trả");
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		try {

			//Check tồn kho
			if (rq.getProductList() != null && !rq.getProductList().isEmpty()) {
				List<TransProductDto> productList = rq.getProductList();
				List<ResponseTransProductDto> responseTransProducts = paymentService.checkInventory(productList);
				if (responseTransProducts == null) {
					res.setCode("02");
					res.setDesc("Có lỗi kết nối hệ thống tồn kho");
					return ResponseEntity.ok().body(res);
				}
				if (!responseTransProducts.isEmpty()) {
					res.setCode("03");
					res.setDesc("Số lượng tồn kho của một trong sản phẩm ở giỏ hàng không đủ, vui lòng kiểm tra lại");
					res.setStockProductList(responseTransProducts);
					return ResponseEntity.ok().body(res);
				}
			}
			// Kiểm tra có trong hệ thống không?
			Optional<User> optionalUser = userService.findByPhone(rq.getCustomer().getCusPhone());
			User user = optionalUser.isPresent() ? optionalUser.get() : userService.makeUserForPayment(rq);
			Transaction transaction = paymentService.paymentWithRiceProductAgain(rq, user);
			if (transaction == null) {
				res.setCode("06");
				res.setDesc("Giao dịch thất bại");
				return new ResponseEntity<>(res, HttpStatus.OK);
			}
			res.setTransaction(transaction);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "API lấy danh sách phương thức thanh toán")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<PaymentMethodListResponse> listPaymentMethod() throws Exception {
		PaymentMethodListResponse res = PaymentMethodListResponse.builder().code("00").desc("Success").build();
		res.setPaymentMethodList(paymentService.findAllPaymentMethod());
		return ResponseEntity.ok(res);
	}

//	@Operation(summary = "API giá ship")
//	@RequestMapping(value = "/ship-price", method = RequestMethod.GET)
//	public ResponseEntity<ShipPriceResponse> getShipPrice(@RequestParam int cityCode,@RequestParam int districtCode,@RequestParam double weight,@RequestParam int totalPrice) throws Exception {
//		ShipPriceResponse res = ShipPriceResponse.builder().code("00").desc("Success").build();
//		res.setShipPrice(paymentService.getShipPrice(cityCode, districtCode, weight, totalPrice));
//		return ResponseEntity.ok(res);
//	}
}
