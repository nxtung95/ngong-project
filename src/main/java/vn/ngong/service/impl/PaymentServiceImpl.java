package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ngong.cache.LocalCacheConfig;
import vn.ngong.dto.RequestTransProductDto;
import vn.ngong.dto.ResponseTransProductDto;
import vn.ngong.entity.*;
import vn.ngong.enums.TransactionStatusEnum;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.repository.*;
import vn.ngong.request.PaymentRequest;
import vn.ngong.service.PaymentService;
import vn.ngong.service.ProductService;
import vn.ngong.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	@Override
	@Transactional
	public Transaction paymentWithNoSoGao(PaymentRequest rq) {
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

			User user = userService.makeUserForPayment(rq);
			if (user == null) {
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

	@Override
	public boolean isHaveSoGao(List<RequestTransProductDto> products) {
		return products.stream().anyMatch(p -> p.getIsSoGao() == 1);
	}

	@Override
	public List<ResponseTransProductDto> checkInventory(List<RequestTransProductDto> productList) {
		List<ResponseTransProductDto> responseTransProducts = new ArrayList<>();
		for (RequestTransProductDto p : productList) {
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
}
