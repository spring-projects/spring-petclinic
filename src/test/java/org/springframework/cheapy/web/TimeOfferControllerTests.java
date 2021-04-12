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
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.TimeOfferService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = TimeOfferController.class, 
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration = SecurityConfiguration.class)
class TimeOfferControllerTest {

	private static final int TEST_CLIENT_ID = 1;
	private static final int TEST_TIMEOFFER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TimeOfferService timeOfferService;
	
	@MockBean
	private ClientService clientService;

	private TimeOffer time1;

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
		client1.setInit(LocalTime.of(12, 00));
		client1.setFinish(LocalTime.of(01, 01));
		client1.setTelephone("123456789");
		client1.setDescription("client1");
		client1.setCode(code1);
		client1.setFood("client1");
		client1.setUsuar(user1);
		BDDMockito.given(this.clientService.getCurrentClient()).willReturn(client1);
		
		TimeOffer time1test = new TimeOffer();
		time1test.setId(TEST_TIMEOFFER_ID);
		time1test.setStart(LocalDateTime.of(2021, 12, 23, 12, 30));
		time1test.setEnd(LocalDateTime.of(2022, 12, 23, 12, 30));
		time1test.setInit(LocalTime.of(12, 00));
		time1test.setFinish(LocalTime.of(13, 00));
		time1test.setDiscount(10);
		time1test.setClient(client1);
		this.time1 = time1test;
		BDDMockito.given(this.timeOfferService.findTimeOfferById(TEST_TIMEOFFER_ID)).willReturn(this.time1);	
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/offers/time/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("timeOffer"))
				.andExpect(view().name("offers/time/createOrUpdateTimeOfferForm"));
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/offers/time/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("start", "23/12/2021 12:30")
				.param("end", "23/12/2022 12:30")
				.param("init", "12:30")
				.param("finish", "13:30")
				.param("discount", "10"))
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/offers/time/new")
					.with(csrf())
					.param("start", "lsqdufhlqhf")
					.param("end", "")
					.param("init", "gold")
					.param("finish", "")
					.param("discount", ""))
				.andExpect(model().attributeHasErrors("timeOffer"))
				.andExpect(model().attributeHasFieldErrors("timeOffer", "start"))
				.andExpect(model().attributeHasFieldErrors("timeOffer", "end"))
				.andExpect(model().attributeHasFieldErrors("timeOffer", "init"))
				.andExpect(model().attributeHasFieldErrors("timeOffer", "finish"))
				.andExpect(model().attributeHasFieldErrors("timeOffer", "discount"))
				.andExpect(view().name("offers/time/createOrUpdateTimeOfferForm"));
	}
	
	@WithMockUser(value = "user1", authorities = "client")
	@Test
	void testActivateSuccess() throws Exception {
		mockMvc.perform(get("/offers/time/{timeOfferId}/activate", TEST_TIMEOFFER_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/offers/time/"+TEST_TIMEOFFER_ID));
	}
	
	@WithMockUser(value = "user1", authorities = "client")
	@Test
	void testActivateHasErrors() throws Exception {
		mockMvc.perform(get("/offers/time/{timeOfferId}/activate", TEST_TIMEOFFER_ID+1))
				.andExpect(view().name("exception"));
	}

	

	
}