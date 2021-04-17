package org.springframework.cheapy.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cheapy.configuration.SecurityConfiguration;
import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.Code;
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.SpeedOffer;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.FoodOfferService;
import org.springframework.cheapy.service.NuOfferService;
import org.springframework.cheapy.service.SpeedOfferService;
import org.springframework.cheapy.service.TimeOfferService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(value = ClientController.class, 
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration = SecurityConfiguration.class)
class ClientControllerTest {

	private static final int TEST_CLIENT_ID = 1;

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClientService clientService;
	
	@MockBean
	private FoodOfferService foodOfferService;
	
	@MockBean
	private SpeedOfferService speedOfferService;
	
	@MockBean
	private NuOfferService nuOfferService;
	
	@MockBean
	private TimeOfferService timeOfferService;


	@BeforeEach
	void setup() {
		User user1 = new User();
		Code code1 = new Code();
		code1.setActivo(true);
		code1.setCode("codeTest1");
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
		client1.setCode(code1);
		client1.setFood("client1");
		client1.setUsuar(user1);
		BDDMockito.given(this.clientService.getCurrentClient()).willReturn(client1);
		
		List<FoodOffer> foodOffer = new ArrayList<FoodOffer>();
		List<SpeedOffer> speedOffer = new ArrayList<SpeedOffer>();
		List<NuOffer> nuOffer = new ArrayList<NuOffer>();
		List<TimeOffer> timeOffer = new ArrayList<TimeOffer>();
		
		
		BDDMockito.given(this.foodOfferService.findFoodOfferByUserId(client1.getId())).willReturn(foodOffer);
		BDDMockito.given(this.speedOfferService.findSpeedOfferByUserId(client1.getId())).willReturn(speedOffer);
		BDDMockito.given(this.nuOfferService.findNuOfferByUserId(client1.getId())).willReturn(nuOffer);
		BDDMockito.given(this.timeOfferService.findTimeOfferByUserId(client1.getId())).willReturn(timeOffer);
		
		
		
	}
	
	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testShow() throws Exception {
		mockMvc.perform(get("/clients/show"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("client"))
				.andExpect(view().name("clients/clientShow"));
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/clients/edit"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("client"))
				.andExpect(view().name("clients/createOrUpdateClientForm"));
	}

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/clients/edit")
					.with(csrf())
					.param("usuar.password", "Contrasenya123")
					.param("init", "11:30")
					.param("finish", "23:30")
					.param("name", "Restaurante Pepe")
					.param("email", "pepe@hotmail.es")
					.param("address", "Pirineos 10")
					.param("telephone", "654999999")
					.param("description", "Comida al mejor precio")
					.param("food", "Americana")
					.param("municipio", "Dos_Hermanas"))
				.andExpect(status().is3xxRedirection());
	}
	

	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/clients/edit")
					.with(csrf())
					.param("usuar.password", "")
					.param("init", "24:30")
					.param("finish", "a:30")
					.param("name", "")
					.param("email", "")
					.param("address", "")
					.param("telephone", "654999")
					.param("description", "")
					.param("food", "")
					.param("municipio", "Dos Hermanas"))
				.andExpect(model().attributeHasErrors("client"))
				.andExpect(model().attributeHasFieldErrors("client", "usuar.password"))
				.andExpect(model().attributeHasFieldErrors("client", "init"))
				.andExpect(model().attributeHasFieldErrors("client", "finish"))
				.andExpect(model().attributeHasFieldErrors("client", "name"))
				.andExpect(model().attributeHasFieldErrors("client", "email"))
				.andExpect(model().attributeHasFieldErrors("client", "address"))
				.andExpect(model().attributeHasFieldErrors("client", "telephone"))
				.andExpect(model().attributeHasFieldErrors("client", "description"))
				.andExpect(model().attributeHasFieldErrors("client", "food"))
				.andExpect(model().attributeHasFieldErrors("client", "municipio"))
				
				.andExpect(view().name("clients/createOrUpdateClientForm"));
	}
	
	@WithMockUser(value = "user1", authorities = "client")
	@Test
	void testInitDisableForm() throws Exception {
		mockMvc.perform(get("/clients/disable"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("client"))
				.andExpect(view().name("/clients/clientDisable"));
	}
	
	@WithMockUser(value = "spring", authorities = "client")
	@Test
	void testProcessDisableFormSuccess() throws Exception {
		mockMvc.perform(post("/clients/disable")
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/login"));
	}
	
	
}
