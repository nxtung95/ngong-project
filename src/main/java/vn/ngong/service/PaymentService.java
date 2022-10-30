package vn.ngong.service;

import vn.ngong.dto.TransProductDto;
import vn.ngong.dto.ResponseTransProductDto;
import vn.ngong.entity.PaymentMethod;
import vn.ngong.entity.Transaction;
import vn.ngong.entity.User;
import vn.ngong.entity.UserSoGao;
import vn.ngong.request.PaymentRequest;

import java.util.List;

public interface PaymentService {
	Transaction paymentWithNoRiceProduct(PaymentRequest rq, User user);

	List<PaymentMethod> findAllPaymentMethod();

	boolean isHaveRiceProduct(List<TransProductDto> products);

	List<ResponseTransProductDto> checkInventory(List<TransProductDto> productList);

	Transaction paymentWithRiceProduct(PaymentRequest rq, User user);
}
