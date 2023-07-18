package com.mspr.payetonkawasrvc.service;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddStockRequest {
   private Long productId;
   private Long qteDisponible;
   private BigDecimal price;
   private String description;
}
