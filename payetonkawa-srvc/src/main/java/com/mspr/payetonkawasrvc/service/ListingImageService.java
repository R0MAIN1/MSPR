package com.mspr.payetonkawasrvc.service;


import com.mspr.payetonkawasrvc.core.ListingImageBean;

import java.util.List;

public interface ListingImageService {
   void add(List<ListingImageRequest> listingImageRequestList, String email);
   List<ListingImageBean> findAllByStockId(Long productId);
}
