package com.mspr.payetonkawasrvc.token;

import java.util.List;
import java.util.Optional;

import com.mspr.payetonkawasrvc.core.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Long> {

   @Query(value = """
      select t from Token t inner join _user u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false )\s
      """)
   List<Token> findAllValidTokenByUser(Long id);

   @Query(value = """
      select u from Token t inner join _user u\s
      on t.user.id = u.id\s
      where t.token = :token  \s
      """)
   Optional<UserBean> findByUser(@Param("token") String token);

   @Query(value = "SELECT t from Token t where t.token = :token and t.expired = false ")
   Optional<Token> findToken(@Param("token") String token);
}
