package com.example.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
	@Autowired
	CustomerRepo repo;

	public Customer add(Customer customer) {
		return repo.add(customer);
	}

	public Customer get(int id) {
		return repo.get(id);
	}

	public List<Customer> getAll() {
		return repo.getAll();
	}

	public void delete(int id) {
		repo.delete(id);
	}

}