package javamaven.javamaven.controller.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordForm {
    private String oldPassword;
    private String newPassword;
}
