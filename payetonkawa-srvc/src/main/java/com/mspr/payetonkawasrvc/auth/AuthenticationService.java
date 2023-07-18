package com.mspr.payetonkawasrvc.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mspr.payetonkawasrvc.config.JwtService;
import com.mspr.payetonkawasrvc.core.Role;
import com.mspr.payetonkawasrvc.core.UserBean;
import com.mspr.payetonkawasrvc.email.EmailSenderServiceServiceImpl;
import com.mspr.payetonkawasrvc.exception.UserAlreadyExistsException;
import com.mspr.payetonkawasrvc.repository.UserRepository;
import com.mspr.payetonkawasrvc.token.Token;
import com.mspr.payetonkawasrvc.token.TokenRepository;
import com.mspr.payetonkawasrvc.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
   private final UserRepository repository;
   private final TokenRepository tokenRepository;
   private final PasswordEncoder passwordEncoder;
   private final JwtService jwtService;
   private final AuthenticationManager authenticationManager;
   private final EmailSenderServiceServiceImpl emailSenderServiceImpl;
   @Value("${application.frontend.link}")
   private String link_frontend;

   public void register(RegisterRequest request, HttpServletRequest httpServletRequest ) {
      boolean userExists = repository.findByEmail(request.getEmail())
              .isPresent();

      if (userExists) {
         throw new UserAlreadyExistsException(
                 "User with email "+request.getEmail() + " already exists");
      }

      var user = UserBean.builder()
              .firstname(request.getFirstname())
              .lastname(request.getLastname())
              .email(request.getEmail())
              .password(passwordEncoder.encode(request.getPassword()))
              .role(Role.DEALER)
              .creationDate(LocalDateTime.now())
              .isEnabled(false)
              .deleted(false)
              .build();
      var savedUser = repository.saveAndFlush(user);
      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);
      saveUserToken(savedUser, jwtToken);

      String link = link_frontend+"confirm?token="+jwtToken;

      emailSenderServiceImpl.send(
              request.getEmail(),
              buildEmail(request.getFirstname()+" "+ request.getLastname(), link));
   }

   public AuthenticationResponse authenticate(AuthenticationRequest request) {

      Authentication authentication =  authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
      );
      AuthenticationResponse authenticationResponse = new AuthenticationResponse();
      var userBean = repository.findByEmail(request.getEmail())
              .orElseThrow();

      if(userBean != null){
         if(userBean.getIsEnabled()){
            if (authentication != null) {
               revokeAllUserTokens(userBean);
               var jwtToken = jwtService.generateToken(userBean);
               var refreshToken = jwtService.generateRefreshToken(userBean);
               saveUserToken(userBean, jwtToken);
               authenticationResponse.setAccess_token(jwtToken);
               authenticationResponse.setRefresh_token(refreshToken);
               authenticationResponse.setMessage("connected");
               authenticationResponse.setSuccess(true);
            }
            else{
               authenticationResponse.setMessage("The user is not authenticated");
               authenticationResponse.setSuccess(false);
            }
            return authenticationResponse;

         }
         authenticationResponse.setMessage("This account not been verified");
         authenticationResponse.setSuccess(false);
         return authenticationResponse;
      }
      authenticationResponse.setMessage("this account not exist, please, signIn");
      authenticationResponse.setSuccess(false);

      return authenticationResponse;
   }

   private void saveUserToken(UserBean user, String jwtToken) {
      var token = Token.builder()
              .user(user)
              .token(jwtToken)
              .tokenType(TokenType.BEARER)
              .expired(false)
              .build();
      tokenRepository.save(token);
   }

   public void refreshToken(
           HttpServletRequest request,
           HttpServletResponse response
   ) throws IOException {
      final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
      final String refreshToken;
      final String userEmail;
      if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
         return;
      }
      refreshToken = authHeader.substring(7);
      userEmail = jwtService.extractUsername(refreshToken);
      if (userEmail != null) {
         var user = this.repository.findByEmail(userEmail)
                 .orElseThrow();
         if (jwtService.isTokenValid(refreshToken, user)) {
            var accessToken = jwtService.generateToken(user);
            var _refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);
            var authResponse = AuthenticationResponse.builder()
                    .access_token(accessToken)
                    .refresh_token(_refreshToken)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
         }
      }
   }

   private void revokeAllUserTokens(UserBean user) {
      var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
      if (validUserTokens.isEmpty())
         return;
      validUserTokens.forEach(token -> {
         token.setExpired(true);
      });
      tokenRepository.saveAll(validUserTokens);
   }

   private String buildEmail(String name, String link) {
      return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
              "\n" +
              "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
              "\n" +
              "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
              "    <tbody><tr>\n" +
              "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
              "        \n" +
              "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
              "          <tbody><tr>\n" +
              "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
              "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
              "                  <tbody><tr>\n" +
              "                    <td style=\"padding-left:10px\">\n" +
              "                  \n" +
              "                    </td>\n" +
              "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
              "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
              "                    </td>\n" +
              "                  </tr>\n" +
              "                </tbody></table>\n" +
              "              </a>\n" +
              "            </td>\n" +
              "          </tr>\n" +
              "        </tbody></table>\n" +
              "        \n" +
              "      </td>\n" +
              "    </tr>\n" +
              "  </tbody></table>\n" +
              "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
              "    <tbody><tr>\n" +
              "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
              "      <td>\n" +
              "        \n" +
              "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
              "                  <tbody><tr>\n" +
              "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
              "                  </tr>\n" +
              "                </tbody></table>\n" +
              "        \n" +
              "      </td>\n" +
              "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
              "    </tr>\n" +
              "  </tbody></table>\n" +
              "\n" +
              "\n" +
              "\n" +
              "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
              "    <tbody><tr>\n" +
              "      <td height=\"30\"><br></td>\n" +
              "    </tr>\n" +
              "    <tr>\n" +
              "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
              "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
              "        \n" +
              "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
              "        \n" +
              "      </td>\n" +
              "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
              "    </tr>\n" +
              "    <tr>\n" +
              "      <td height=\"30\"><br></td>\n" +
              "    </tr>\n" +
              "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
              "\n" +
              "</div></div>";
   }
}
