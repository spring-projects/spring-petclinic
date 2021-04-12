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
import org.springframework.cheapy.model.Code;
import org.springframework.cheapy.model.SpeedOffer;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.SpeedOfferService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(value = SpeedOfferController.class, 
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration = SecurityConfiguration.class)
class SpeedOfferControllerTest {

	private static final int TEST_CLIENT_ID = 1;
	private static final int TEST_SPEEDOFFER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SpeedOfferService speedOfferService;
	
	@MockBean
	private ClientService clientService;

	private SpeedOffer sp1;

	@BeforeEach
	void setup() {
		User user1 = new User();
		Code code1 = new Code();
		code1.setActivo(true);
		code1.setCode("codeTest1");
		user1.setUsername("user1");
		user1.setPassword("user1");
		Client client1 = new Client();
		client1.setId(TEST_CLIENT_ID);
		client1.setName("client1");
		client1.setEmail("client1");
		client1.setAddress("client1");
		client1.setInit(LocalTime.of(01, 00));
		client1.setFinish(LocalTime.of(01, 01));
		client1.setTelephone("123456789");
		client1.setDescription("client1");
		client1.setCode(code1);
		client1.setFood("client1");
		client1.setUsuar(user1);
		BDDMockito.given(this.clientService.getCurrentClient()).willReturn(client1);
		
		SpeedOffer sp1test = new SpeedOffer();
		sp1test.setId(TEST_SPEEDOFFER_ID);
		sp1test.setStart(LocalDateTime.of(2021, 12, 23, 12, 30));
		sp1test.setEnd(LocalDateTime.of(2022, 12, 23, 12, 30));
		sp1test.setGold(5);
		sp1test.setDiscountGold(15);
		sp1test.setSilver(10);
		sp1test.setDiscountSilver(10);
		sp1test.setBronze(15);
		sp1test.setDiscountBronze(5);
		sp1test.setClient(client1);
		this.sp1 = sp1test;
		BDDMockito.given(this.speedOfferService.findSpeedOfferById(TEST_SPEEDOFFER_ID)).willReturn(this.sp1);
		
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/offers/speed/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("speedOffer"))
				.andExpect(view().name("offers/speed/createOrUpdateSpeedOfferForm"));
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/offers/speed/new")
					.with(csrf())
					.param("start", "23/12/2021 12:30")
					.param("end", "23/12/2022 12:30")
					.param("gold", "5")
					.param("discountGold", "15")
					.param("silver", "10")
					.param("discountSilver", "10")
					.param("bronze", "15")
					.param("discountBronze", "5"))
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/offers/speed/new")
					.with(csrf())
					.param("start", "lsqdufhlqhf")
					.param("end", "")
					.param("gold", "gold")
					.param("discountGold", "")
					.param("silver", "")
					.param("discountSilver", "")
					.param("bronze", "")
					.param("discountBronze", ""))
				.andExpect(model().attributeHasErrors("speedOffer"))
				.andExpect(model().attributeHasFieldErrors("speedOffer", "start"))
				.andExpect(model().attributeHasFieldErrors("speedOffer", "end"))
				.andExpect(model().attributeHasFieldErrors("speedOffer", "gold"))
				.andExpect(model().attributeHasFieldErrors("speedOffer", "discountGold"))
				.andExpect(model().attributeHasFieldErrors("speedOffer", "silver"))
				.andExpect(model().attributeHasFieldErrors("speedOffer", "discountSilver"))
				.andExpect(model().attributeHasFieldErrors("speedOffer", "bronze"))
				.andExpect(model().attributeHasFieldErrors("speedOffer", "discountBronze"))
				.andExpect(view().name("offers/speed/createOrUpdateSpeedOfferForm"));
	}
	
	@WithMockUser(value = "user1", authorities = "client")
	@Test
	void testActivateSuccess() throws Exception {
		mockMvc.perform(get("/offers/speed/{speedOfferId}/activate", TEST_SPEEDOFFER_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/offers/speed/"+TEST_SPEEDOFFER_ID));
	}
	
	@WithMockUser(value = "user1", authorities = "client")
	@Test
	void testActivateHasErrors() throws Exception {
		mockMvc.perform(get("/offers/speed/{speedOfferId}/activate", TEST_SPEEDOFFER_ID+1))
				.andExpect(view().name("exception"));
	}
}