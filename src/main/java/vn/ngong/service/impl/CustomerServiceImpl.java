package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.config.ShareConfig;
import vn.ngong.dto.payment.TransCustomerDto;
import vn.ngong.entity.Customer;
import vn.ngong.helper.FormatUtil;
import vn.ngong.kiotviet.request.CreateCustomerRequest;
import vn.ngong.kiotviet.response.CreateCustomerResponse;
import vn.ngong.kiotviet.service.KiotVietService;
import vn.ngong.repository.CustomerRepository;
import vn.ngong.service.CustomerService;

import java.util.Date;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private KiotVietService kiotVietService;
	@Autowired
	private ShareConfig shareConfig;

	@Override
	public Customer add(Customer customer) {
		try {
			return customerRepository.saveAndFlush(customer);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public CreateCustomerResponse addCusToKiotViet(Customer customer) {
		CreateCustomerRequest rq = CreateCustomerRequest.builder()
				.code("CUS" + FormatUtil.makeCustomerCode())
				.name(customer.getName())
				.gender(true)
				.contactNumber(customer.getPhone())
				.address(customer.getAddress())
				.email(customer.getEmail())
				.comments(customer.getNote())
				.branchId(shareConfig.getBranchId())
				.groupIds(new int[] {shareConfig.getCusGroupId()})
				.source("ngong.vn")
				.build();
		CreateCustomerResponse res = kiotVietService.createCustomer(rq);
		return res;
	}

	@Override
	public Customer findByPhone(String phone) {
		try {
			return customerRepository.findFirstByPhone(phone).orElse(null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public CreateCustomerResponse getDetailCus(String code) {
		return CreateCustomerResponse.builder()
				.message("Success")
				.data(kiotVietService.getDetailCus(code))
				.build();
	}
}
