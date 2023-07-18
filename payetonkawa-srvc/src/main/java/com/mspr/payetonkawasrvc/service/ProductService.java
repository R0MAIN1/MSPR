package com.mspr.payetonkawasrvc.service;

import com.mspr.payetonkawasrvc.core.ProductBean;

import java.util.List;

public interface ProductService {
   ProductBean addProduct(String name);
   List<ProductBean> getAllProduct();
   ProductBean getProduct(Long productId);
}
