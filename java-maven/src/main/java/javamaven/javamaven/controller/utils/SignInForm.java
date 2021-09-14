package javamaven.javamaven.controller.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInForm {
    private final String email;
    private final String password;
}
