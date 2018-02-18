package io.theam.controller;

import io.theam.model.Customer;
import io.theam.repository.CustomersRepository;
import io.theam.repository.ImagesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CustomerController.class, secure = false)
public class CustomerControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CustomersRepository customerRepo;

	@MockBean
	private ImagesRepository imageRepository;

	private Customer customer;
	
	@Before
	public void prepare() {
		customer = new Customer();
		customer.setId(1l);
		customer.setFirstName("Saulo");
		customer.setLastName("Alvarado Mateos");
		customer.setNdi("0000000X");
	}

	@Test
	public void getPersonTest() throws Exception {
		given(customerRepo.findOne(1l)).willReturn(customer);
		mvc.perform(get("/customers/1").accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.customerId", is(1)))
				.andExpect(jsonPath("$.customer.firstName", is("Saulo")))
				.andExpect(jsonPath("$.customer.lastName", is("Alvarado Mateos")))
				.andExpect(jsonPath("$.customer.ndi", is("0000000X")));
	}
	
	@Test
	public void personNotFoundTest() throws Exception {
		mvc.perform(get("/customers/2").accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotFound());
	}
	
	@Test
	public void getPeopleTest() throws Exception {
		List<Customer> customers = new ArrayList<>();
		customers.add(customer);
		
		given(customerRepo.findAll()).willReturn(customers);
		mvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$[0].customerId", is(1)))
				.andExpect(jsonPath("$[0].customer.firstName", is("Saulo")))
				.andExpect(jsonPath("$[0].customer.lastName", is("Alvarado Mateos")))
				.andExpect(jsonPath("$[0].customer.ndi", is("0000000X")));
	}

}
