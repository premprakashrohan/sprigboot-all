package com.example;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
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
	public ResponseEntity<Object> saveCustomer(@Valid @RequestBody Customer customer) {
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

@ControllerAdvice
@RestController
class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception {
		ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR); 
	}

	@ExceptionHandler(CustomerNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(CustomerNotFoundException ex, WebRequest request)
			throws Exception {
		ExceptionResponse er = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(er, HttpStatus.NOT_EXTENDED);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse er = new ExceptionResponse(new Date(),"VALIDATION Failed!!" , ex.getBindingResult().toString());
		return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
	}
}

class ExceptionResponse {
	private Date timeStamp;
	private String msg;
	private String details;

	public ExceptionResponse(Date timeStamp, String msg, String details) {
		super();
		this.timeStamp = timeStamp;
		this.msg = msg;
		this.details = details;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public String getMsg() {
		return msg;
	}

	public String getDetails() {
		return details;
	}

}