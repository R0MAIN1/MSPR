package com.mspr.payetonkawasrvc.service;

import com.mspr.payetonkawasrvc.core.UserBean;
import com.mspr.payetonkawasrvc.repository.UserRepository;
import com.mspr.payetonkawasrvc.token.Token;
import com.mspr.payetonkawasrvc.token.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
   private UserRepository userRepository;
   private TokenRepository tokenRepository;

   public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository) {
      this.userRepository = userRepository;
      this.tokenRepository = tokenRepository;
   }

   @Override
   public UserBean getDealer(Long dealerId) {
      return userRepository.findById(dealerId).get();
   }

   @Override
   public UserBean setEnable(String email, String token) {
      UserBean userBean = userRepository.findByEmail(email).get();
      Token t = tokenRepository.findToken(token).get();
      t.setExpired(true);
      userBean.setIsEnabled(true);
      tokenRepository.save(t);
      return userRepository.save(userBean);
   }

   @Override
   public UserBean findByEmail(String email) {
      return userRepository.findByEmail(email).orElse(null);
   }
}
