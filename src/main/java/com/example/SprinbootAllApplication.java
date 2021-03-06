package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

//import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
//import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
//import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@SpringBootApplication
public class SprinbootAllApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprinbootAllApplication.class, args);

	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver lr = new SessionLocaleResolver();
		lr.setDefaultLocale(Locale.US);
		return lr;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
		rbms.setBasename("messages");
		return rbms;
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

	@Autowired
	ResourceBundleMessageSource messageResource;

	@GetMapping(value = "/hwi", produces = "application/json")
	public String helloWorldInternationalization(
			@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
		return messageResource.getMessage("hello.world", null, locale);
	}

}

@RestController
class VersioningCutomerController {
	@Autowired
	CustomerService customerService;
	com.example.v1.CustomerService customerServiceV1;
//Urls
	@GetMapping(value = "/v1/customers/{id}", produces = "application/json")
	public Customer getCustomerByIdV1(@PathVariable("id") int id) {
		return customerService.get(id);
	}

	@GetMapping(value = "/v2/customers/{id}", produces = "application/json")
	public com.example.v1.Customer getCustomerByIdV2(@PathVariable("id") int id) {
		return customerServiceV1.get(id);
	}
//Parameters
	@GetMapping(value = "/v1/customers/param/{id}", produces = "application/json", params = "version=1")
	public Customer getCustomerByIdV1ByParam(@PathVariable("id") int id) {
		return customerService.get(id);
	}

	@GetMapping(value = "/v2/customers/param/{id}", produces = "application/json", params = "version=2")
	public com.example.v1.Customer getCustomerByIdV2ByParam(@PathVariable("id") int id) {
		return customerServiceV1.get(id);
	}
	
//Headers
	@GetMapping(value = "/v1/customers/header/{id}", produces = "application/json", headers = "X-API-VERSION=1")
	public Customer getCustomerByIdV1ByHeader(@PathVariable("id") int id) {
		return customerService.get(id);
	}

	@GetMapping(value = "/v2/customers/header/{id}", produces = "application/json", params = "X-API-VERSION=2")
	public com.example.v1.Customer getCustomerByIdV2ByHeader(@PathVariable("id") int id) {
		return customerServiceV1.get(id);
	}
//MIME type
	@GetMapping(value = "/v1/customers/produces/{id}", produces = "application/vnd.ims.lis.v1.result+json")
	public Customer getCustomerByIdV1ByProduces(@PathVariable("id") int id) {
		return customerService.get(id);
	}

	@GetMapping(value = "/v2/customers/produces/{id}", produces = "application/vnd.ims.lis.v2.result+json")
	public com.example.v1.Customer getCustomerByIdV2ByProduces(@PathVariable("id") int id) {
		return customerServiceV1.get(id);
	}
}

@RestController("customerControllerV1")
class CutomerController {
	@Autowired
	CustomerService customerService;

	@GetMapping(value = "/customers", produces = { "application/json", "application/xml" })
	public List<Customer> getAllCustomer() {
		return customerService.getAll();
	}
//	/*
//	 * @GetMapping(value = "/customers/{id}", produces = "application/json") public
//	 * EntityModel<Customer> getCustomerById(@PathVariable("id") int id) { Customer
//	 * c = customerService.get(id); if (c == null) { throw new
//	 * CustomerNotFoundException("Customer Not Found!!"); }
//	 * 
//	 * return new EntityModel<Customer>(c,
//	 * linkTo(methodOn(this.getClass()).getAllCustomer()).withRel("all-users"));
//	 * 
//	 * }
//	 */

	@GetMapping(value = "/customers/{id}", produces = "application/json")
	public MappingJacksonValue getCustomerById(@PathVariable("id") int id) {
		Customer c = customerService.get(id);
		if (c == null) {
			throw new CustomerNotFoundException("Customer Not Found!!");
		}
		MappingJacksonValue mjv = new MappingJacksonValue(c);
		mjv.setFilters(new SimpleFilterProvider().addFilter("customerFilter",
				SimpleBeanPropertyFilter.filterOutAllExcept("id")));
		return mjv;
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
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse er = new ExceptionResponse(new Date(), "VALIDATION Failed!!",
				ex.getBindingResult().toString());
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

@Configuration
@EnableSwagger2
class SwaggerConfig {

	public static final Contact DEFAULT_CONTACT = new Contact("Prem", "http://www.gmail.com",
			"premprakashrohan@gmail.com");
	public static final ApiInfo DEFAULT_API_INFO = new ApiInfoBuilder().contact(DEFAULT_CONTACT)
			.title("My Api Document").description("Api Documentation").version("1.0").termsOfServiceUrl("urn:tos")
			.license("Apache 2.0").licenseUrl("http://www.apache.org/licenses/LICENSE-2.0").build();

	public static final Set<String> DEFAULT_CONSUMER_PRODUCER = new HashSet<>(
			Arrays.asList("application/json", "application/xml"));

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).consumes(DEFAULT_CONSUMER_PRODUCER)
				.produces(DEFAULT_CONSUMER_PRODUCER).apiInfo(DEFAULT_API_INFO).select()
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
	}
}
