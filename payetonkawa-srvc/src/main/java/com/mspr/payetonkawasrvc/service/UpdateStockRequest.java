package com.mspr.payetonkawasrvc.service;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateStockRequest {
   private Long qteDisponible;
   private TypeOfStatuts statuts;
   private String description;
   private BigDecimal price;
}
