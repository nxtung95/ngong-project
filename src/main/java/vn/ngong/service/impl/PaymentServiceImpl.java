package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ngong.cache.LocalCacheConfig;
import vn.ngong.dto.RequestTransProductDto;
import vn.ngong.entity.*;
import vn.ngong.enums.TransactionStatusEnum;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.repository.*;
import vn.ngong.request.PaymentRequest;
import vn.ngong.service.PaymentService;
import vn.ngong.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;
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

	@Override
	@Transactional
	public Transaction insert(PaymentRequest rq) {
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

			String currentPhoneUser = authenticationUtil.getPhoneLoginName();
			log.info("currentPhoneUser: ", currentPhoneUser);
			User user;
			// User đặt hàng chưa đăng nhập
			if (ValidtionUtils.checkEmptyOrNull(currentPhoneUser) || "anonymousUser".equalsIgnoreCase(currentPhoneUser)) {
				// Kiểm tra có trong hệ thống không?
				Optional<User> optionalUser = userService.findByPhone(rq.getCustomer().getCusPhone());
				if (optionalUser == null) {
					log.info("Lỗi user null");
					return null;
				}

				// Đã có trong hệ thống
				if (optionalUser.isPresent()) {
					user = optionalUser.get();
				} else {
					// Chưa có trong hệ thống
					String passwordDefault = authenticationUtil.makeDefaultPassword();
					log.info("passwordDefault: " + passwordDefault);
					User newUser = User.builder()
							.name(rq.getCustomer().getCusName())
							.phone(rq.getCustomer().getCusPhone())
							.password("")
							.passwordPlainText(passwordDefault)
							.email(rq.getCustomer().getCusEmail())
							.address(rq.getCustomer().getCusWard() + "," + rq.getCustomer().getCusDistrict() + "," + rq.getCustomer().getCusCity())
							.actived(1)
							.build();
					user = userService.add(newUser);
				}
			} else {
				// Kiểm tra có trong hệ thống không?
				Optional<User> optionalUser = userService.findByPhone(rq.getCustomer().getCusPhone());
				if (optionalUser == null) {
					log.info("Lỗi user null");
					return null;
				}
				user = optionalUser.get();
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
			Orders order = Orders.builder()
					.customerReceiverId(addCustomer.getId())
					.originAmount(Integer.parseInt(rq.getOriginAmount()))
					.discountAmount(Integer.parseInt(rq.getAmountDiscount()))
					.totalAmount(Integer.parseInt(rq.getTotalAmount()))
					.status(1)
					.createdBy(user.getName())
					.build();
			Orders addOrder = orderRepository.saveAndFlush(order);
			log.info("--- End add order ---");

			// Add order detail
			log.info("--- start add order_detail ---");
			List<OrderDetail> orderDetails = new ArrayList<>();
			for (RequestTransProductDto p : rq.getProductList()) {
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

			log.info("-------END TRANSACTION -----------");
			return trans;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<PaymentMethod> findAllPaymentMethod() {
		if (localCacheConfig.getPaymentMethodList() == null || localCacheConfig.getPaymentMethodList().isEmpty()) {
			localCacheConfig.loadPaymentMethodList();
		}
		return localCacheConfig.getPaymentMethodList();
	}
}
