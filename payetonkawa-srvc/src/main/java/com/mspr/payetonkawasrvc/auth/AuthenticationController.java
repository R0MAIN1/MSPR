package com.mspr.payetonkawasrvc.auth;

import com.mspr.payetonkawasrvc.config.JwtService;
import com.mspr.payetonkawasrvc.core.UserBean;
import com.mspr.payetonkawasrvc.repository.UserRepository;
import com.mspr.payetonkawasrvc.service.UserService;
import com.mspr.payetonkawasrvc.token.Token;
import com.mspr.payetonkawasrvc.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

   private final AuthenticationService service;
   private final TokenRepository tokenRepository;
   private final UserService userService;
   private final JwtService jwtService;

   @PostMapping("/register")
   public RegisterResponse register(@RequestBody RegisterRequest registerRequest,
                                                          final HttpServletRequest httpServletRequest ) {
      service.register(registerRequest, httpServletRequest);
      RegisterResponse registerResponse = new RegisterResponse();
      registerResponse.setMessage("Succès!, S'il vous plaît, vérifiez votre boîte mail pour terminer votre inscription");
      registerResponse.setSuccess(true);

      return registerResponse;
   }

   @PostMapping("/register/verify-email")
   public RegisterResponse verifyEmail(@RequestParam(name = "email") String email){
      UserBean userBean = userService.findByEmail(email);
      if(userBean != null){
         RegisterResponse registerResponse = new RegisterResponse();
         registerResponse.setMessage("Erreur!, Cette adresse email est deja utilise ");
         registerResponse.setSuccess(false);
         return registerResponse;
      }
      return RegisterResponse.builder()
              .success(true)
              .build();
   }

   @PostMapping("/authenticate")
   @ResponseStatus(HttpStatus.CREATED)
   public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
      return service.authenticate(request);
   }

   @PutMapping(path = "/confirm")
   @ResponseStatus(HttpStatus.OK)
   public String confirm(@RequestParam("token") String token) {
      Token theToken = tokenRepository.findToken(token).orElse(null);
      if(theToken != null){
         UserBean userBean = tokenRepository.findByUser(token).get();
         if (userBean.getIsEnabled()){
            return "This account has already been verified, please, login.";
         } else  if (!jwtService.isTokenExpired(token)){
            userService.setEnable(theToken.getUser().getEmail(), token);
            return "Email verified successfully. Now you can login to your account";
         }
      }
      return "Invalid verification token";
   }

   @PostMapping("/refresh-token")
   public void refreshToken(
           HttpServletRequest request,
           HttpServletResponse response
   ) throws IOException {
      service.refreshToken(request, response);
   }
}
