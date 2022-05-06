package com.womakerscode.microservicemeetup.repository;

import com.womakerscode.microservicemeetup.model.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    //Não precisa fazer o do save por causa do JPA

    boolean existsByRegistration(String registration);

    Optional<Registration> findByRegistration(String registrationAtrb);
}