package com.mspr.payetonkawasrvc.web;

import lombok.Data;

import java.util.List;

@Data
public class GetConfigurationResponse {
   private List<ListingProductDto> products;
   private Boolean error;
}
