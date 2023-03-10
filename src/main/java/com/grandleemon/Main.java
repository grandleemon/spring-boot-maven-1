package com.grandleemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("api/v1")
public class Main {

	private final CustomerRepository customerRepository;

	public Main(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@GetMapping("/")
	public GreetResponse greet() {
		return new GreetResponse("Hello");
	}

	record GreetResponse(String greet) {
	}

	@GetMapping("/customers")
	public List<Customer> getCustomer() {
		return customerRepository.findAll();
	}

	record NewCustomerRequest(
					String name,
					String email,
					Integer age
	) {

	}

	@PostMapping("/customers")
	public void addCustomer(@RequestBody NewCustomerRequest request) {
		Customer customer = new Customer();
		customer.setName(request.name());
		customer.setEmail(request.email());
		customer.setAge(request.age());

		customerRepository.save(customer);
	}

	record CustomerToUpdate(
					String name,
					String email,
					Integer age
	) {

	}

	@PutMapping("/customers/{customerId}")
	public Customer updateCustomer(
					@PathVariable("customerId") Integer id,
					@RequestBody CustomerToUpdate customer
	) {
		Customer customerToUpdate = customerRepository.findById(id).orElseThrow();
		customerToUpdate.setAge(customer.age());
		customerToUpdate.setEmail(customer.email());
		customerToUpdate.setName(customer.name());

		return customerRepository.save(customerToUpdate);
	}

	@DeleteMapping("/customers/{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Integer id) {
		customerRepository.deleteById(id);
	}
}
