package com.mspr.payetonkawasrvc.service;

import com.mspr.payetonkawasrvc.core.StockBean;
import com.mspr.payetonkawasrvc.core.UserBean;
import com.mspr.payetonkawasrvc.repository.UserRepository;
import com.mspr.payetonkawasrvc.repository.ProductRepository;
import com.mspr.payetonkawasrvc.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

@Service
public class StockServiceImpl implements StockService {
   private StockRepository stockRepository;
   private ProductRepository productRepository;
   private UserRepository userRepository;

   public StockServiceImpl(StockRepository stockRepository, ProductRepository productRepository, UserRepository userRepository) {
      this.stockRepository = stockRepository;
      this.productRepository = productRepository;
      this.userRepository = userRepository;
   }

   @Override
   public StockBean add(AddStockRequest addStockRequest, String dealerId) {
      UserBean userBean = userRepository.findByEmail(dealerId).get();

      StockBean stockBean = new StockBean();
      stockBean.setCreationDate(LocalDateTime.now());
      stockBean.setQteDisponible(addStockRequest.getQteDisponible());
      stockBean.setRevendeurId(userBean.getId());
      stockBean.setProductId(addStockRequest.getProductId());
      stockBean.setPrice(addStockRequest.getPrice());
      stockBean.setDescription(addStockRequest.getDescription());
      stockBean.setStatuts(TypeOfStatuts.DISPONIBLE);
      return stockRepository.save(stockBean);
   }

   @Override
   public List<StockBean> getProductsOfDealer(String dealerId) {
      UserBean userBean = userRepository.findByEmail(dealerId).get();
      return stockRepository.findAllByRevendeurId(userBean.getId());
   }

   @Override
   public StockBean getStockDetails(Long stockId, String dealerId) {
      UserBean userBean = userRepository.findByEmail(dealerId).get();
     return stockRepository.findByIdAndRevendeurId(stockId, userBean.getId());
   }

   @Override
   public StockBean update(UpdateStockRequest updateStockRequest, String dealerId, Long stockId) {
      StockBean stockBean = stockRepository.findById(stockId).get();

      stockBean.setLastModificationDate(LocalDateTime.now());
      stockBean.setQteDisponible(updateStockRequest.getQteDisponible());
      stockBean.setStatuts(updateStockRequest.getStatuts());
      stockBean.setPrice(updateStockRequest.getPrice());
      stockBean.setDescription(updateStockRequest.getDescription());

      return stockRepository.save(stockBean);
   }
}
