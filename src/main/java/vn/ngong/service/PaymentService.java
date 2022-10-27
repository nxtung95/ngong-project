package vn.ngong.service;

import vn.ngong.dto.RequestTransProductDto;
import vn.ngong.dto.ResponseTransProductDto;
import vn.ngong.entity.PaymentMethod;
import vn.ngong.entity.Transaction;
import vn.ngong.request.PaymentRequest;

import java.util.List;

public interface PaymentService {
	Transaction paymentWithNoSoGao(PaymentRequest rq);

	List<PaymentMethod> findAllPaymentMethod();

	boolean isHaveSoGao(List<RequestTransProductDto> products);

	List<ResponseTransProductDto> checkInventory(List<RequestTransProductDto> productList);
}
