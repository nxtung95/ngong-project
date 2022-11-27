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
import vn.ngong.entity.*;
import vn.ngong.helper.FormatUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.repository.UserSoGaoRepository;
import vn.ngong.request.CalculateAmountRequest;
import vn.ngong.request.PaymentRequest;
import vn.ngong.response.CalculateAmountResponse;
import vn.ngong.response.PaymentMethodListResponse;
import vn.ngong.response.PaymentResponse;
import vn.ngong.response.ShipPriceResponse;
import vn.ngong.service.PaymentService;
import vn.ngong.service.ProductService;
import vn.ngong.service.UserService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	@Autowired
	private ProductService productService;

	@Operation(summary = "Thanh toán",
			description = "API tính tổng tiền sản phẩm trong giỏ hàng")
	@RequestMapping(value = "/amount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CalculateAmountResponse> calculateAmount(@RequestBody CalculateAmountRequest rq) {
		CalculateAmountResponse res = CalculateAmountResponse.builder()
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
		if ((rq.getProductList() == null || rq.getProductList().isEmpty()) && (rq.getSoGaoList() == null || rq.getSoGaoList().isEmpty())) {
			res.setCode("01");
			res.setDesc("Sản phẩm để tính phí không được trống");
			return ResponseEntity.ok(res);
		}
		try {
			List<AmountProductDto> paymentProductList = new ArrayList<>();
			List<AmountProductDto> paymentGaoList = new ArrayList<>();
			AmountProductDto remindGao = null;
			List<TransProductDto> productList = rq.getProductList();
			List<TransSoGaoDto> soGaoList = rq.getSoGaoList();

			if (productList != null && !productList.isEmpty()) {
				paymentProductList = convertProductToPayment(productList);
			}
			if (soGaoList != null && !soGaoList.isEmpty()) {
				paymentGaoList = convertSoGaoToPayment(soGaoList);
			}
			if (rq.getRemindGao() != null) {
				remindGao = convertRemindGaoToPayment(rq.getRemindGao());
			}

			long[] calculateAmount = calcAmount(productList, paymentProductList, paymentGaoList, soGaoList, rq.getCustomer().getCusPhone(), remindGao);

			if (paymentProductList != null && !paymentProductList.isEmpty()) {
				res.setProductList(paymentProductList);
			}
			if (paymentGaoList != null && !paymentGaoList.isEmpty()) {
				res.setSoGaoList(paymentGaoList);
			}
			if (remindGao != null) {
				res.setRemindGao(remindGao);
			}
			res.setOriginAmount(calculateAmount[0]);
			res.setAmountDiscount(calculateAmount[1]);
			res.setTotalAmount(calculateAmount[1]);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			res.setCode("01");
			res.setDesc("Có lỗi xảy ra... xin vui lòng thử lại");
		}
		return ResponseEntity.ok(res);
	}

	private AmountProductDto convertRemindGaoToPayment(RemainGaoProductDto remainGao) {
		Product product = productService.findById(remainGao.getProductId());
		return AmountProductDto.builder()
				.productId(remainGao.getProductId())
				.productCode(remainGao.getProductCode())
				.productName(product.getName())
				.price((int) remainGao.getAmountFix())
				.priceDiscount((int) remainGao.getAmountFix())
				.quantity(1)
				.size((int) remainGao.getQuantity())
				.gaoFlag(1)
				.soGaoFlag(0)
				.build();
	}

	private long[] calcAmount(List<TransProductDto> productList, List<AmountProductDto> paymentProductList,
							  List<AmountProductDto> paymentGaoList, List<TransSoGaoDto> soGaoList, String phone, AmountProductDto remainGao) {
		long originAmount = 0;
		long discountAmount = 0;
		if (productList == null || !paymentService.isHaveRiceProduct(paymentProductList)) {
			// Đơn hàng không có sản phẩm gạo
			if (paymentProductList != null) {
				originAmount += paymentProductList.stream().mapToLong(p -> p.getPrice() * p.getQuantity()).sum();
				discountAmount += paymentProductList.stream().mapToLong(p -> p.getPriceDiscount() * p.getQuantity()).sum();
			}
			if (paymentGaoList != null) {
				originAmount += paymentGaoList.stream().mapToLong(p -> p.getPrice() * p.getQuantity()).sum();
				discountAmount += paymentGaoList.stream().mapToLong(p -> p.getPriceDiscount() * p.getQuantity()).sum();
			}
		} else {
			// Kiểm tra có trong hệ thống không?
			Optional<User> optionalUser = userService.findByPhone(phone);
			if (!optionalUser.isPresent()) { // User chưa tồn tại trong hệ thống
				if (paymentProductList != null) {
					originAmount += paymentProductList.stream().mapToLong(p -> p.getPrice() * p.getQuantity()).sum();
					discountAmount += paymentProductList.stream().mapToLong(p -> p.getPriceDiscount() * p.getQuantity()).sum();
				}
				if (paymentGaoList != null) {
					originAmount += paymentGaoList.stream().mapToLong(p -> p.getPrice() * p.getQuantity()).sum();
					discountAmount += paymentGaoList.stream().mapToLong(p -> p.getPriceDiscount() * p.getQuantity()).sum();
				}
			} else {
				User user = optionalUser.get();
				// Check số gạo trong sổ của user
				List<UserSoGao> userSoGaoList = userSoGaoRepository.findAllByUserIdAndStatusAndExpireDateAfterOrderByExpireDateAsc(
						user.getId(), 1, new Timestamp(System.currentTimeMillis()));
				if (!userSoGaoList.isEmpty()) { // Có sổ gạo
					// Cập nhật
					for (AmountProductDto pv : paymentProductList) {
						if (pv.getGaoFlag() == 1) {
							pv.setPrice(0);
							pv.setPriceDiscount(0);
						}
					}
				}
				originAmount += paymentProductList.stream().mapToLong(p -> p.getPrice() * p.getQuantity()).sum();
				discountAmount += paymentProductList.stream().mapToLong(p -> p.getPriceDiscount() * p.getQuantity()).sum();

				if (soGaoList != null && !soGaoList.isEmpty()) {
					originAmount += paymentGaoList.stream().mapToLong(p -> p.getPrice() * p.getQuantity()).sum();
					discountAmount += paymentGaoList.stream().mapToLong(p -> p.getPriceDiscount() * p.getQuantity()).sum();
				}
			}
		}
		if (remainGao != null) {
			originAmount += remainGao.getPrice() * remainGao.getSize();
			discountAmount += remainGao.getPriceDiscount() * remainGao.getSize();
		}
		return new long[] {originAmount, discountAmount};
	}

	private List<AmountProductDto> convertSoGaoToPayment(List<TransSoGaoDto> soGaoList) {
		List<AmountProductDto> paymentGaoList = new ArrayList<>();
		List<String> productCodeList = soGaoList.stream().map(p -> p.getProductCode()).collect(Collectors.toList());
		List<ProductVariant> productVariantGaoList = productService.findAllProductByCode(productCodeList);
		for (ProductVariant pv : productVariantGaoList) {
			Product parent = productService.findById(pv.getProductId());
			if (parent != null) {
				if (parent.getSoGaoFlag() != 1) {
					continue;
				}
				TransSoGaoDto transSoGaoDto = soGaoList.stream().filter(f -> f.getProductCode().equals(pv.getCode())).findFirst().orElse(null);
				Integer quantity = transSoGaoDto != null ? transSoGaoDto.getQuantity() : null;
				paymentGaoList.add(AmountProductDto.builder()
						.productId(pv.getProductId())
						.productCode(pv.getCode())
						.productName(parent.getName())
						.quantity(quantity)
						.price(pv.getPrice())
						.priceDiscount(pv.getSalePrice())
						.size((int) pv.getWeight())
						.gaoFlag(parent.getGaoFlag())
						.soGaoFlag(parent.getSoGaoFlag())
						.build());
			}
		}
		return paymentGaoList;
	}

	private List<AmountProductDto> convertProductToPayment(List<TransProductDto> productList) {
		List<AmountProductDto> paymentProductList = new ArrayList<>();
		List<String> productCodeList = productList.stream().map(p -> p.getProductCode()).collect(Collectors.toList());
		List<ProductVariant> productVariantList = productService.findAllProductByCode(productCodeList);
		for (ProductVariant pv : productVariantList) {
			Product parent = productService.findById(pv.getProductId());
			if (parent != null) {
				if (parent.getSoGaoFlag() == 1) {
					continue;
				}
				TransProductDto transProductDto = productList.stream().filter(f -> f.getProductCode().equals(pv.getCode())).findFirst().orElse(null);
				Integer quantity = transProductDto != null ? transProductDto.getQuantity() : null;
				paymentProductList.add(AmountProductDto.builder()
						.productId(pv.getProductId())
						.productCode(pv.getCode())
						.productName(parent.getName())
						.price(pv.getPrice())
						.priceDiscount(pv.getSalePrice())
						.quantity(quantity)
						.size(parent.getGaoFlag() == 1 ? (int) (pv.getWeight()) : 0)
						.gaoFlag(parent.getGaoFlag())
						.soGaoFlag(0)
						.build());

			}
		}
		return paymentProductList;
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

		if (rq.getOriginAmount() <= 0 || rq.getTotalAmount() <= 0 || rq.getAmountDiscount() <= 0) {
			res.setCode("01");
			res.setDesc("Vui lòng tính toán tổng tiền");
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

			List<AmountProductDto> paymentProductList = new ArrayList<>();
			List<AmountProductDto> paymentGaoList = new ArrayList<>();
			AmountProductDto remindGao = null;
			List<TransProductDto> productList = rq.getProductList();
			List<TransSoGaoDto> soGaoList = rq.getSoGaoList();

			if (productList != null && !productList.isEmpty()) {
				paymentProductList = convertProductToPayment(productList);
			}
			if (soGaoList != null && !soGaoList.isEmpty()) {
				paymentGaoList = convertSoGaoToPayment(soGaoList);
			}
			if (rq.getRemindGao() != null) {
				remindGao = convertRemindGaoToPayment(rq.getRemindGao());
			}
			long[] calculateAmount = calcAmount(productList, paymentProductList, paymentGaoList, soGaoList, rq.getCustomer().getCusPhone(), remindGao);

			int shippingFee = rq.getShippingFee();
			if (rq.getOriginAmount() != calculateAmount[0]) {
				res.setCode("04");
				res.setDesc("Tổng tiền gốc đang không khớp, vui lòng thử lại");
				return new ResponseEntity<>(res, HttpStatus.OK);
			}

			if (rq.getAmountDiscount() != calculateAmount[1]) {
				res.setCode("04");
				res.setDesc("Tổng tiền sau khuyến mại đang không khớp, vui lòng thử lại");
				return new ResponseEntity<>(res, HttpStatus.OK);
			}

			if (rq.getTotalAmount() != calculateAmount[1] + shippingFee) {
				res.setCode("04");
				res.setDesc("Tổng tiền đang không khớp, vui lòng thử lại");
				return new ResponseEntity<>(res, HttpStatus.OK);
			}

			// Kiểm tra có trong hệ thống không?
			Optional<User> optionalUser = userService.findByPhone(rq.getCustomer().getCusPhone());

			Transaction transaction = null;
			if (paymentProductList == null || !paymentService.isHaveRiceProduct(paymentProductList)) {
				// Đơn hàng không có sản phẩm gạo
				User user = optionalUser.isPresent() ? optionalUser.get() : userService.makeUserForPayment(rq);
				transaction = paymentService.paymentWithNoRiceProduct(rq, user, paymentProductList, paymentGaoList);
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
					transaction = paymentService.paymentWithNoRiceProduct(rq, user, paymentProductList, paymentGaoList);
				} else {
					// User có sổ gạo
					User user = optionalUser.get();
					// Check số gạo trong sổ có đủ để thanh toán không
					long currentSizeSoGaoKg = userSoGaoList.stream().mapToInt(u -> u.getSize()).sum();
					long paymentSizeSoGaoKg = paymentProductList.stream()
							.filter(p -> p.getGaoFlag() == 1)
							.mapToInt(p -> p.getSize() * p.getQuantity())
							.sum();
					if (remindGao != null) {
						paymentSizeSoGaoKg = paymentSizeSoGaoKg - remindGao.getSize();
					}
					log.info("Current size so gao (kg): " + currentSizeSoGaoKg);
					log.info("Size payment so gao (kg): " + paymentSizeSoGaoKg);
					if (currentSizeSoGaoKg >= paymentSizeSoGaoKg) {
						// Số gạo trong sổ đủ để thanh toán
						try {
							transaction = paymentService.paymentWithRiceProduct(rq, user, paymentProductList, paymentGaoList, remindGao);
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					} else {
						long currentSize = currentSizeSoGaoKg;
						AmountProductDto remindProduct = null;
						for (AmountProductDto p : paymentProductList) {
							int paymentSize = p.getSize() * p.getQuantity();
							if (currentSize - paymentSize >= 0) {
								currentSize = currentSize - paymentSize;
							} else {
								remindProduct = p;
							}
						}
						long amountFixRemainGao = shareConfig.getAmountFixRemainGao();
						long remainSizeGao = paymentSizeSoGaoKg - currentSizeSoGaoKg;
						long amountRemainGao = remainSizeGao * amountFixRemainGao;
						RemainGaoProductDto remainGaoProductDto = RemainGaoProductDto.builder()
								.productId(remindProduct.getProductId())
								.productCode(remindProduct.getProductCode())
								.amountFix(amountFixRemainGao)
								.totalAmount(amountRemainGao)
								.quantity(remainSizeGao)
								.build();
						// Số gạo trong sổ không đủ để thanh toán
						res.setCode("05");
						res.setDesc("Số gạo trong sổ không đủ để thanh toán sản phẩm gạo");
						res.setRemindGao(remainGaoProductDto);
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

//	@Operation(summary = "API thanh toán lại",
//			description = "API thanh toán khi số gạo của user trong sổ gạo không đủ để trừ sản phẩm gạo và không muốn mua sổ gạo mới")
//	@RequestMapping(value = "/again", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<PaymentResponse> paymentAgain(@RequestBody PaymentRequest rq) throws Exception {
//		PaymentResponse res = PaymentResponse.builder()
//				.code("00")
//				.desc("Success")
//				.build();
//		if (rq.getCustomer() == null ||
//				ValidtionUtils.checkEmptyOrNull(
//						rq.getCustomer().getCusPhone(),
//						rq.getCustomer().getCusEmail(),
//						rq.getCustomer().getCusCity(),
//						rq.getCustomer().getCusDistrict(),
//						rq.getCustomer().getCusWard())) {
//			res.setCode("01");
//			res.setDesc("Vui lòng nhập đủ thông tin khách hàng");
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//		if (!ValidtionUtils.validPhoneNumber(rq.getCustomer().getCusPhone())) {
//			res.setCode("01");
//			res.setDesc("Vui lòng nhập số điện thoại đúng định dạng");
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//
//		if (ValidtionUtils.checkEmptyOrNull(rq.getPaymentMethodId())) {
//			res.setCode("01");
//			res.setDesc("Vui lòng chọn phương thức thanh toán");
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//
//		if (rq.getProductList() == null) {
//			res.setCode("01");
//			res.setDesc("Vui lòng chọn sản phẩm để thanh toán");
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//		List<TransProductDto> productRequestList = rq.getProductList();
//		if (productRequestList.stream().anyMatch(p ->
//				ValidtionUtils.checkEmptyOrNull(p.getProductCode()) || p.getQuantity() <= 0 || p.getProductId() <= 0)) {
//			res.setCode("01");
//			res.setDesc("Thông tin sản phẩm thanh toán không được trống");
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//
//		if (rq.getRemainGaoProductList() == null || rq.getRemainGaoProductList().isEmpty()) {
//			res.setCode("01");
//			res.setDesc("Thông tin sản phẩm gạo được quy đổi sang tiền để thanh toán lại không được trống");
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//
//		if (ValidtionUtils.checkEmptyOrNull(rq.getOriginAmount(), rq.getTotalAmount())) {
//			res.setCode("01");
//			res.setDesc("Vui lòng tính toán tổng tiền ban đầu và tổng tiền phải trả");
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//		try {
//
//			//Check tồn kho
//			if (rq.getProductList() != null && !rq.getProductList().isEmpty()) {
//				List<TransProductDto> productList = rq.getProductList();
//				List<ResponseTransProductDto> responseTransProducts = paymentService.checkInventory(productList);
////				if (responseTransProducts == null) {
////					res.setCode("02");
////					res.setDesc("Có lỗi kết nối hệ thống tồn kho");
////					return ResponseEntity.ok().body(res);
////				}
//				if (!responseTransProducts.isEmpty()) {
//					res.setCode("03");
//					res.setDesc("Số lượng tồn kho của một trong sản phẩm ở giỏ hàng không đủ, vui lòng kiểm tra lại");
//					res.setStockProductList(responseTransProducts);
//					return ResponseEntity.ok().body(res);
//				}
//			}
//
//			// Kiểm tra có trong hệ thống không?
//			Optional<User> optionalUser = userService.findByPhone(rq.getCustomer().getCusPhone());
//			User user = optionalUser.isPresent() ? optionalUser.get() : userService.makeUserForPayment(rq);
//			Transaction transaction = paymentService.paymentWithRiceProductAgain(rq, user);
//			if (transaction == null) {
//				res.setCode("06");
//				res.setDesc("Giao dịch thất bại");
//				return new ResponseEntity<>(res, HttpStatus.OK);
//			}
//			res.setTransaction(transaction);
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		}
//		return new ResponseEntity<>(res, HttpStatus.OK);
//	}

	@Operation(summary = "API lấy danh sách phương thức thanh toán")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<PaymentMethodListResponse> listPaymentMethod() throws Exception {
		PaymentMethodListResponse res = PaymentMethodListResponse.builder().code("00").desc("Success").build();
		res.setPaymentMethodList(paymentService.findAllPaymentMethod());
		return ResponseEntity.ok(res);
	}

//	@Operation(summary = "API giá ship")
//	@RequestMapping(value = "/ship-price", method = RequestMethod.GET)
//	public ResponseEntity<ShipPriceResponse> getShipPrice(@RequestParam int cityCode, @RequestParam int districtCode, @RequestParam double weight, @RequestParam int totalPrice) throws Exception {
//		ShipPriceResponse res = ShipPriceResponse.builder().code("00").desc("Success").build();
//		res.setShipPrice(paymentService.getShipPrice(cityCode, districtCode, weight, totalPrice));
//		return ResponseEntity.ok(res);
//	}
}
