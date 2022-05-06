package com.womakerscode.microservicemeetup.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.microservicemeetup.controller.resource.RegistrationController;
import com.womakerscode.microservicemeetup.exception.BusinessException;
import com.womakerscode.microservicemeetup.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.model.entity.Registration;
import com.womakerscode.microservicemeetup.service.RegistrationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {RegistrationController.class}) //aqui a gente informa qual classe queremos testar com esse teste de controller
@AutoConfigureMockMvc //anotação do junit, consegue trazer expressões mockadas do nosso controller
public class RegistrationControllerTest {

    static String REGISTRATION_API = "/api/registration";  //static quer dizer que não pode mudar essa String, letra maiuscula é padrao para string estatica

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RegistrationService registrationService;

    @Test
    @DisplayName("Should create a registration with success")
    public void createRegistrationTest() throws Exception {

        // cenario
        RegistrationDTO registrationDTOBuilder = createNewRegistration();
        Registration savedRegistration  = Registration.builder().id(101)
                .name("Ana Carolina").dateOfRegistration("10/10/2021").registration("001").build();


        // execucao
        BDDMockito.given(registrationService.save(any(Registration.class))).willReturn(savedRegistration);


        String json  = new ObjectMapper().writeValueAsString(registrationDTOBuilder);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(REGISTRATION_API)   //aqui eu chamo a rota, que definimos como String estatica la em cima
                .contentType(MediaType.APPLICATION_JSON)   //aqui eu digo que é do tipo json
                .accept(MediaType.APPLICATION_JSON)  //aqui ele so vai aceitar o json
                .content(json);    //aqui adiciona o conteúdo

        // assert
        mockMvc
                .perform(request)
                .andExpect(status().isCreated())  //ele espera o status de created e espera também... próximas linhas
                .andExpect(jsonPath("id").value(101))
                .andExpect(jsonPath("name").value(registrationDTOBuilder.getName()))
                .andExpect(jsonPath("dateOfRegistration").value(registrationDTOBuilder.getDateOfRegistration()))
                .andExpect(jsonPath("registration").value(registrationDTOBuilder.getRegistration()));
    }

    @Test
    @DisplayName("Should throw exception when it hadn't have enough date for the test.")
    public void createInvalidRegistrationTest() throws Exception {

        String json  = new ObjectMapper().writeValueAsString(new RegistrationDTO());

        MockHttpServletRequestBuilder request  = MockMvcRequestBuilders
                .post(REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should throw exception when try to create a new registration when there is already a registration created.")
    public void createRegistrationWithDuplicatedRegistration() throws Exception {

        RegistrationDTO dto = createNewRegistration();
        String json  = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(registrationService.save(any(Registration.class))).willThrow(new BusinessException("Registration already created"));

        MockHttpServletRequestBuilder request  = MockMvcRequestBuilders
                .post(REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Registration already created"));
    }

    @Test
    @DisplayName("Should get registration information")
    public void getRegistrationTest() throws Exception {

        Integer id = 11;

        Registration student = Registration.builder()
                .id(id)
                .name(createNewRegistration().getName())
                .dateOfRegistration(createNewRegistration().getDateOfRegistration())
                .registration(createNewRegistration().getRegistration()).build();

        BDDMockito.given(registrationService.getRegistrationById(id)).willReturn(Optional.of(student));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value(createNewRegistration().getName()))
                .andExpect(jsonPath("dateOfRegistration").value(createNewRegistration().getDateOfRegistration()))
                .andExpect(jsonPath("registration").value(createNewRegistration().getRegistration()));

    }

    @Test
    @DisplayName("Should return NOT FOUND when registration doesn't exists")
    public void registrationNotFoundTest() throws Exception {

        BDDMockito.given(registrationService.getRegistrationById(anyInt())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete a registration")
    public void deleteRegistrationTest() throws Exception {

        BDDMockito.given(registrationService
                        .getRegistrationById(anyInt()))
                .willReturn(Optional.of(Registration.builder().id(11).build()));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(REGISTRATION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return resource not found when no registration is found to delete")
    public void deleteNonExistentRegistrationTest() throws Exception {

        BDDMockito.given(registrationService
                .getRegistrationById(anyInt())).willReturn(Optional.empty());


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(REGISTRATION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update registration info")
    public void updateRegistrationTest() throws Exception {

        Integer id = 11;
        String json = new ObjectMapper().writeValueAsString(createNewRegistration());

        Registration updatingRegistration =
                Registration.builder()
                        .id(id)
                        .name("Julia Carolina")
                        .dateOfRegistration("10/10/2021")
                        .registration("323")
                        .build();

        BDDMockito.given(registrationService.getRegistrationById(anyInt()))
                .willReturn(Optional.of(updatingRegistration));

        Registration updatedRegistration =
                Registration.builder()
                        .id(id)
                        .name("Ana Carolina")
                        .dateOfRegistration("10/10/2021")
                        .registration("323")
                        .build();

        BDDMockito.given(registrationService
                        .update(updatingRegistration))
                .willReturn(updatedRegistration);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(REGISTRATION_API.concat("/" + 1))
                .contentType(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value(createNewRegistration().getName()))
                .andExpect(jsonPath("dateOfRegistration").value(createNewRegistration().getDateOfRegistration()))
                .andExpect(jsonPath("registration").value("323"));
    }

    @Test
    @DisplayName("Should return 404 when try to update a non existent registration")
    public void updateNonExistentRegistrationTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(createNewRegistration());
        BDDMockito.given(registrationService.getRegistrationById(anyInt()))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(REGISTRATION_API.concat("/" + 1))
                .contentType(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should filter registration")
    public void findRegistrationTest() throws Exception {

        Integer id = 11;

        Registration registration = Registration.builder()
                .id(id)
                .name(createNewRegistration().getName())
                .dateOfRegistration(createNewRegistration().getDateOfRegistration())
                .registration(createNewRegistration().getRegistration()).build();

        BDDMockito.given(registrationService.find(Mockito.any(Registration.class), Mockito.any(Pageable.class)) )
                .willReturn(new PageImpl<Registration>(Arrays.asList(registration), PageRequest.of(0,100), 1));


        String queryString = String.format("?name=%s&dateOfRegistration=%s&page=0&size=100",
                registration.getRegistration(), registration.getDateOfRegistration());


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements"). value(1))
                .andExpect(jsonPath("pageable.pageSize"). value(100))
                .andExpect(jsonPath("pageable.pageNumber"). value(0));

    }





    private RegistrationDTO createNewRegistration() {
        return  RegistrationDTO.builder().id(101).name("Ana Carolina").dateOfRegistration("10/10/2021").registration("001").build();
    }


}
