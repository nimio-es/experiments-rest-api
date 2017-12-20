package io.theam.controller;

import io.theam.model.Customer;
import io.theam.repository.CustomerRepository;
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
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerRepository customerRepo;

    private List<Customer> customers;


    @Before
    public void setUp() {

        customers = new ArrayList<>();

        final Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("Saulo");
        customer1.setFamilyName("Alvarado");
        customer1.setNdi("00000000Y");

        final Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Nieves");
        customer2.setFamilyName("Sánchez");
        customer2.setNdi("11111111X");

        customers.add(customer1);
        customers.add(customer2);
    }


    @Test
    public void customerNotFoundTest() throws Exception {
        mvc.perform(get("/customers/2").accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    public void getCustomerTest() throws Exception {

        given(customerRepo.findOne(1L)).willReturn(customers.get(0));
        mvc.perform(get("/customers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Saulo")))
                .andExpect(jsonPath("$.familyName", is("Alvarado")));
    }

    @Test
    public void getCustomersTest() throws Exception {

        given(customerRepo.findAll()).willReturn(customers);
        mvc.perform(get("/customers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Saulo")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].familyName", is("Sánchez")));
    }

}
