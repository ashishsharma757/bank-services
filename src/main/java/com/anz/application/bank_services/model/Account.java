package com.anz.application.bank_services.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long accountNumber;

//    @NotNull
//    @NotEmpty(message = "firstname is required")
    private String firstName;

    private String lastName;

//    @NotNull
//    @NotEmpty(message = "phoneNumber is required")
//    @Size(min = 10, max = 10, message = "phone number is invalid")
    private String phoneNumber;

    private double currentBalance;

    @Override
    public String toString() {
        return "Account{" +
                "  firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", currentBalance=" + currentBalance +
                '}';
    }
}


