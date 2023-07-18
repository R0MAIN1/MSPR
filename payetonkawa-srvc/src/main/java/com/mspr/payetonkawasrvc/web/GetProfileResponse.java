package com.mspr.payetonkawasrvc.web;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetProfileResponse {
   private String firstname;
   private String lastname;
   private String email;
}
