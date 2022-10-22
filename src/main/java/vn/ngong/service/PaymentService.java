package vn.ngong.service;

import vn.ngong.entity.PaymentMethod;
import vn.ngong.entity.Transaction;
import vn.ngong.request.PaymentRequest;

import java.util.List;

public interface PaymentService {
	Transaction insert(PaymentRequest rq);

	List<PaymentMethod> findAllPaymentMethod();
}
