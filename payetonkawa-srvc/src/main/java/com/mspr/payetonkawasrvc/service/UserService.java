package com.mspr.payetonkawasrvc.service;

import com.mspr.payetonkawasrvc.core.UserBean;

public interface UserService {
   UserBean getDealer(Long dealerId);
   UserBean setEnable(String email, String token);
   UserBean findByEmail(String email);
}
