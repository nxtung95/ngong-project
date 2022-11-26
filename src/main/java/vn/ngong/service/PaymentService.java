package vn.ngong.service;

import vn.ngong.dto.payment.AmountProductDto;
import vn.ngong.dto.payment.TransProductDto;
import vn.ngong.dto.payment.ResponseTransProductDto;
import vn.ngong.entity.PaymentMethod;
import vn.ngong.entity.ProductVariant;
import vn.ngong.entity.Transaction;
import vn.ngong.entity.User;
import vn.ngong.request.PaymentRequest;

import java.util.List;

public interface PaymentService {
	Transaction paymentWithNoRiceProduct(PaymentRequest rq, User user, List<AmountProductDto> paymentProductList, List<AmountProductDto> paymentGaoList);

	List<PaymentMethod> findAllPaymentMethod();

	boolean isHaveRiceProduct(List<AmountProductDto> paymentProductList);

	List<ResponseTransProductDto> checkInventory(List<TransProductDto> productList);

	Transaction paymentWithRiceProduct(PaymentRequest rq, User user, List<AmountProductDto> paymentProductList, List<AmountProductDto> paymentGaoList);

	int getShipPrice(int cityCode, int districtCode, double weight, int totalPrice);

	Transaction paymentWithRiceProductAgain(PaymentRequest rq, User user);
}
