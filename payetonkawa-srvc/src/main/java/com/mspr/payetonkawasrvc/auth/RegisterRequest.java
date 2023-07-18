package com.mspr.payetonkawasrvc.auth;

import com.mspr.payetonkawasrvc.core.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
   private String firstname;
   private String lastname;
   private String email;
   private String password;
}
