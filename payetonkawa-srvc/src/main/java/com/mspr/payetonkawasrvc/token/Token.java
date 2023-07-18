package com.mspr.payetonkawasrvc.token;

import com.mspr.payetonkawasrvc.core.UserBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

   @Id
   @GeneratedValue
   public Long id;

   @Column(unique = true)
   public String token;

   @Enumerated(EnumType.STRING)
   public TokenType tokenType = TokenType.BEARER;

   public boolean expired = false;
   private LocalDateTime confirmedAt;

   @OneToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "user_id")
   private UserBean user;
}
