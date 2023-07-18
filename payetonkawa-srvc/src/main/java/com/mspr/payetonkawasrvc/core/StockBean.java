package com.mspr.payetonkawasrvc.core;

import com.mspr.payetonkawasrvc.service.TypeOfStatuts;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stock")
@Entity(name = "stock")
public class StockBean {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @Column(name = "produit_id")
   private Long productId;
   private String description;
   private BigDecimal price;
   @Enumerated(EnumType.STRING)
   private TypeOfStatuts statuts;
   @Column(name = "revendeur_id")
   private Long revendeurId;
   @Column(name = "qte_disponible")
   private Long qteDisponible;
   @Column(name = "qte_restante")
   private Long qteRestante;
   @Column(name = "creation_date")
   private LocalDateTime creationDate;
   @Column(name = "last_modification_date")
   private LocalDateTime lastModificationDate;
}
