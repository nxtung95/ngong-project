package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.entity.Customer;
import vn.ngong.repository.CustomerRepository;
import vn.ngong.service.CustomerService;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public void add(Customer customer) {
		try {
			customerRepository.saveAndFlush(customer);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
