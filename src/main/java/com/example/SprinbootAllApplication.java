package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	@GetMapping(value = "/hwobpv/{name}", produces = "application/json")// myApp/hwob/prem
	public HelloWorldBean helloWorldByBeanByPathVariable(@PathVariable("name") String name) {
		return new HelloWorldBean("Hello " + name + "!!!");
	}

}
@RestController
@RequestMapping(value = "/customer")
class CutomerController {
	@Autowired
	CustomerService customerService;

	@GetMapping(value = "/", produces = "application/json")
	public List<Customer> helloWorldByBean() {
		return customerService.getAll();
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public Customer helloWorldByBeanByPathVariable(@PathVariable("id") int id) {
		return customerService.get(id);
	}
	@PostMapping(value = "/", consumes = "application/json")
	public void save(@RequestBody Customer customer) {
		 customerService.add(customer);
	}
	

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable Integer id) {
		customerService.delete(id);
	}
}