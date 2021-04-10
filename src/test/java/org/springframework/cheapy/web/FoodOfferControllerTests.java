package org.springframework.cheapy.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cheapy.configuration.SecurityConfiguration;
import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.FoodOfferService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(value = FoodOfferController.class, 
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration = SecurityConfiguration.class)
class FoodOfferControllerTest {

	private static final int TEST_CLIENT_ID = 1;
	private static final int TEST_FOODOFFER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FoodOfferService foodOfferService;
	
	@MockBean
	private ClientService clientService;

	private FoodOffer fo1;

	@BeforeEach
	void setup() {
		User user1 = new User();
		user1.setUsername("user1");
		user1.setPassword("user1");
		Client client1 = new Client();;
		client1.setId(TEST_CLIENT_ID);
		client1.setName("client1");
		client1.setEmail("client1");
		client1.setAddress("client1");
		client1.setInit(LocalTime.of(01, 00));
		client1.setFinish(LocalTime.of(01, 01));
		client1.setTelephone("123456789");
		client1.setDescription("client1");
		client1.setCode("client1");
		client1.setFood("client1");
		client1.setUsuar(user1);
		BDDMockito.given(this.clientService.getCurrentClient()).willReturn(client1);
		
		FoodOffer fo1test = new FoodOffer();
		fo1test.setId(TEST_FOODOFFER_ID);
		fo1test.setStart(LocalDateTime.of(2021, 12, 23, 12, 30));
		fo1test.setEnd(LocalDateTime.of(2022, 12, 23, 12, 30));
		fo1test.setFood("fo1test");
		fo1test.setDiscount(1);
		fo1test.setClient(client1);
		this.fo1 = fo1test;
		BDDMockito.given(this.foodOfferService.findFoodOfferById(TEST_FOODOFFER_ID)).willReturn(this.fo1);
		
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/offers/food/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("foodOffer"))
				.andExpect(view().name("offers/food/createOrUpdateFoodOfferForm"));
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/offers/food/new")
					.with(csrf())
					.param("start", "23/12/2021 12:30")
					.param("end", "23/12/2022 12:30")
					.param("food", "food")
					.param("discount", "10"))
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/offers/food/new")
					.with(csrf())
					.param("start", "lsqdufhlqhf")
					.param("end", "")
					.param("food", "")
					.param("discount", ""))
				.andExpect(model().attributeHasErrors("foodOffer"))
				.andExpect(model().attributeHasFieldErrors("foodOffer", "start"))
				.andExpect(model().attributeHasFieldErrors("foodOffer", "end"))
				.andExpect(model().attributeHasFieldErrors("foodOffer", "food"))
				.andExpect(model().attributeHasFieldErrors("foodOffer", "discount"))
				.andExpect(view().name("offers/food/createOrUpdateFoodOfferForm"));
	}
	
	@WithMockUser(value = "user1", authorities = "client")
	@Test
	void testActivateSuccess() throws Exception {
		mockMvc.perform(get("/offers/food/{foodOfferId}/activate", TEST_FOODOFFER_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/offers/food/"+TEST_FOODOFFER_ID));
	}
	
	@WithMockUser(value = "user1", authorities = "client")
	@Test
	void testActivateHasErrors() throws Exception {
		mockMvc.perform(get("/offers/food/{foodOfferId}/activate", TEST_FOODOFFER_ID+1))
				.andExpect(view().name("exception"));
	}
}