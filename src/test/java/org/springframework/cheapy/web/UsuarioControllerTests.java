package org.springframework.cheapy.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cheapy.configuration.SecurityConfiguration;
import org.springframework.cheapy.model.Code;
import org.springframework.cheapy.model.Municipio;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.model.Usuario;
import org.springframework.cheapy.service.UsuarioService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(value = UsuarioController.class, 
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration = SecurityConfiguration.class)
class UsuarioControllerTest {

	private static final int TEST_USUARIO_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UsuarioService usuarioService;

	@BeforeEach
	void setup() {
		User user = new User();
		Code code = new Code();
		code.setActivo(true);
		code.setCode("codeTest");
		user.setUsername("user");
		user.setPassword("user");
		Usuario usuario = new Usuario();
		usuario.setNombre("usuario");
		usuario.setApellidos("usuario");
		usuario.setDireccion("usuario");
		usuario.setMunicipio(Municipio.Sevilla);
		usuario.setEmail("usuario@gmail.com");
		usuario.setUsuar(user);
		BDDMockito.given(this.usuarioService.getCurrentUsuario()).willReturn(usuario);
		
		
	}

	@WithMockUser(value = "spring", authorities = "usuario")
	@Test
	void testShow() throws Exception {
		mockMvc.perform(get("/usuarios/show"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("usuario"))
				.andExpect(view().name("usuarios/usuariosShow"));
	}

	@WithMockUser(value = "spring", authorities = "usuario")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/usuarios/edit"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("usuario"))
				.andExpect(view().name("usuarios/createOrUpdateUsuarioForm"));
	}

	@WithMockUser(value = "spring", authorities = "usuario")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/usuarios/edit")
					.with(csrf())
					.param("usuar.password", "Contrasenya123")
					.param("nombre", "nombre")
					.param("apellidos", "apellidos")
					.param("direccion", "direccion")
					.param("municipio", "Sevilla")
					.param("email", "email@gmail.com"))
				.andExpect(status().is3xxRedirection());
	}
	

	@WithMockUser(value = "spring", authorities = "usuario")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/usuarios/edit")
				.with(csrf())
				.param("usuar.password", "")
				.param("nombre", "")
				.param("apellidos", "")
				.param("direccion", "")
				.param("municipio", "Sevil")
				.param("email", "email"))
				.andExpect(model().attributeHasErrors("usuario"))
				.andExpect(model().attributeHasFieldErrors("usuario", "nombre"))
				.andExpect(model().attributeHasFieldErrors("usuario", "apellidos"))
				.andExpect(model().attributeHasFieldErrors("usuario", "direccion"))
				.andExpect(model().attributeHasFieldErrors("usuario", "municipio"))
				.andExpect(model().attributeHasFieldErrors("usuario", "email"))
				.andExpect(view().name("usuarios/createOrUpdateUsuarioForm"));
	}
	
	@WithMockUser(value = "spring", authorities = "usuario")
	@Test
	void testInitDisable() throws Exception {
		mockMvc.perform(get("/usuarios/disable"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("usuario"))
			.andExpect(view().name("usuarios/usuariosDisable"));
	}
	
	@WithMockUser(value = "spring", authorities = "usuario")
	@Test
	void testProcessDisableSuccess() throws Exception {
		mockMvc.perform(post("/usuarios/disable")
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/login"));
	}
}
