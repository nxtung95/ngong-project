package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ngong.cache.LocalCacheConfig;
import vn.ngong.dto.ResponseTransProductDto;
import vn.ngong.dto.TransProductDto;
import vn.ngong.dto.TransSoGaoDto;
import vn.ngong.entity.*;
import vn.ngong.enums.TransactionStatusEnum;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.FormatUtil;
import vn.ngong.repository.*;
import vn.ngong.request.PaymentRequest;
import vn.ngong.service.PaymentService;
import vn.ngong.service.ProductService;
import vn.ngong.service.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationUtil authenticationUtil;
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

	@Transactional
	public Transaction paymentWithNoRiceProduct(PaymentRequest rq, User user) {
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

			log.info("--- Start add customer ---");
			Customer customer = Customer.builder()
					.name(rq.getCustomer().getCusName())
					.email(rq.getCustomer().getCusEmail())
					.phone(rq.getCustomer().getCusPhone())
					.address(rq.getCustomer().getCusWard() + "," + rq.getCustomer().getCusDistrict() + "," + rq.getCustomer().getCusCity())
					.note(rq.getCustomer().getCusNote())
					.createdBy(user.getName())
					.build();
			Customer addCustomer = customerRepository.saveAndFlush(customer);
			log.info("--- End add customer ---");

			log.info("--- Start add order ---");
			long totalAddGao = 0;
			if (rq.getSoGaoList() != null && !rq.getSoGaoList().isEmpty()) {
				totalAddGao = rq.getSoGaoList().stream()
						.map(p -> Long.parseLong(p.getAttribute().getAttributeValue()))
						.count();
			}
			Orders order = Orders.builder()
					.customerReceiverId(addCustomer.getId())
					.originAmount(Integer.parseInt(rq.getOriginAmount()))
					.discountAmount(Integer.parseInt(rq.getAmountDiscount()))
					.totalAmount(Integer.parseInt(rq.getTotalAmount()))
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
			for (TransProductDto p : rq.getProductList()) {
				String productCode = p.getProductCode();
				int quantity = p.getQuantity();
				int amount = quantity * p.getPrice();
				int amountDiscount = quantity * p.getPriceDiscount();
				OrderDetail orderDetail = OrderDetail.builder()
						.orderId(addOrder.getId())
						.productCode(productCode)
						.quantity(quantity)
						.amount(amount)
						.amountDiscount(amountDiscount)
						.addGao(0)
						.subGao(0)
						.isBuyGao(0)
						.isBuySoGao(0)
						.build();
				orderDetails.add(orderDetail);
			}
			if (rq.getSoGaoList() != null && !rq.getSoGaoList().isEmpty()) {
				for (TransSoGaoDto s : rq.getSoGaoList()) {
					String productCode = s.getProductCode();
					int quantity = s.getQuantity();
					int amount = quantity * s.getPrice();
					int amountDiscount = quantity * s.getPriceDiscount();
					int addGao = s.getAttribute() != null ? Integer.parseInt(s.getAttribute().getAttributeValue()) : 0;
					OrderDetail orderDetail = OrderDetail.builder()
							.orderId(addOrder.getId())
							.productCode(productCode)
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

			log.info("--- start add so gao ---");
			if (rq.getSoGaoList() != null && !rq.getSoGaoList().isEmpty()) {
				addSoGao(rq, user);
			}
			log.info("--- end add so gao ---");

			log.info("--- start add transaction ---");
			Transaction transaction = Transaction.builder()
					.tranxCode(FormatUtil.makeTranxId())
					.orderId(addOrder.getId())
					.paymentMethodId(rq.getPaymentMethodId())
					.userId(user.getId())
					.totalAmount(Integer.parseInt(rq.getTotalAmount()))
					.status(statusTrans)
					.createdBy(user.getName())
					.updatedBy(user.getName())
					.build();
			Transaction trans = transactionRepository.saveAndFlush(transaction);
			log.info("--- end add transaction ---");

			log.info("--- start add transaction_notify default ---");
			Product firstProduct = productService.findById(rq.getProductList().get(0).getAttribute().getProductId());
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

			log.info("--- start add so gao history ---");
			if (rq.getSoGaoList() != null && !rq.getSoGaoList().isEmpty()) {
				addSoGaoHistory(rq.getSoGaoList(), user, trans);
			}
			log.info("--- start end so gao history ---");

			log.info("-------END TRANSACTION -----------");
			return trans;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	private void addSoGaoHistory(List<TransSoGaoDto> soGaoList, User user, Transaction transaction) {
		try {
			List<UserSoGaoHistory> userSoGaoHistoryList = new ArrayList<>();
			for (TransSoGaoDto s : soGaoList) {
				int size = Integer.parseInt(s.getAttribute().getAttributeValue());
				int quantity = s.getQuantity();
				int totalSizeGao = size * quantity;
				UserSoGaoHistory userSoGao = UserSoGaoHistory.builder()
						.userSoGaoId(user.getId())
						.transactionId(transaction.getId())
						.usedNumber(0)
						.addedNumber(totalSizeGao)
						.remainingNumber(totalSizeGao)
						.note("")
						.createdAt(new Timestamp(System.currentTimeMillis()))
						.createdBy(user.getId())
						.updatedAt(new Timestamp(System.currentTimeMillis()))
						.updatedBy(user.getId())
						.build();
				userSoGaoHistoryList.add(userSoGao);
			}
			userSoGaoHistoryRepository.saveAllAndFlush(userSoGaoHistoryList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void addSoGao(PaymentRequest rq, User user) {
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		String expireDateSoGao = localCacheConfig.getConfig("EXPIRE_DATE_SO_GAO", "3");
		Timestamp expireDate = new Timestamp(ZonedDateTime.of(LocalDateTime.now().minusYears(Long.parseLong(expireDateSoGao)),
				ZoneId.systemDefault()).toInstant().toEpochMilli());
		try {
			List<UserSoGao> userSoGaoList = new ArrayList<>();
			for (TransSoGaoDto s : rq.getSoGaoList()) {
				String soGaoCode = s.getProductCode();
				int size = Integer.parseInt(s.getAttribute().getAttributeValue());
				int quantity = s.getQuantity();
				int totalSizeGao = size * quantity;
				int productId = s.getAttribute().getProductId();
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
	}

	@Override
	public List<PaymentMethod> findAllPaymentMethod() {
		if (localCacheConfig.getPaymentMethodList() == null || localCacheConfig.getPaymentMethodList().isEmpty()) {
			localCacheConfig.loadPaymentMethodList();
		}
		return localCacheConfig.getPaymentMethodList();
	}

	@Override
	public boolean isHaveRiceProduct(List<TransProductDto> products) {
		return products.stream().anyMatch(p -> p.getGaoFlag() == 1);
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
	public Transaction paymentWithRiceProduct(PaymentRequest rq, User user) {
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

			log.info("--- Start add customer ---");
			Customer customer = Customer.builder()
					.name(rq.getCustomer().getCusName())
					.email(rq.getCustomer().getCusEmail())
					.phone(rq.getCustomer().getCusPhone())
					.address(rq.getCustomer().getCusWard() + "," + rq.getCustomer().getCusDistrict() + "," + rq.getCustomer().getCusCity())
					.note(rq.getCustomer().getCusNote())
					.createdBy(user.getName())
					.build();
			Customer addCustomer = customerRepository.saveAndFlush(customer);
			log.info("--- End add customer ---");

			log.info("--- Start add order ---");
			long totalSubGao = rq.getProductList().stream()
					.filter(p -> p.getGaoFlag() == 1)
					.map(p -> Long.parseLong(p.getAttribute().getAttributeValue()))
					.count();
			long totalAddGao = 0;
			if (rq.getSoGaoList() != null && !rq.getSoGaoList().isEmpty()) {
				totalAddGao = rq.getSoGaoList().stream()
						.map(p -> Long.parseLong(p.getAttribute().getAttributeValue()))
						.count();
			}
			Orders order = Orders.builder()
					.customerReceiverId(addCustomer.getId())
					.originAmount(Integer.parseInt(rq.getOriginAmount()))
					.discountAmount(Integer.parseInt(rq.getAmountDiscount()))
					.totalAmount(Integer.parseInt(rq.getTotalAmount()))
					.totalSubGao((int) totalSubGao)
					.totalAddGao((int) totalAddGao)
					.status(1)
					.createdBy(user.getName())
					.build();
			Orders addOrder = orderRepository.saveAndFlush(order);
			log.info("--- End add order ---");

			// Add order detail
			log.info("--- start add order_detail ---");
			List<OrderDetail> orderDetails = new ArrayList<>();
			for (TransProductDto p : rq.getProductList()) {
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
					subGao = Integer.parseInt(p.getAttribute().getAttributeValue());
					isBuyGao = 1;
				}
				OrderDetail orderDetail = OrderDetail.builder()
						.orderId(addOrder.getId())
						.productCode(productCode)
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
			orderDetailRepository.saveAllAndFlush(orderDetails);
			log.info("--- end add order_detail ---");

			log.info("--- start add transaction ---");
			Transaction transaction = Transaction.builder()
					.orderId(addOrder.getId())
					.paymentMethodId(rq.getPaymentMethodId())
					.userId(user.getId())
					.totalAmount(Integer.parseInt(rq.getTotalAmount()))
					.status(statusTrans)
					.createdBy(user.getName())
					.updatedBy(user.getName())
					.build();
			Transaction trans = transactionRepository.saveAndFlush(transaction);
			log.info("--- end add transaction ---");

			log.info("--- start tru gao trong so gao ---");
			List<TransProductDto> gaoProductList = rq.getProductList().stream()
					.filter(g -> g.getGaoFlag() == 1)
					.collect(Collectors.toList());
			if (!gaoProductList.isEmpty()) {
				Integer remindGaoProduct = subSoGao(gaoProductList, user, trans);
				if (remindGaoProduct > 0) {
					log.info("Số gạo trong sổ không đủ để thanh toán");
					throw new Exception("Not enough rice to pay");
				}
			}
			log.info("--- end tru gao trong so ---");

			log.info("--- start add so gao ---");
			if (rq.getSoGaoList() != null && !rq.getSoGaoList().isEmpty()) {
				addSoGao(rq, user);
			}
			log.info("--- end add so gao ---");

			log.info("--- start add so gao history ---");
			if (rq.getSoGaoList() != null && !rq.getSoGaoList().isEmpty()) {
				addSoGaoHistory(rq.getSoGaoList(), user, trans);
			}
			log.info("--- end so gao history ---");


			log.info("------------------------END TRANSACTION -----------------------------");
			return trans;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	private Integer subSoGao(List<TransProductDto> productList, User user, Transaction trans) {
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		try {
			List<UserSoGao> userSoGaoList = userSoGaoRepository.findAllByUserIdAndStatusAndExpireDateAfterOrderByExpireDateAsc(user.getId(), 1, currentDate);
			long tmpSubTotalGao = productList.stream()
					.collect(Collectors.summingLong(g -> g.getQuantity() * Long.parseLong(g.getAttribute().getAttributeValue())));
			Integer subTotalGao = (int) tmpSubTotalGao;
			UserSoGaoHistory userSoGaoHistory = UserSoGaoHistory.builder()
					.userSoGaoId(user.getId())
					.transactionId(trans.getId())
					.note("")
					.createdAt(new Timestamp(System.currentTimeMillis()))
					.createdBy(user.getId())
					.updatedAt(new Timestamp(System.currentTimeMillis()))
					.updatedBy(user.getId())
					.build();
			List<UserSoGaoHistory> userSoGaoHistoryList = new ArrayList<>();
			for (UserSoGao u : userSoGaoList) {
				int currentGao = u.getSize();
				int remainGao = currentGao - subTotalGao;
				if (remainGao > 0) {
					// Số gạo trong sổ gạo hiện tại đủ để trừ hết
					u.setSize(remainGao);
					u.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
					u.setUpdatedBy(u.getId());
					userSoGaoHistory.setId(u.getProductId());
					userSoGaoHistory.setUsedNumber(subTotalGao);
					userSoGaoHistory.setAddedNumber(0);
					userSoGaoHistory.setRemainingNumber(remainGao);
					userSoGaoHistoryList.add(userSoGaoHistory);
					break;
				} else if (remainGao == 0) {
					// Số gạo trong sổ gạo hiện tại đủ để trừ hết
					u.setSize(remainGao);
					u.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
					u.setUpdatedBy(u.getId());
					u.setStatus(0);
					userSoGaoHistory.setId(u.getProductId());
					userSoGaoHistory.setUsedNumber(subTotalGao);
					userSoGaoHistory.setAddedNumber(0);
					userSoGaoHistory.setRemainingNumber(remainGao);
					userSoGaoHistoryList.add(userSoGaoHistory);
					break;
				} else {
					// Số gạo trong sổ gạo hiện tại chưa đủ để trừ hết
					subTotalGao = subTotalGao - currentGao;
				}
			}
			if (subTotalGao > 0) {
				return subTotalGao;
			}
			userSoGaoRepository.saveAllAndFlush(userSoGaoList);
			userSoGaoHistoryRepository.saveAllAndFlush(userSoGaoHistoryList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	public int getShipPrice(int cityCode, int districtCode, int weight, int totalPrice){
		return shipRepository.getShipPrice(cityCode, districtCode, weight, totalPrice);
	}
}
