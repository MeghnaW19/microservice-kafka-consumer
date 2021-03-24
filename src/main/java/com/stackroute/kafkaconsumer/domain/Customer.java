package com.stackroute.kafkaconsumer.domain;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document
public class Customer {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
}
