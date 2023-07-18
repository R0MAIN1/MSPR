package com.mspr.payetonkawasrvc.service;

import com.mspr.payetonkawasrvc.core.ProductBean;
import com.mspr.payetonkawasrvc.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
   private ProductRepository productRepository;

   public ProductServiceImpl(ProductRepository productRepository) {
      this.productRepository = productRepository;
   }

   @Override
   public ProductBean addProduct(String name) {

      ProductBean productBean = new ProductBean();
      productBean.setName(name);

      return productRepository.save(productBean);
   }

   @Override
   public List<ProductBean> getAllProduct() {
      return productRepository.findAll();
   }

   @Override
   public ProductBean getProduct(Long productId) {
      return productRepository.findById(productId).get();
   }
}
