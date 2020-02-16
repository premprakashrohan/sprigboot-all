package com.example.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class CustomerRepo {
	Map<Integer, Customer> customerMap = Stream
			.of(new Object[][] { { 1, new Customer(1, new Name("Prem", "Prakash", "Rohan"), new Date(1980, 02, 14)) },
					{ 2, new Customer(2, new Name("Vikram", "Aditya", "Sinha"), new Date(2014, 04, 12)) }, })
			.collect(Collectors.toMap(data -> (Integer) data[0], data -> (Customer) data[1]));

	public Customer add(Customer customer) {
		if (customer.getId() == 0) {
			customer.setId(customerMap.size() + 1);

		}
		customerMap.put(customer.getId(), customer);
		return customer;
	}

	public Customer get(int id) {
		return customerMap.get(id);
	}

	public List<Customer> getAll() {
		List<Customer> key = new ArrayList<>();

		customerMap.entrySet().stream().forEach(entry -> {
			key.add(entry.getValue());
		});

		return key;
	}

	public void delete(int id) {
		customerMap.remove(id);
	}

}
