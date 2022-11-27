package vn.ngong.service;

import vn.ngong.entity.Customer;
import vn.ngong.kiotviet.response.CreateCustomerResponse;

public interface CustomerService {
	Customer add(Customer customer);

	CreateCustomerResponse addCusToKiotViet(Customer customer);

	Customer findByPhone(String phone);

	CreateCustomerResponse getDetailCus(String code);
}
