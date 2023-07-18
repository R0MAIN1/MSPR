package com.mspr.payetonkawasrvc.web;

import com.mspr.payetonkawasrvc.service.TypeOfStatuts;
import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ListingStockDetailsDto {
   private Long id;
   private String nameProduct;
   private List<String> images;
   private String description;
   private BigDecimal price;
   private TypeOfStatuts statuts;
   private Long qteDisponible;
   private Long qteRestante;
   private LocalDateTime creationDate;
   private LocalDateTime lastModificationDate;
}
