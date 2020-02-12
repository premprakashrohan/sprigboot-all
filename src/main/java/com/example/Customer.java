package com.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

public class Customer {
	private int id;
	@Size(min = 2)
	private String name;
	@Past
	private Date dob;

	public Customer() {
		 super();
	}

	public Customer(int id, String name, Date dob) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}
}

@Component
class CustomerRepo {
	Map<Integer, Customer> customerMap = Stream
			.of(new Object[][] { { 1, new Customer(1, "prem", new Date(1980, 02, 14)) },
					{ 2, new Customer(2, "krish", new Date(2014, 04, 12)) }, })
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

@Service
class CustomerService {
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

