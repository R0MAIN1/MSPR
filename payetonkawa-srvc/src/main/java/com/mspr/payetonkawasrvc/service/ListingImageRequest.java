package com.mspr.payetonkawasrvc.service;

import lombok.Data;

@Data
public class ListingImageRequest {
   private String imageUrl;
   private Long stockId;
}
