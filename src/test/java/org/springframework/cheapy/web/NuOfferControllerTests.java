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
import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.NuOfferService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = NuOfferController.class, 
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration = SecurityConfiguration.class)
class NuOfferControllerTest {

	private static final int TEST_CLIENT_ID = 1;
	private static final int TEST_NUOFFER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private NuOfferService nuOfferService;
	
	@MockBean
	private ClientService clientService;

	private NuOffer nu1;

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
		
		NuOffer nu1test = new NuOffer();
		nu1test.setId(TEST_NUOFFER_ID);
		nu1test.setStart(LocalDateTime.of(2021, 12, 23, 12, 30));
		nu1test.setEnd(LocalDateTime.of(2022, 12, 23, 12, 30));
		nu1test.setGold(5);
		nu1test.setDiscountGold(15);
		nu1test.setSilver(10);
		nu1test.setDiscountSilver(10);
		nu1test.setBronze(15);
		nu1test.setDiscountBronze(5);
		nu1test.setClient(client1);
		this.nu1 = nu1test;
		BDDMockito.given(this.nuOfferService.findNuOfferById(TEST_NUOFFER_ID)).willReturn(this.nu1);
	}
		
	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/offers/nu/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("nuOffer"))
				.andExpect(view().name("offers/nu/createOrUpdateNuOfferForm"));
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/offers/nu/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("start", "23/12/2021 12:30")
				.param("end", "23/12/2022 12:30")
				.param("gold", "15")
				.param("discountGold", "15")
				.param("silver", "10")
				.param("discountSilver", "10")
				.param("bronze", "5")
				.param("discountBronze", "5"))
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/offers/nu/new")
					.with(csrf())
					.param("start", "lsqdufhlqhf")
					.param("end", "")
					.param("gold", "gold")
					.param("discountGold", "")
					.param("silver", "")
					.param("discountSilver", "")
					.param("bronze", "")
					.param("discountBronze", ""))
				.andExpect(model().attributeHasErrors("nuOffer"))
				.andExpect(model().attributeHasFieldErrors("nuOffer", "start"))
				.andExpect(model().attributeHasFieldErrors("nuOffer", "end"))
				.andExpect(model().attributeHasFieldErrors("nuOffer", "gold"))
				.andExpect(model().attributeHasFieldErrors("nuOffer", "discountGold"))
				.andExpect(model().attributeHasFieldErrors("nuOffer", "silver"))
				.andExpect(model().attributeHasFieldErrors("nuOffer", "discountSilver"))
				.andExpect(model().attributeHasFieldErrors("nuOffer", "bronze"))
				.andExpect(model().attributeHasFieldErrors("nuOffer", "discountBronze"))
				.andExpect(view().name("offers/nu/createOrUpdateNuOfferForm"));
	}
	
	@WithMockUser(value = "user1", authorities = "client")
	@Test
	void testActivateSuccess() throws Exception {
		mockMvc.perform(get("/offers/nu/{nuOfferId}/activate", TEST_NUOFFER_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/offers/nu/"+TEST_NUOFFER_ID));
	}
	
	@WithMockUser(value = "user1", authorities = "client")
	@Test
	void testActivateHasErrors() throws Exception {
		mockMvc.perform(get("/offers/nu/{nuOfferId}/activate", TEST_NUOFFER_ID+1))
				.andExpect(view().name("exception"));
	}

	
}