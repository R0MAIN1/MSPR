package com.mspr.payetonkawasrvc.repository;

import com.mspr.payetonkawasrvc.core.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserBean, Long> {
   Optional<UserBean> findByEmail(String email);
}
