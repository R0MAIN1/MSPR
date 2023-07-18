package com.mspr.payetonkawasrvc.repository;

import com.mspr.payetonkawasrvc.core.StockBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StockRepository extends JpaRepository<StockBean, Long> {
   StockBean save(StockBean stockBean);
   List<StockBean> findAllByRevendeurId(Long dealerId);
   StockBean findByIdAndRevendeurId(Long stockId, Long dealerId);
}
