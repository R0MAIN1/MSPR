package com.mspr.payetonkawasrvc.service;

import com.mspr.payetonkawasrvc.core.ListingImageBean;
import com.mspr.payetonkawasrvc.core.UserBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingImageServiceImpl implements ListingImageService{
   private ListingImageRepository listingImageRepository;
   private UserService userService;

   public ListingImageServiceImpl(ListingImageRepository listingImageRepository, UserService userService) {
      this.listingImageRepository = listingImageRepository;
      this.userService = userService;
   }

   @Override
   public void add(List<ListingImageRequest> listingImageRequestList, String email) {
      listingImageRequestList.forEach(listingImageRequest -> {
         UserBean userBean = userService.findByEmail(email);
         ListingImageBean listingImageBean = new ListingImageBean();
         listingImageBean.setDealerId(userBean.getId());
         listingImageBean.setImageUrl(listingImageRequest.getImageUrl());
         listingImageBean.setStockId(listingImageRequest.getStockId());
         listingImageRepository.save(listingImageBean);
      });

   }

   @Override
   public List<ListingImageBean> findAllByStockId(Long productId) {
      return listingImageRepository.findAllByStockId(productId);
   }
}
