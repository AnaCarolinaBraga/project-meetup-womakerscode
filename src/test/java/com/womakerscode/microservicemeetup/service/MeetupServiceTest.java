package com.womakerscode.microservicemeetup.service;

import com.womakerscode.microservicemeetup.model.entity.Meetup;
import com.womakerscode.microservicemeetup.model.entity.Registration;
import com.womakerscode.microservicemeetup.repository.MeetupRepository;
import com.womakerscode.microservicemeetup.repository.RegistrationRepository;
import com.womakerscode.microservicemeetup.service.impl.MeetupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MeetupServiceTest {

    MeetupService meetupService;

    @MockBean
    RegistrationService registrationService;

    @MockBean
    RegistrationRepository registrationRepository;

    @MockBean
    MeetupRepository meetupRepository;

    @BeforeEach
    public void setUp(){
        this.meetupService = new MeetupServiceImpl(meetupRepository);
    }

    @Test
    @DisplayName("Should save an meetup")
    public void saveMeetup(){
        Meetup meetup = createValidMeetup();
        PageRequest pageRequest = PageRequest.of(0,10);

        List<Meetup> listMeetup = Arrays.asList(meetup);
        Page<Meetup> page = new PageImpl<Meetup>(Arrays.asList(meetup),
                PageRequest.of(0,10), 1);

        Mockito.when(meetupRepository.findByRegistrationOnMeetup(Mockito.anyString(),Mockito.anyString(),Mockito.any(PageRequest.class))).thenReturn(page);
        Mockito.when(meetupRepository.save(meetup)).thenReturn(createValidMeetup());

        Meetup savedMeetup = meetupService.save(meetup);

        assertThat(savedMeetup.getId()).isEqualTo(101);
        assertThat(savedMeetup.getEvent()).isEqualTo("Aniversario");
        assertThat(savedMeetup.getRegistration()).isEqualTo(createValidRegistration());
        assertThat(savedMeetup.getMeetupDate()).isEqualTo("10/10/2022");
        assertThat(savedMeetup.getRegistered()).isEqualTo(true);
    }

    @Test
    @DisplayName("Should update a Meetup")
    public void updateMeetupTest() {

        Integer id = 20;
        Meetup updatingMeetup = Meetup.builder().id(20).build();

        Meetup updatedMeetup = createValidMeetup();
        updatedMeetup.setId(id);

        Mockito.when(meetupRepository.save(updatingMeetup)).thenReturn(updatedMeetup);
        Meetup meetup = meetupService.update(updatingMeetup);

        // assert
        assertThat(meetup.getId()).isEqualTo(updatedMeetup.getId());
        assertThat(meetup.getEvent()).isEqualTo(updatedMeetup.getEvent());
        assertThat(meetup.getRegistration()).isEqualTo(updatedMeetup.getRegistration());
        assertThat(meetup.getMeetupDate()).isEqualTo(updatedMeetup.getMeetupDate());
        assertThat(meetup.getRegistered()).isEqualTo(updatedMeetup.getRegistered());

    }

    @Test
    @DisplayName("Should get an meetup by Id")
    public void getByIdTest(){

        Integer id = 11;
        Meetup meetup = createValidMeetup();
        meetup.setId(id);
        Mockito.when(meetupRepository.findById(id)).thenReturn(Optional.of(meetup));

        Optional<Meetup> foundRegistration = meetupService.getById(id);

        assertThat(foundRegistration.isPresent()).isTrue();
        assertThat(foundRegistration.get().getId()).isEqualTo(id);
        assertThat(foundRegistration.get().getEvent()).isEqualTo(meetup.getEvent());
        assertThat(foundRegistration.get().getRegistration()).isEqualTo(meetup.getRegistration());
        assertThat(foundRegistration.get().getRegistered()).isEqualTo(meetup.getRegistered());
    }

    @Test
    @DisplayName("Should return empty when get an meetup by id that doesn't exist")
    public void meetupNotFoundByIdTest() {

        Integer id = 11;
        Mockito.when(meetupRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Meetup> meetup  = meetupService.getById(id);

        assertThat(meetup.isPresent()).isFalse();
    }

    @Test
    @DisplayName("should not save a new meetup when the registration is nonexistent")
    public void dontCreateMeetupWithInvalidRegistrationTest(){

        Meetup meetup = createMeetupWithoutRegistration();
        Integer id = 22;

        Mockito.when(registrationRepository.findById(id)).thenReturn(Optional.empty());
        Optional<Registration> registration = registrationService.getRegistrationById(id);

        assertThat(registration.isPresent()).isFalse();
        Mockito.verify(meetupRepository,Mockito.never()).save(meetup);

    }

    @Test
    @DisplayName("Should delete a meetup")
    public void deleteMeetup() {

        Meetup meetup = Meetup.builder().id(11).build();

        assertDoesNotThrow(() -> meetupService.delete(meetup));

        Mockito.verify(meetupRepository, Mockito.times(1)).delete(meetup);

    }


    private Meetup createValidMeetup(){
        return Meetup.builder()
                .id(101)
                .event("Aniversario")
                .registration(createValidRegistration())
                .meetupDate("10/10/2022")
                .registered(true)
                .build();
    }

    private Meetup createMeetupWithoutRegistration(){
        return Meetup.builder()
                .id(101)
                .event("Aniversario")
                .meetupDate("10/10/2022")
                .registered(true)
                .build();
    }


    private Registration createValidRegistration() {
        return Registration.builder()
                .id(101)
                .name("Ana Carolina")
                .dateOfRegistration("01/04/2022")
                .registration("001")
                .build();
    }

}
