package com.womakerscode.microservicemeetup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.microservicemeetup.controller.dto.MeetupDTO;
import com.womakerscode.microservicemeetup.controller.dto.RegistrationDTO;
import com.womakerscode.microservicemeetup.controller.resource.MeetupController;
import com.womakerscode.microservicemeetup.controller.resource.RegistrationController;
import com.womakerscode.microservicemeetup.model.entity.Meetup;
import com.womakerscode.microservicemeetup.model.entity.Registration;
import com.womakerscode.microservicemeetup.service.MeetupService;
import com.womakerscode.microservicemeetup.service.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupController.class})
@AutoConfigureMockMvc
public class MeetupControllerTest {

    static String MEETUP_API = "/api/meetups";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MeetupService meetupService;

    @Test
    @DisplayName("Should create a meetup with success")
    public void createRMeetupTest() throws Exception {

        // cenario
        MeetupDTO meetupDTOBuilder = createNewRegistration();
        Meetup savedMeetup  = Meetup.builder().id(101)
                .event("aniversario").build();


        // execucao
        BDDMockito.given(meetupService.save(any(Meetup.class))).willReturn(savedMeetup);


        String json  = new ObjectMapper().writeValueAsString(meetupDTOBuilder);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MEETUP_API)   //aqui eu chamo a rota, que definimos como String estatica la em cima
                .contentType(MediaType.APPLICATION_JSON)   //aqui eu digo que é do tipo json
                .accept(MediaType.APPLICATION_JSON)  //aqui ele so vai aceitar o json
                .content(json);    //aqui adiciona o conteúdo

        // assert
        mockMvc
                .perform(request)
                .andExpect(status().isCreated())  //ele espera o status de created e espera também... próximas linhas
                .andExpect(jsonPath("id").value(101))
                .andExpect(jsonPath("event").value(meetupDTOBuilder.getEvent()));
                //.andExpect(jsonPath("dateOfRegistration").value(registrationDTOBuilder.getDateOfRegistration()))
                //.andExpect(jsonPath("registration").value(registrationDTOBuilder.getRegistration()));
    }

    private MeetupDTO createNewRegistration() {
        return  MeetupDTO.builder().id(101).event("Aniversario").build();
    }

}
