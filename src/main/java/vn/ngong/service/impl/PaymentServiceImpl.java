package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ngong.cache.LocalCacheConfig;
import vn.ngong.config.ShareConfig;
import vn.ngong.dto.payment.*;
import vn.ngong.entity.*;
import vn.ngong.enums.TransactionStatusEnum;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.FormatUtil;
import vn.ngong.kiotviet.response.CreateCustomerResponse;
import vn.ngong.kiotviet.response.CreateOrdersResponse;
import vn.ngong.kiotviet.service.KiotVietService;
import vn.ngong.repository.*;
import vn.ngong.request.PaymentRequest;
import vn.ngong.service.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	@Autowired
	private LocalCacheConfig localCacheConfig;
	@Autowired
	private ProductService productService;
	@Autowired
	private UserSoGaoRepository userSoGaoRepository;
	@Autowired
	private UserSoGaoHistoryRepository userSoGaoHistoryRepository;
	@Autowired
	private TransactionNotifyRepository transactionNotifyRepository;
	@Autowired
	private ShipRepository shipRepository;
	@Autowired
	private ShareConfig shareConfig;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CustomerService customerService;

	@Transactional
	public Transaction paymentWithNoRiceProduct(PaymentRequest rq, User user, List<AmountProductDto> paymentProductList, List<AmountProductDto> paymentGaoList) {
		log.info("--------START TRANSACTION --------");
		try {
			List<PaymentMethod> paymentMethodList = findAllPaymentMethod();
			if (paymentMethodList.isEmpty()) {
				log.info("Chưa cấu hình payment method");
				return null;
			}
			PaymentMethod paymentMethod = paymentMethodList.stream()
					.filter(p -> p.getId().equalsIgnoreCase(rq.getPaymentMethodId())).findFirst().orElse(null);
			if (paymentMethod == null) {
				log.info("Phương thức thanh toán không tồn tại");
				return null;
			}
			int statusTrans = 0;
			if (paymentMethod.getPaymentType() == 1) {
				statusTrans = TransactionStatusEnum.PAYMENT_NOT_APPROVE.label();
			} else if (paymentMethod.getPaymentType() == 2) {
				statusTrans = TransactionStatusEnum.NOT_PAYMENT_NOT_APPOVE.label();
			}
			if (statusTrans == 0) {
				log.info("Chưa chọn phương thức thanh toán hợp lệ");
				return null;
			}


			log.info("--- Start add customer");
			Customer customer = Customer.builder()
					.name(rq.getCustomer().getCusName())
					.email(rq.getCustomer().getCusEmail())
					.phone(rq.getCustomer().getCusPhone())
					.address(rq.getCustomer().getCusWard() + "," + rq.getCustomer().getCusDistrict() + "," + rq.getCustomer().getCusCity())
					.note(rq.getCustomer().getCusNote())
					.createdBy(user.getName())
					.build();

			Customer cusReceive = customerService.findByPhone(customer.getPhone());
			CreateCustomerResponse cusKiotViet;
			if (cusReceive == null) {
				cusKiotViet = customerService.addCusToKiotViet(customer);
				if (cusKiotViet != null) {
					customer.setCode(cusKiotViet.getData().getCode());
				}
			} else {
				cusKiotViet = customerService.getDetailCus(cusReceive.getCode());
				customer.setCode(cusReceive.getCode());
			}
			Customer addCustomer = customerService.add(customer);
			log.info("--- End add customer");

			log.info("--- Start add order ---");
			long totalAddGao = 0;
			if (paymentGaoList != null && !paymentGaoList.isEmpty()) {
				totalAddGao = paymentGaoList.stream()
						.mapToInt(p -> p.getSize() * p.getQuantity())
						.sum();
			}
			Orders order = Orders.builder()
					.customerReceiverId(addCustomer.getId())
					.originAmount((int) rq.getOriginAmount())
					.discountAmount((int) rq.getAmountDiscount())
					.totalAmount((int) rq.getTotalAmount())
					.totalAddGao((int) totalAddGao)
					.totalSubGao(0)
					.status(1)
					.createdBy(user.getName())
					.build();
			Orders addOrder = orderRepository.saveAndFlush(order);
			log.info("--- End add order ---");

			// Add order detail
			log.info("--- start add order_detail ---");
			List<OrderDetail> orderDetails = new ArrayList<>();
			if (paymentProductList != null && !paymentProductList.isEmpty()) {
				for (AmountProductDto p : paymentProductList) {
					String productCode = p.getProductCode();
					int quantity = p.getQuantity();
					int amount = quantity * p.getPrice();
					int amountDiscount = quantity * p.getPriceDiscount();
					int addGao = 0;
					int subGao = 0;
					int isBuySoGao = 0;
					int isBuyGao = 0;
					OrderDetail orderDetail = OrderDetail.builder()
							.orderId(addOrder.getId())
							.productId(p.getProductId())
							.productName(p.getProductName())
							.productCode(productCode)
							.note("Đặt hàng từ ngong.vn")
							.quantity(quantity)
							.amount(amount)
							.amountDiscount(amountDiscount)
							.addGao(addGao)
							.subGao(subGao)
							.isBuySoGao(isBuySoGao)
							.isBuyGao(isBuyGao)
							.createdBy(user.getName())
							.build();
					orderDetails.add(orderDetail);
				}
			}
			if (paymentGaoList != null && !paymentGaoList.isEmpty()) {
				for (AmountProductDto s : paymentGaoList) {
					String productCode = s.getProductCode();
					int quantity = s.getQuantity();
					int amount = quantity * s.getPrice();
					int amountDiscount = quantity * s.getPriceDiscount();
					int addGao = s.getSize();
					OrderDetail orderDetail = OrderDetail.builder()
							.orderId(addOrder.getId())
							.productId(s.getProductId())
							.productName(s.getProductName())
							.productCode(productCode)
							.note("Đặt hàng từ ngong.vn")
							.quantity(quantity)
							.amount(amount)
							.amountDiscount(amountDiscount)
							.addGao(addGao)
							.subGao(0)
							.isBuyGao(0)
							.isBuySoGao(1)
							.build();
					orderDetails.add(orderDetail);
				}
			}
			orderDetailRepository.saveAllAndFlush(orderDetails);
			log.info("--- end add order_detail ---");

			log.info("---- start add order to kiotviet ------");
			CreateOrdersResponse ordersResponse = orderService.addOrderToKiotViet(order, orderDetails, paymentMethod, cusKiotViet);
			if (ordersResponse == null) {
				log.info("Error kiot viet");
			}
			log.info("---- end add order to kiotviet ------");

			log.info("--- start add so gao ---");
			List<UserSoGao> userSoGaoList = null;
			if (paymentGaoList != null && !paymentGaoList.isEmpty()) {
				userSoGaoList = addSoGao(paymentGaoList, user);
			}
			log.info("--- end add so gao ---");

			log.info("--- start add transaction ---");
			Transaction transaction = Transaction.builder()
					.tranxCode(ordersResponse == null ? FormatUtil.makeTranxId() : ordersResponse.getCode())
					.orderId(addOrder.getId())
					.paymentMethodId(rq.getPaymentMethodId())
					.userId(user.getId())
					.totalAmount((int) rq.getTotalAmount())
					.status(statusTrans)
					.createdBy(user.getName())
					.updatedBy(user.getName())
					.build();
			Transaction trans = transactionRepository.saveAndFlush(transaction);
			log.info("--- end add transaction ---");

			log.info("--- start add transaction_notify default ---");
//			Product firstProduct;
//			if (rq.getProductList() != null && !rq.getProductList().isEmpty()) {
//				firstProduct = productService.findById(rq.getProductList().get(0).getProductId());
//			} else {
//				firstProduct = productService.findById(rq.getSoGaoList().get(0).getProductId());
//			}
//			String image = firstProduct == null ? "" : firstProduct.getImage();
			TransactionNotify transactionNotify = TransactionNotify.builder()
					.tranxId(trans.getId())
					.userId(user.getId())
					.tranxCode(trans.getTranxCode())
					.title("Yeah! Đã đặt hàng thành công")
//					.image(image)
					.content("Bạn đã đặt hàng thành công, đơn hàng của bạn: " + trans.getTranxCode() + ". Thông tin chi tiết, liên hệ: 0945348008")
					.createdBy(user.getName())
					.updatedBy(user.getName())
					.build();
			transactionNotifyRepository.saveAndFlush(transactionNotify);
			log.info("--- end add transaction_notify default ---");

			log.info("--- start add so gao history ---");
			if (paymentGaoList != null && !paymentGaoList.isEmpty()) {
				List<UserSoGaoHistory> userSoGaoHistoryList = addSoGaoHistory(userSoGaoList, user, trans);
				userSoGaoHistoryRepository.saveAllAndFlush(userSoGaoHistoryList);
			}
			log.info("--- start end so gao history ---");

			log.info("-------END TRANSACTION -----------");
			return trans;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	private List<UserSoGaoHistory> addSoGaoHistory(List<UserSoGao> userSoGaoList, User user, Transaction transaction) {
		List<UserSoGaoHistory> userSoGaoHistoryList = new ArrayList<>();
		try {
			for (UserSoGao s : userSoGaoList) {
				UserSoGaoHistory userSoGao = UserSoGaoHistory.builder()
						.userSoGaoId(s.getId())
						.transactionId(transaction.getId())
						.usedNumber(0)
						.addedNumber(s.getSize())
						.remainingNumber(s.getSize())
						.note("Mua sản phẩm sổ gạo")
						.createdAt(new Timestamp(System.currentTimeMillis()))
						.createdBy(user.getId())
						.updatedAt(new Timestamp(System.currentTimeMillis()))
						.updatedBy(user.getId())
						.build();
				userSoGaoHistoryList.add(userSoGao);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return userSoGaoHistoryList;
	}

	private List<UserSoGao> addSoGao(List<AmountProductDto> paymentGaoList, User user) {
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		String expireDateSoGao = localCacheConfig.getConfig("EXPIRE_DATE_SO_GAO", "3");
		Timestamp expireDate = new Timestamp(ZonedDateTime.of(LocalDateTime.now().plusYears(Long.parseLong(expireDateSoGao)),
				ZoneId.systemDefault()).toInstant().toEpochMilli());
		List<UserSoGao> userSoGaoList = new ArrayList<>();
		try {
			for (AmountProductDto s : paymentGaoList) {
				Optional<UserSoGao> soGaoOptional = userSoGaoRepository.findTopByOrderByIdDesc();
				int nextSoGaoId = soGaoOptional.isPresent() ? soGaoOptional.get().getId() + 1 : 0;
				String soGaoCode = FormatUtil.makeSoGaoCode(nextSoGaoId);
				int quantity = s.getQuantity();
				int totalSizeGao = s.getSize() * quantity;
				int productId = s.getProductId();
				UserSoGao userSoGao = UserSoGao.builder()
						.userId(user.getId())
						.productId(productId)
						.soGaoCode(soGaoCode)
						.size(totalSizeGao)
						.expireDate(expireDate)
						.purchaseDate(currentDate)
						.status(1)
						.createdAt(currentDate)
						.createdBy(user.getId())
						.updatedAt(currentDate)
						.updatedBy(user.getId())
						.build();
				userSoGaoList.add(userSoGao);
			}
			userSoGaoRepository.saveAllAndFlush(userSoGaoList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return userSoGaoList;
	}

	@Override
	public List<PaymentMethod> findAllPaymentMethod() {
		if (localCacheConfig.getPaymentMethodList() == null || localCacheConfig.getPaymentMethodList().isEmpty()) {
			localCacheConfig.loadPaymentMethodList();
		}
		return localCacheConfig.getPaymentMethodList();
	}

	@Override
	public boolean isHaveRiceProduct(List<AmountProductDto> paymentProductList) {
		for (AmountProductDto pv : paymentProductList) {
			if (pv.getGaoFlag() == 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<ResponseTransProductDto> checkInventory(List<TransProductDto> productList) {
		List<ResponseTransProductDto> responseTransProducts = new ArrayList<>();
		for (TransProductDto p : productList) {
			Integer numProductStock = productService.getQuantityStockByProductCode(p.getProductCode());
			if (numProductStock == null) {
				return null;
			}
			if (numProductStock == 0 || p.getQuantity() > numProductStock) {
				ResponseTransProductDto responseTransProductDto = ResponseTransProductDto.builder()
						.productCode(p.getProductCode())
						.totalCurrentProduct(p.getQuantity())
						.totalProductStock(numProductStock)
						.totalMinimumProduct(p.getQuantity() - numProductStock)
						.build();
				responseTransProducts.add(responseTransProductDto);
			}
		}
		return responseTransProducts;
	}

	@Override
	@Transactional
	public Transaction paymentWithRiceProduct(PaymentRequest rq, User user, List<AmountProductDto> paymentProductList,
											  List<AmountProductDto> paymentGaoList, AmountProductDto remindGao) {
		log.info("----------------------START TRANSACTION ---------------------------");
		try {
			List<PaymentMethod> paymentMethodList = findAllPaymentMethod();
			if (paymentMethodList.isEmpty()) {
				log.info("Chưa cấu hình payment method");
				return null;
			}
			PaymentMethod paymentMethod = paymentMethodList.stream()
					.filter(p -> p.getId().equalsIgnoreCase(rq.getPaymentMethodId())).findFirst().orElse(null);
			if (paymentMethod == null) {
				log.info("Phương thức thanh toán không tồn tại");
				return null;
			}
			int statusTrans = 0;
			if (paymentMethod.getPaymentType() == 1) {
				statusTrans = TransactionStatusEnum.PAYMENT_NOT_APPROVE.label();
			} else if (paymentMethod.getPaymentType() == 2) {
				statusTrans = TransactionStatusEnum.NOT_PAYMENT_NOT_APPOVE.label();
			}
			if (statusTrans == 0) {
				log.info("Chưa chọn phương thức thanh toán hợp lệ");
				return null;
			}

			log.info("--- Start add customer");
			Customer customer = Customer.builder()
					.name(rq.getCustomer().getCusName())
					.email(rq.getCustomer().getCusEmail())
					.phone(rq.getCustomer().getCusPhone())
					.address(rq.getCustomer().getCusWard() + "," + rq.getCustomer().getCusDistrict() + "," + rq.getCustomer().getCusCity())
					.note(rq.getCustomer().getCusNote())
					.createdBy(user.getName())
					.build();

			Customer cusReceive = customerService.findByPhone(customer.getPhone());
			CreateCustomerResponse cusKiotViet;
			if (cusReceive == null) {
				cusKiotViet = customerService.addCusToKiotViet(customer);
				if (cusKiotViet != null) {
					customer.setCode(cusKiotViet.getData().getCode());
				}
			} else {
				cusKiotViet = customerService.getDetailCus(cusReceive.getCode());
				customer.setCode(cusReceive.getCode());
			}
			Customer addCustomer = customerService.add(customer);
			log.info("--- End add customer");

			log.info("--- Start add order ---");
			List<AmountProductDto> gaoProductList = paymentProductList.stream()
					.filter(g -> g.getGaoFlag() == 1)
					.collect(Collectors.toList());
			long totalSubGao = gaoProductList.stream()
					.mapToInt(p -> p.getSize() * p.getQuantity())
					.sum();
			long totalAddGao = 0;
			if (paymentGaoList != null && !paymentGaoList.isEmpty()) {
				totalAddGao = paymentGaoList.stream()
						.mapToInt(p -> p.getSize() * p.getQuantity())
						.sum();
			}
			Orders order = Orders.builder()
					.customerReceiverId(addCustomer.getId())
					.originAmount((int) rq.getOriginAmount())
					.discountAmount((int) rq.getAmountDiscount())
					.totalAmount((int) rq.getTotalAmount())
					.totalSubGao((int) totalSubGao)
					.totalAddGao((int) totalAddGao)
					.status(1)
					.createdBy(user.getName())
					.build();
			Orders addOrder = orderRepository.saveAndFlush(order);
			log.info("--- End add order ---");

			// Add order detail sản phẩm gạo/sản phẩm khác
			log.info("--- start add order_detail ---");
			List<OrderDetail> orderDetails = new ArrayList<>();
			if (paymentProductList != null && !paymentProductList.isEmpty()) {
				for (AmountProductDto p : paymentProductList) {
					String productCode = p.getProductCode();
					int quantity = p.getQuantity();
					int amount = quantity * p.getPrice();
					int amountDiscount = quantity * p.getPriceDiscount();
					int addGao = 0;
					int subGao = 0;
					int isBuySoGao = 0;
					int isBuyGao = 0;
					if (p.getGaoFlag() == 1) {
						amount = 0;
						amountDiscount = 0;
						subGao = p.getSize() * quantity;
						isBuyGao = 1;
					}
					OrderDetail orderDetail = OrderDetail.builder()
							.orderId(addOrder.getId())
							.productId(p.getProductId())
							.productName(p.getProductName())
							.productCode(productCode)
							.note("Đặt hàng từ ngong.vn")
							.quantity(quantity)
							.amount(amount)
							.amountDiscount(amountDiscount)
							.addGao(addGao)
							.subGao(subGao)
							.isBuySoGao(isBuySoGao)
							.isBuyGao(isBuyGao)
							.createdBy(user.getName())
							.build();
					orderDetails.add(orderDetail);
				}
			}

			if (remindGao != null) {
				int quantity = remindGao.getQuantity();
				int amount = remindGao.getPrice() * remindGao.getSize();
				int amountDiscount = remindGao.getPriceDiscount() * remindGao.getSize();
				int addGao = 0;
				int subGao = 0;
				int isBuySoGao = 0;
				int isBuyGao = 1;
				OrderDetail orderDetail = OrderDetail.builder()
						.orderId(addOrder.getId())
						.productId(remindGao.getProductId())
						.productName(remindGao.getProductName())
						.productCode(remindGao.getProductCode())
						.note("Đặt hàng từ ngong.vn, sản phẩm gạo còn dư: " + remindGao.getSize() + " kg, đơn giá: " + remindGao.getPriceDiscount())
						.quantity(quantity)
						.amount(amount)
						.amountDiscount(amountDiscount)
						.addGao(addGao)
						.subGao(subGao)
						.isBuySoGao(isBuySoGao)
						.isBuyGao(isBuyGao)
						.createdBy(user.getName())
						.build();
				orderDetails.add(orderDetail);
			}

			// Add order detail sản phẩm sổ gạo
			if (paymentGaoList != null && !paymentGaoList.isEmpty()) {
				for (AmountProductDto p : paymentGaoList) {
					String productCode = p.getProductCode();
					int quantity = p.getQuantity();
					int amount = quantity * p.getPrice();
					int amountDiscount = quantity * p.getPriceDiscount();
					int addGao = quantity * p.getSize();
					int subGao = 0;
					int isBuySoGao = 1;
					int isBuyGao = 0;
					OrderDetail orderDetail = OrderDetail.builder()
							.orderId(addOrder.getId())
							.productId(p.getProductId())
							.productName(p.getProductName())
							.productCode(productCode)
							.note("Đặt hàng từ ngong.vn")
							.quantity(quantity)
							.amount(amount)
							.amountDiscount(amountDiscount)
							.addGao(addGao)
							.subGao(subGao)
							.isBuySoGao(isBuySoGao)
							.isBuyGao(isBuyGao)
							.createdBy(user.getName())
							.build();
					orderDetails.add(orderDetail);
				}
			}

			orderDetailRepository.saveAllAndFlush(orderDetails);
			log.info("--- end add order_detail ---");

			log.info("---- start add order to kiotviet ------");
			CreateOrdersResponse ordersResponse = orderService.addOrderToKiotViet(order, orderDetails, paymentMethod, cusKiotViet);
			if (ordersResponse == null) {
				log.info("Error kiot viet");
			}
			log.info("---- end add order to kiotviet ------");

			log.info("--- start add transaction ---");
			Transaction transaction = Transaction.builder()
					.orderId(addOrder.getId())
					.tranxCode(ordersResponse == null ? FormatUtil.makeTranxId() : ordersResponse.getCode())
					.paymentMethodId(rq.getPaymentMethodId())
					.userId(user.getId())
					.totalAmount((int) rq.getTotalAmount())
					.status(statusTrans)
					.createdBy(user.getName())
					.updatedBy(user.getName())
					.build();
			Transaction trans = transactionRepository.saveAndFlush(transaction);
			log.info("--- end add transaction ---");

			log.info("--- start tru gao trong so gao ---");
			if (!gaoProductList.isEmpty()) {
				subSoGao(gaoProductList, user, trans);
			}
			log.info("--- end tru gao trong so ---");

			log.info("--- start add so gao, add so gao history ---");
			List<UserSoGao> userSoGaoList;
			if (paymentGaoList != null && !paymentGaoList.isEmpty()) {
				userSoGaoList = addSoGao(paymentGaoList, user);
				List<UserSoGaoHistory> userSoGaoHistoryList = addSoGaoHistory(userSoGaoList, user, trans);
				userSoGaoHistoryRepository.saveAllAndFlush(userSoGaoHistoryList);
			}
			log.info("--- end add so gao, add so gao history ---");

			log.info("--- start add transaction_notify default ---");
			Product firstProduct;
			if (rq.getProductList() != null && !rq.getProductList().isEmpty()) {
				firstProduct = productService.findById(rq.getProductList().get(0).getProductId());
			} else {
				firstProduct = productService.findById(rq.getSoGaoList().get(0).getProductId());
			}
			String image = firstProduct == null ? "" : firstProduct.getImage();
			TransactionNotify transactionNotify = TransactionNotify.builder()
					.tranxId(trans.getId())
					.userId(user.getId())
					.tranxCode(trans.getTranxCode())
					.title("Yeah! Đã đặt hàng thành công")
					.image(image)
					.content("Bạn đã đặt hàng thành công, đơn hàng của bạn: " + trans.getTranxCode() + ". Thông tin chi tiết, liên hệ: 0945348008")
					.createdBy(user.getName())
					.updatedBy(user.getName())
					.build();
			transactionNotifyRepository.saveAndFlush(transactionNotify);
			log.info("--- end add transaction_notify default ---");

			log.info("------------------------END TRANSACTION -----------------------------");
			return trans;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	private void subSoGao(List<AmountProductDto> productList, User user, Transaction trans) throws Exception {
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		try {
			List<UserSoGao> userSoGaoList = userSoGaoRepository.findAllByUserIdAndStatusAndExpireDateAfterOrderByExpireDateAsc(user.getId(), 1, currentDate);
			long tmpSubTotalGao = productList.stream()
					.collect(Collectors.summingLong(g -> g.getQuantity() * g.getSize()));
			Integer subTotalGao = (int) tmpSubTotalGao;
			UserSoGaoHistory defineUserSoGaoHistory = UserSoGaoHistory.builder()
					.transactionId(trans.getId())
					.note("Trừ thanh toán sổ gạo")
					.createdAt(new Timestamp(System.currentTimeMillis()))
					.createdBy(user.getId())
					.updatedAt(new Timestamp(System.currentTimeMillis()))
					.updatedBy(user.getId())
					.build();
			List<UserSoGaoHistory> userSoGaoHistoryList = new ArrayList<>();
			for (UserSoGao u : userSoGaoList) {
				int currentGao = u.getSize();
				int remainGao = currentGao - subTotalGao;
				u.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
				u.setUpdatedBy(user.getId());
				UserSoGaoHistory userSoGaoHistory = defineUserSoGaoHistory.toBuilder().build();
				userSoGaoHistory.setUserSoGaoId(u.getId());
				if (remainGao > 0) {
					// Số gạo trong sổ gạo hiện tại đủ để trừ hết
					u.setSize(remainGao);
					userSoGaoHistory.setUsedNumber(subTotalGao);
					userSoGaoHistory.setAddedNumber(0);
					userSoGaoHistory.setRemainingNumber(remainGao);
					userSoGaoHistoryList.add(userSoGaoHistory);
					break;
				} else if (remainGao == 0) {
					// Số gạo trong sổ gạo hiện tại đủ để trừ hết
					u.setSize(remainGao);
					u.setStatus(0);
					userSoGaoHistory.setUsedNumber(subTotalGao);
					userSoGaoHistory.setAddedNumber(0);
					userSoGaoHistory.setRemainingNumber(remainGao);
					userSoGaoHistoryList.add(userSoGaoHistory);
					break;
				} else {
					// Số gạo trong sổ gạo hiện tại chưa đủ để trừ hết
					subTotalGao = subTotalGao - currentGao;
					u.setSize(0);
					u.setStatus(0);
					userSoGaoHistory.setUsedNumber(currentGao);
					userSoGaoHistory.setAddedNumber(0);
					userSoGaoHistory.setRemainingNumber(0);
					userSoGaoHistoryList.add(userSoGaoHistory);
				}
			}
			userSoGaoRepository.saveAllAndFlush(userSoGaoList);
			userSoGaoHistoryRepository.saveAllAndFlush(userSoGaoHistoryList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public int getShipPrice(int cityCode, int districtCode, double weight, int totalPrice){
		return shipRepository.getShipPrice(cityCode, districtCode, weight, totalPrice);
	}

}
