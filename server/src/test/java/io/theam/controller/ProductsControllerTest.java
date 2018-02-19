package io.theam.controller;

import io.theam.model.Product;
import io.theam.repository.ProductsRepository;
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
@WebMvcTest(value = ProductsController.class, secure = false)
public class ProductsControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductsRepository productsRepository;

	private Product product;
	
	@Before
	public void prepare() {
		product = new Product();
		product.setId(1l);
		product.setRef("JUGUETO");
		product.setName("PLAYMOBIL");
		product.setCommonPrice(10.01);
	}

	@Test
	public void getProductTest() throws Exception {
		given(productsRepository.findOne(1l)).willReturn(product);
		mvc.perform(get("/products/1").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.product.ref", is("JUGUETO")))
				.andExpect(jsonPath("$.product.name", is("PLAYMOBIL")))
				.andExpect(jsonPath("$.product.commonPrice", is(10.01)));
	}
	
	@Test
	public void productNotFoundTest() throws Exception {
		mvc.perform(get("/products/2").accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotFound());
	}
	
	@Test
	public void getProductsTest() throws Exception {
		List<Product> products = new ArrayList<>();
		products.add(product);
		
		given(productsRepository.findAll()).willReturn(products);
		mvc.perform(get("/products").accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].product.ref", is("JUGUETO")))
				.andExpect(jsonPath("$[0].product.name", is("PLAYMOBIL")))
				.andExpect(jsonPath("$[0].product.commonPrice", is(10.01)));
	}

}
