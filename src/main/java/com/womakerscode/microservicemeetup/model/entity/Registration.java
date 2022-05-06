package com.womakerscode.microservicemeetup.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder    //Essas anotações criam os constructos, getters e setters
@Entity
@Table
public class Registration {

    @Id  //vai dizer que é um elemento único na tabela/banco
    @Column(name = "registration_id") //esse vai ser o nome da nossa tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //tem que ter uma pessoa com esse ID?
    private Integer id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "date_of_registration")
    private String dateOfRegistration;

    @Column   //o nome da tabela vai ficar "registration" mesmo quando não especificamos
    private String registration;

}