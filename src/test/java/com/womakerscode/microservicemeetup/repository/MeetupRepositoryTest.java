package com.womakerscode.microservicemeetup.repository;

import com.womakerscode.microservicemeetup.model.entity.Meetup;
import com.womakerscode.microservicemeetup.model.entity.Registration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test1")
//@DataJpaTest
/*public class MeetupRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MeetupRepository meetupRepository;

    @Test
    @DisplayName("Should save a meetup")
    /*public void saveMeetupTest() {

        Meetup newEvent = createNewMeetup("Microservice");

        Meetup savedMeetup = meetupRepository.save(newEvent);

        assertThat(savedMeetup.getId()).isNotNull();

    }

    @Test
    @DisplayName("Should delete a metup from the base")
    public void deleteMeetup() {

        Meetup newEvent = createNewMeetup("323");
        entityManager.persist(newEvent);

        Registration foundRegistration = entityManager
                .find(Registration.class, newEvent.getId());
        meetupRepository.delete(foundRegistration);

        Registration deleteRegistration = entityManager
                .find(Registration.class, newEvent.getId());

        assertThat(deleteRegistration).isNull();


    public static Meetup createNewMeetup(String meetup) {
        return Meetup.builder()
                .event(meetup)
                .meetupDate("10/10/2022")
                .build();
    }

}*/
