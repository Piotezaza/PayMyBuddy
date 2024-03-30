package com.pmb.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int id;

    @NotNull
    @NotEmpty(message = "First name should not be empty")
    private String prenom;

    @NotNull
    @NotEmpty(message = "Last name should not be empty")
    private String nom;

    @NotNull
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    @NotNull
    @NotEmpty(message = "Password should not be empty")
    private String password;
    private String matchingPassword;
}
