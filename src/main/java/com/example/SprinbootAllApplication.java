package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@SpringBootApplication
public class SprinbootAllApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprinbootAllApplication.class, args);

	}

}

@RestController
@RequestMapping(value = "/myApp")
class HelloWorldController {
	@GetMapping(value = "/hw", produces = "application/json")
	public String helloWorld() {
		return "Hello World!!!";
	}

	@GetMapping(value = "/hwo", produces = "application/json")
	public String helloWorld(@RequestParam("name") String name) {
		return "Hello " + name + "!!!";
	}

	@GetMapping(value = "/hwob", produces = "application/json")
	public HelloWorldBean helloWorldByBean(@RequestParam("name") String name) {
		return new HelloWorldBean("Hello " + name + "!!!");
	}

	@GetMapping(value = "/hwobpv/{name}", produces = "application/json") // myApp/hwob/prem
	public HelloWorldBean helloWorldByBeanByPathVariable(@PathVariable("name") String name) {
		return new HelloWorldBean("Hello " + name + "!!!");
	}

}

@RestController
class CutomerController {
	@Autowired
	CustomerService customerService;

	@GetMapping(value = "/customers", produces = "application/json")
	public List<Customer> getAllCustomer() {
		return customerService.getAll();
	}

	@GetMapping(value = "/customers/{id}", produces = "application/json")
	public Customer getCustomerById(@PathVariable("id") int id) {
		Customer c = customerService.get(id);
		if (c == null) {
			throw new CustomerNotFoundException("Customer Not Found!!");
		}
		return c;

	}

	@PostMapping(value = "/customers", consumes = "application/json")
	public ResponseEntity<Object> saveCustomer(@RequestBody Customer customer) {
		Customer savedCustomer = customerService.add(customer);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedCustomer.getId()).toUri()).build();
	}

	@DeleteMapping(value = "/customers/{id}")
	public void deleteCustomer(@PathVariable Integer id) {
		customerService.delete(id);
	}
}
@ResponseStatus(HttpStatus.NOT_FOUND)
class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException(String msg) {
		super(msg);

	}

	public CustomerNotFoundException(String msg, Exception e) {
		super(msg, e);

	}
}