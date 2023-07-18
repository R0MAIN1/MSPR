package com.mspr.payetonkawasrvc.repository;

import com.mspr.payetonkawasrvc.core.ProductBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<ProductBean, Long> {
   ProductBean save(ProductBean productBean);
}
