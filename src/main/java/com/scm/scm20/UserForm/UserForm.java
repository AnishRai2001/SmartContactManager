package com.scm.scm20.UserForm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserForm {
    private String name;
    private String email;
    private String password;  // Lowercase 'p' for consistency
    private String about;
    private String phone;  // Match with the form field
}

