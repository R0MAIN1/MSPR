package com.mspr.payetonkawasrvc.web;

import com.mspr.payetonkawasrvc.service.TypeOfStatuts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingStockDto {
   private Long id;
   private String nameProduct;
   private String image;
   private String Description;
   private BigDecimal price;
   private TypeOfStatuts statuts;
}
