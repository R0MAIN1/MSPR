package com.mspr.payetonkawasrvc.service;

import com.mspr.payetonkawasrvc.core.StockBean;

import java.util.List;

public interface StockService {
   StockBean add(AddStockRequest addStockRequest, String dealerId );
   List<StockBean> getProductsOfDealer(String userId);
   StockBean getStockDetails(Long stockId, String dealerId);
   StockBean update(UpdateStockRequest updateStockRequest, String dealerId, Long stockId);
}
