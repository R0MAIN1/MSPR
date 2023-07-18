package com.mspr.payetonkawasrvc.service;

import com.mspr.payetonkawasrvc.core.ListingImageBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingImageRepository extends JpaRepository<ListingImageBean, Long> {
   List<ListingImageBean> findAllByStockId(Long stockId);
}
