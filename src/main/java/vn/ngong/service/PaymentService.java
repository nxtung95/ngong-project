package vn.ngong.service;

import vn.ngong.dto.payment.TransProductDto;
import vn.ngong.dto.payment.ResponseTransProductDto;
import vn.ngong.entity.PaymentMethod;
import vn.ngong.entity.Transaction;
import vn.ngong.entity.User;
import vn.ngong.request.PaymentRequest;

import java.util.List;

public interface PaymentService {
	Transaction paymentWithNoRiceProduct(PaymentRequest rq, User user);

	List<PaymentMethod> findAllPaymentMethod();

	boolean isHaveRiceProduct(List<TransProductDto> products);

	List<ResponseTransProductDto> checkInventory(List<TransProductDto> productList);

	Transaction paymentWithRiceProduct(PaymentRequest rq, User user);

	int getShipPrice(int cityCode, int districtCode, int weight, int totalPrice);

	Transaction paymentWithRiceProductAgain(PaymentRequest rq, User user);
}
